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
import numpy as np

# Test-Parameter
robot_velocity = 0.25
robot_target_distance = 1.0


def _kill_proc_group(proc: subprocess.Popen):
    """Failsafe: gesamte Prozessgruppe gnadenlos beenden (für atexit)."""
    try:
        if proc and proc.poll() is None:
            os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
    except Exception:
        pass


class TestStraightDriveNode(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        # ROS initialisieren
        rclpy.init()
        cls._startup_failed = False

        # 1) Gazebo/Robot per ros2 launch starten (eigene Prozessgruppe)
        try:
            cls._gazebo_proc = subprocess.Popen(
                ["ros2", "launch", "jk_rollin_gazebo",
                    "jk_rollin.gazebo.launch.py"],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                env=os.environ.copy(),
                preexec_fn=os.setsid,  # eigene PGID
            )
            atexit.register(_kill_proc_group, cls._gazebo_proc)
        except FileNotFoundError:
            cls._startup_failed = True
            return

        # Hilfsnode für Graph-Polling
        cls._graph: RclpyNode = rclpy.create_node("straight_drive_graph_probe")

        # 2) Warten bis Gazebo/Robot „da“ sind (z.B. /odom erscheint)
        if not cls._wait_for_topic("/odom", timeout=45.0):
            cls._startup_failed = True
            return

        # kleine zusätzliche Wartezeit, bis Controller/Twist ready ist
        time.sleep(1.5)

        # 3) straight_drive separat starten (eigene Prozessgruppe)
        try:
            cls._drive_proc = subprocess.Popen(
                [
                    "ros2", "run",
                    "my_diffdrive_kinematics", "straight_drive",
                    "--ros-args", "-p", f"v:={robot_velocity}", "-p", f"distance:={robot_target_distance}",
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

        # Optional: warten, bis /cmd_vel sichtbar ist
        cls._wait_for_topic("/cmd_vel", timeout=15.0)

    @classmethod
    def tearDownClass(cls):
        # Graph-Node schließen
        try:
            cls._graph.destroy_node()
        except Exception:
            pass

        # Prozesse sanft herunterfahren: SIGINT -> SIGTERM -> SIGKILL
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

        # Reihenfolge: drive -> gazebo
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
        """Warte darauf, dass ein Topic im Graph auftaucht (best-effort)."""
        end = time.time() + timeout
        while time.time() < end:
            if getattr(cls, "_gazebo_proc", None) and cls._gazebo_proc.poll() is not None:
                # Launch-Prozess bereits beendet -> fehlgeschlagen
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
                "Gazebo/Robot oder straight_drive konnte nicht gestartet werden.")
        self.node: RclpyNode = rclpy.create_node("test_straight_drive")

    def tearDown(self):
        self.node.destroy_node()

    # ---------- Test ----------
    def test_straight_drive(self):
        twist_msgs_rec_buffer: list[TwistStamped] = []
        odom_msgs_rec_buffer: list[Odometry] = []

        twist_sub = self.node.create_subscription(
            TwistStamped, "cmd_vel", lambda msg: twist_msgs_rec_buffer.append(
                msg), 20
        )
        odom_sub = self.node.create_subscription(
            Odometry, "odom", lambda msg: odom_msgs_rec_buffer.append(msg), 20
        )

        try:
            robot_move_start = False
            robot_move_start_position = None
            robot_move_stop = False
            robot_move_stop_position = None
            robot_move_stop_twist_msg = None

            end_time = time.time() + 60.0
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=0.5)

                # Warten bis erste Odom- und Twist-Daten da sind
                if not robot_move_start:
                    if odom_msgs_rec_buffer and twist_msgs_rec_buffer:
                        if twist_msgs_rec_buffer[-1].twist.linear.x > 0:
                            robot_move_start_position = odom_msgs_rec_buffer[-1].pose.pose.position
                            robot_move_start = True
                    continue

                latest_twist = twist_msgs_rec_buffer[-1]

                # Stop erkannt?
                if latest_twist.twist.linear.x == 0.0:
                    robot_move_stop_position = odom_msgs_rec_buffer[-1].pose.pose.position
                    robot_move_stop_twist_msg = latest_twist
                    robot_move_stop = True
                    break

                # Während der Fahrt: Twist-Inhalte prüfen
                self.assertEqual(
                    latest_twist.twist.linear.x, robot_velocity,
                    "\033[0;31m Robot's target linear x-velocity does not match expected velocity \033[0m"
                )
                self.assertEqual(latest_twist.twist.linear.y, 0.0,
                                 "\033[0;31m Robot's target linear y-velocity does not match expected velocity \033[0m")
                self.assertEqual(latest_twist.twist.linear.z, 0.0,
                                 "\033[0;31m Robot's target linear z-velocity does not match expected velocity \033[0m")

                self.assertEqual(latest_twist.twist.angular.x, 0.0,
                                 "\033[0;31m Robot's target angular x-velocity does not match expected velocity \033[0m")
                self.assertEqual(latest_twist.twist.angular.y, 0.0,
                                 "\033[0;31m Robot's target angular y-velocity does not match expected velocity \033[0m")
                self.assertEqual(latest_twist.twist.angular.z, 0.0,
                                 "\033[0;31m Robot's target angular z-velocity does not match expected velocity \033[0m")

            # Verhalten prüfen
            self.assertTrue(
                robot_move_start,
                "\033[0;31m Did not receive twist messages to start robot movement. "
                "Please make sure that twist messages are published to 'cmd_vel' continuously. \033[0m"
            )

            self.assertTrue(
                robot_move_stop,
                "\033[0;31m Did not receive any twist message to stop the robot \033[0m"
            )

            self.assertIsNotNone(robot_move_stop_twist_msg,
                                 "\033[0;31m Missing stop Twist message \033[0m")
            self.assertEqual(robot_move_stop_twist_msg.twist.linear.x, 0.0,
                             "\033[0;31m Robot's stop linear x-velocity does not match expected velocity \033[0m")
            self.assertEqual(robot_move_stop_twist_msg.twist.linear.y, 0.0,
                             "\033[0;31m Robot's stop linear y-velocity does not match expected velocity \033[0m")
            self.assertEqual(robot_move_stop_twist_msg.twist.linear.z, 0.0,
                             "\033[0;31m Robot's stop linear z-velocity does not match expected velocity \033[0m")
            self.assertEqual(robot_move_stop_twist_msg.twist.angular.x, 0.0,
                             "\033[0;31m Robot's stop angular x-velocity does not match expected velocity \033[0m")
            self.assertEqual(robot_move_stop_twist_msg.twist.angular.y, 0.0,
                             "\033[0;31m Robot's stop angular y-velocity does not match expected velocity \033[0m")
            self.assertEqual(robot_move_stop_twist_msg.twist.angular.z, 0.0,
                             "\033[0;31m Robot's stop angular z-velocity does not match expected velocity \033[0m")

            # zurückgelegte Strecke prüfen
            stop_distance = np.sqrt(
                (robot_move_stop_position.x - robot_move_start_position.x) ** 2
                + (robot_move_stop_position.y - robot_move_start_position.y) ** 2
                + (robot_move_stop_position.z - robot_move_start_position.z) ** 2
            )
            tol_min = robot_target_distance - 0.1
            tol_max = robot_target_distance + 0.1
            self.assertTrue(
                tol_min <= stop_distance <= tol_max,
                f"\033[0;31m Robot did not stop between {tol_min} and {tol_max} m. \033[0m",
            )

        finally:
            self.node.destroy_subscription(twist_sub)
            self.node.destroy_subscription(odom_sub)
