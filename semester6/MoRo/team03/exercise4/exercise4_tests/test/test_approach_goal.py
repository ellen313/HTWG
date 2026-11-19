# __author__ = "Michael Blaich, Florian Drescher, Christopher Gogl"

import os
import atexit
import signal
import time
import subprocess
import unittest

import rclpy
from rclpy.node import Node as RclpyNode
from geometry_msgs.msg import TwistStamped
from nav_msgs.msg import Odometry

# Test-Parameter
robot_velocity_max = 0.25
robot_omega_max = 2.5
robot_tolerance = 0.1
robot_target_x = 1.5
robot_target_y = 1.0


def _kill_proc_group(proc: subprocess.Popen):
    """Failsafe: gesamte Prozessgruppe bei Prozessende des Python-Interpreters killen."""
    try:
        if proc and proc.poll() is None:
            os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
    except Exception:
        pass


class TestApproachGoal(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        rclpy.init()
        cls._startup_failed = False

        # 1) Gazebo/Robot starten (eigene Prozessgruppe)
        try:
            cls._gazebo_proc = subprocess.Popen(
                ["ros2", "launch", "jk_rollin_gazebo",
                    "jk_rollin.gazebo.launch.py"],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                env=os.environ.copy(),
                preexec_fn=os.setsid,  # eigene Prozessgruppe
            )
            atexit.register(_kill_proc_group, cls._gazebo_proc)
        except FileNotFoundError:
            cls._startup_failed = True
            return

        # Hilfsnode für Topic-/Graph-Polling
        cls._graph: RclpyNode = rclpy.create_node("approach_goal_graph_probe")

        # 2) Auf /odom warten, damit die Sim wirklich läuft
        if not cls._wait_for_topic("/odom", timeout=45.0):
            cls._startup_failed = True
            return

        time.sleep(1.5)  # Controller settle

        # 3) approach_goal separat starten (eigene Prozessgruppe)
        try:
            cls._drive_proc = subprocess.Popen(
                [
                    "ros2", "run",
                    "my_diffdrive_kinematics", "approach_goal",
                    "--ros-args",
                    "-p", f"x:={robot_target_x}",
                    "-p", f"y:={robot_target_y}",
                    "-p", f"tol:={robot_tolerance}",
                    "-p", f"v_max:={robot_velocity_max}",
                    "-p", f"omega_max:={robot_omega_max}",
                ],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                env=os.environ.copy(),
                preexec_fn=os.setsid,
            )
            atexit.register(_kill_proc_group, cls._drive_proc)
        except FileNotFoundError:
            cls._startup_failed = True
            return

        # optional: auf /cmd_vel warten
        cls._wait_for_topic("/cmd_vel", timeout=15.0)

    @classmethod
    def tearDownClass(cls):
        # Graph-Node schließen
        try:
            cls._graph.destroy_node()
        except Exception:
            pass

        # Prozesse herunterfahren: SIGINT -> SIGTERM -> SIGKILL
        def _graceful_pg_terminate(proc: subprocess.Popen):
            if not proc:
                return
            try:
                pgid = os.getpgid(proc.pid)
            except Exception:
                return
            try:
                if proc.poll() is None:
                    os.killpg(pgid, signal.SIGINT)
                    for _ in range(10):
                        if proc.poll() is not None:
                            break
                        time.sleep(0.3)
                if proc.poll() is None:
                    os.killpg(pgid, signal.SIGTERM)
                    for _ in range(10):
                        if proc.poll() is not None:
                            break
                        time.sleep(0.3)
                if proc.poll() is None:
                    os.killpg(pgid, signal.SIGKILL)
            except Exception:
                pass

        # erst den Fahr-Node, dann Gazebo
        try:
            _graceful_pg_terminate(getattr(cls, "_drive_proc", None))
        except Exception:
            pass
        try:
            _graceful_pg_terminate(getattr(cls, "_gazebo_proc", None))
        except Exception:
            pass

        # ROS beenden
        try:
            rclpy.shutdown()
        except Exception:
            pass

    @classmethod
    def _wait_for_topic(cls, topic_name: str, timeout: float) -> bool:
        end = time.time() + timeout
        while time.time() < end:
            # Abbruch wenn der Launch-Prozess beendet wurde
            if getattr(cls, "_gazebo_proc", None) and cls._gazebo_proc.poll() is not None:
                return False
            try:
                rclpy.spin_once(cls._graph, timeout_sec=0.2)
                topics = [t[0] for t in cls._graph.get_topic_names_and_types()]
                if topic_name in topics:
                    return True
            except Exception:
                pass
            time.sleep(0.2)
        return False

    def setUp(self):
        if getattr(self, "_startup_failed", False):
            self.fail(
                "Gazebo/Robot oder approach_goal konnte nicht gestartet werden.")
        self.node: RclpyNode = rclpy.create_node('test_approach_goal')

    def tearDown(self):
        self.node.destroy_node()

    def test_approach_goal(self):
        twist_msgs_rec_buffer: list[TwistStamped] = []
        odom_msgs_rec_buffer: list[Odometry] = []

        twist_subscriber = self.node.create_subscription(
            TwistStamped, 'cmd_vel', lambda msg: twist_msgs_rec_buffer.append(
                msg), 20
        )
        odom_subscriber = self.node.create_subscription(
            Odometry, 'odom', lambda msg: odom_msgs_rec_buffer.append(msg), 20
        )

        try:
            robot_move_start = False
            robot_move_stop = False
            robot_move_stop_position = None
            robot_move_stop_twist_msg = None

            end_time = time.time() + 120.0
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=0.5)

                if not robot_move_start:
                    if odom_msgs_rec_buffer and twist_msgs_rec_buffer:
                        # Losfahren erkennen
                        if twist_msgs_rec_buffer[-1].twist.linear.x > 0.0:
                            robot_move_start = True
                    continue

                latest_twist_msg = twist_msgs_rec_buffer[-1]

                # Stopp erkannt: linear=0 && angular z=0
                if latest_twist_msg.twist.linear.x == 0.0 and latest_twist_msg.twist.angular.z == 0.0:
                    robot_move_stop_position = odom_msgs_rec_buffer[-1].pose.pose.position
                    robot_move_stop_twist_msg = latest_twist_msg
                    robot_move_stop = True
                    break

                # Sicherheits-Checks während der Fahrt
                self.assertLessEqual(
                    latest_twist_msg.twist.linear.x, robot_velocity_max,
                    "Robot's target linear x-velocity exceeds the given maximum velocity"
                )
                self.assertEqual(latest_twist_msg.twist.linear.y, 0.0,
                                 "Robot's target linear y-velocity does not match expected velocity")
                self.assertEqual(latest_twist_msg.twist.linear.z, 0.0,
                                 "Robot's target linear z-velocity does not match expected velocity")

                self.assertEqual(latest_twist_msg.twist.angular.x, 0.0,
                                 "Robot's target angular x-velocity does not match expected velocity")
                self.assertEqual(latest_twist_msg.twist.angular.y, 0.0,
                                 "Robot's target angular y-velocity does not match expected velocity")
                self.assertLessEqual(
                    latest_twist_msg.twist.angular.z, robot_omega_max,
                    "Robot's target angular z-velocity exceeds the given maximum velocity"
                )

            # Verhalten prüfen
            self.assertTrue(
                robot_move_start,
                "Did not receive twist messages to start robot movement. "
                "Please make sure that twist messages are published to the topic 'cmd_vel' continuously."
            )

            # Test for stop and stop message
            self.assertTrue(
                robot_move_stop, "Did not receive any twist message to stop the robot")
            self.assertIsNotNone(robot_move_stop_twist_msg,
                                 "Missing stop Twist message")

            self.assertEqual(robot_move_stop_twist_msg.twist.linear.x, 0.0,
                             "Robot's stop linear x-velocity does not match expected velocity")
            self.assertEqual(robot_move_stop_twist_msg.twist.linear.y, 0.0,
                             "Robot's stop linear y-velocity does not match expected velocity")
            self.assertEqual(robot_move_stop_twist_msg.twist.linear.z, 0.0,
                             "Robot's stop linear z-velocity does not match expected velocity")

            self.assertEqual(robot_move_stop_twist_msg.twist.angular.x, 0.0,
                             "Robot's stop angular x-velocity does not match expected velocity")
            self.assertEqual(robot_move_stop_twist_msg.twist.angular.y, 0.0,
                             "Robot's stop angular y-velocity does not match expected velocity")
            self.assertEqual(robot_move_stop_twist_msg.twist.angular.z, 0.0,
                             "Robot's stop angular z-velocity does not match expected velocity")

            # Zieltoleranz prüfen
            tol_min_x = robot_target_x - robot_tolerance
            tol_max_x = robot_target_x + robot_tolerance
            tol_min_y = robot_target_y - robot_tolerance
            tol_max_y = robot_target_y + robot_tolerance

            self.assertTrue(
                tol_min_x <= robot_move_stop_position.x <= tol_max_x,
                f"Robot did not stop in expected x tolerance: {tol_min_x} .. {tol_max_x}"
            )
            self.assertTrue(
                tol_min_y <= robot_move_stop_position.y <= tol_max_y,
                f"Robot did not stop in expected y tolerance: {tol_min_y} .. {tol_max_y}"
            )

        finally:
            self.node.destroy_subscription(twist_subscriber)
            self.node.destroy_subscription(odom_subscriber)
