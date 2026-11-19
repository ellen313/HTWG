# __author__ = "Michael Blaich, Florian Drescher, Christopher Gogl"

import os
import atexit
import signal
import time
import subprocess
import unittest
from math import atan2

import rclpy
from rclpy.node import Node as RclpyNode
from geometry_msgs.msg import TwistStamped, Quaternion
from nav_msgs.msg import Odometry
import numpy as np

# Test-Parameter
robot_velocity = 0.25
robot_radius = 1.5
robot_target_alpha = 1.2  # [rad]


# --- Quaternion-Hilfsfunktionen (unverändert zu deiner Vorlage) ---

def get_angle_between_quaternions(start: Quaternion, end: Quaternion):
    q_conj = quaternion_conjugate(start)
    q = quaternion_multiply(q_conj, end)
    angle = atan2(2 * (q.w * q.z + q.x * q.y), 1 - 2 * (q.z ** 2 + q.y ** 2))
    if angle < 0.0:
        return 2 * np.pi + angle
    else:
        return angle


def quaternion_conjugate(q: Quaternion):
    new_quaternion = Quaternion()
    new_quaternion.x = q.x
    new_quaternion.y = q.y
    new_quaternion.z = q.z
    new_quaternion.w = -q.w
    return new_quaternion


def quaternion_multiply(q1: Quaternion, q2: Quaternion):
    res = Quaternion()
    res.w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z
    res.x = q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y
    res.y = q1.w * q2.y - q1.x * q2.z + q1.y * q2.w + q1.z * q2.x
    res.z = q1.w * q2.z + q1.x * q2.y - q1.y * q2.x + q1.z * q2.w
    return res


# --- Failsafe für atexit ---

def _kill_proc_group(proc: subprocess.Popen):
    try:
        if proc and proc.poll() is None:
            os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
    except Exception:
        pass


class TestCurveDrive(unittest.TestCase):
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
                preexec_fn=os.setsid,  # neue Prozessgruppe
            )
            atexit.register(_kill_proc_group, cls._gazebo_proc)
        except FileNotFoundError:
            cls._startup_failed = True
            return

        # Hilfsnode für Graph/Topics
        cls._graph: RclpyNode = rclpy.create_node("curve_drive_graph_probe")

        # 2) Auf /odom warten, damit sim läuft
        if not cls._wait_for_topic("/odom", timeout=45.0):
            cls._startup_failed = True
            return

        time.sleep(1.5)  # Controller settle

        # 3) curve_drive separat starten (eigene Prozessgruppe)
        try:
            cls._drive_proc = subprocess.Popen(
                [
                    "ros2", "run",
                    "my_diffdrive_kinematics", "curve_drive",
                    "--ros-args",
                    "-p", f"v:={robot_velocity}",
                    "-p", f"r:={robot_radius}",
                    "-p", f"alpha:={robot_target_alpha}",
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

        # optional: warten bis /cmd_vel sichtbar
        cls._wait_for_topic("/cmd_vel", timeout=15.0)

    @classmethod
    def tearDownClass(cls):
        # Graph-Node schließen
        try:
            cls._graph.destroy_node()
        except Exception:
            pass

        # Prozesse herunterfahren (SIGINT->SIGTERM->SIGKILL)
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
            # Abbruch falls Launch beendet ist
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
                "Gazebo/Robot oder curve_drive konnte nicht gestartet werden.")
        self.node: RclpyNode = rclpy.create_node('test_curve_drive_node')

    def tearDown(self):
        self.node.destroy_node()

    # -------- Test --------
    def test_curve_drive(self):
        twist_msgs_rec_buffer: list[TwistStamped] = []
        odom_msgs_rec_buffer: list[Odometry] = []
        angle_velocity = robot_velocity / robot_radius

        twist_subscriber = self.node.create_subscription(
            TwistStamped, 'cmd_vel', lambda msg: twist_msgs_rec_buffer.append(
                msg), 20
        )
        odom_subscriber = self.node.create_subscription(
            Odometry, 'odom', lambda msg: odom_msgs_rec_buffer.append(msg), 20
        )

        try:
            robot_move_start = False
            robot_move_start_orientation = None
            robot_move_stop = False
            robot_move_stop_orientation = None
            robot_move_stop_twist_msg = None

            end_time = time.time() + 60.0
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=0.5)

                if not robot_move_start:
                    if odom_msgs_rec_buffer and twist_msgs_rec_buffer:
                        # warte bis curve_drive "losfährt"
                        if twist_msgs_rec_buffer[-1].twist.linear.x > 0.0:
                            robot_move_start_orientation = odom_msgs_rec_buffer[-1].pose.pose.orientation
                            robot_move_start = True
                    continue

                latest_twist_msg = twist_msgs_rec_buffer[-1]

                # Stopp erkannt?
                if latest_twist_msg.twist.linear.x == 0.0:
                    robot_move_stop_orientation = odom_msgs_rec_buffer[-1].pose.pose.orientation
                    robot_move_stop_twist_msg = latest_twist_msg
                    robot_move_stop = True
                    break

                # Twist-Inhalte während Fahrt prüfen
                self.assertEqual(
                    latest_twist_msg.twist.linear.x, robot_velocity,
                    "\033[0;31m Robot's target linear x-velocity does not match expected velocity \033[0m"
                )
                self.assertEqual(latest_twist_msg.twist.linear.y, 0.0,
                                 "\033[0;31m Robot's target linear y-velocity does not match expected velocity \033[0m")
                self.assertEqual(latest_twist_msg.twist.linear.z, 0.0,
                                 "\033[0;31m Robot's target linear z-velocity does not match expected velocity \033[0m")

                self.assertEqual(latest_twist_msg.twist.angular.x, 0.0,
                                 "\033[0;31m Robot's target angular x-velocity does not match expected velocity \033[0m")
                self.assertEqual(latest_twist_msg.twist.angular.y, 0.0,
                                 "\033[0;31m Robot's target angular y-velocity does not match expected velocity \033[0m")
                self.assertAlmostEqual(
                    latest_twist_msg.twist.angular.z, angle_velocity, 3,
                    "\033[0;31m Robot's target angular z-velocity does not match expected velocity \033[0m"
                )

            # Verhalten prüfen
            self.assertTrue(
                robot_move_start,
                "\033[0;31m Did not receive twist messages to start robot movement. "
                "Please make sure that twist messages are published to the topic 'cmd_vel' continuously. \033[0m"
            )

            self.assertTrue(
                robot_move_stop, "\033[0;31m Did not receive any twist message to stop the robot \033[0m"
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

            # Drehwinkel prüfen (Start->Stop)
            driven_angle = get_angle_between_quaternions(
                robot_move_start_orientation, robot_move_stop_orientation
            )
            tol_min = robot_target_alpha - 0.1
            tol_max = robot_target_alpha + 0.1
            self.assertTrue(
                tol_min <= driven_angle <= tol_max,
                f"\033[0;31m Robot did not rotate between {tol_min} and {tol_max} rad. \033[0m"
            )

            # Test if curve_drive exited
            # self.assertNotIn("curve_drive", self.node.get_node_names(),
            #            "Node 'curve_drive' did not exit after sending stop command to robot")

        finally:
            self.node.destroy_subscription(twist_subscriber)
            self.node.destroy_subscription(odom_subscriber)
