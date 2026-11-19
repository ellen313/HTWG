# __author__ = "Michael Blaich, Florian Drescher, Christopher Gogl"

import os
import atexit
import signal
import time
import subprocess
import unittest

import rclpy
from rclpy.node import Node as RclpyNode
from nav_msgs.msg import Odometry, Path
from geometry_msgs.msg import PoseStamped, Pose, TwistStamped

import numpy as np
from ament_index_python import get_package_share_directory


def distance(current_pose: Pose, target_pose: PoseStamped):
    dx = current_pose.position.x - target_pose.pose.position.x
    dy = current_pose.position.y - target_pose.pose.position.y
    return np.sqrt(dx ** 2 + dy ** 2)


def _kill_proc_group(proc: subprocess.Popen):
    """Failsafe: gesamte Prozessgruppe killen, falls noch aktiv (für atexit)."""
    try:
        if proc and proc.poll() is None:
            os.killpg(os.getpgid(proc.pid), signal.SIGKILL)
    except Exception:
        pass


class TestPathFollowerNode(unittest.TestCase):
    def __init__(self, methodName: str = "runTest") -> None:
        super().__init__(methodName)
        self.path_msg_rec = None

    @classmethod
    def setUpClass(cls):
        rclpy.init()
        cls._startup_failed = False

        # --- 0) Pfade zu Parameterdateien auflösen ---
        try:
            follow_config = os.path.join(
                get_package_share_directory('my_diffdrive_kinematics'),
                'config', 'follower_config.yaml'
            )
            path_config = os.path.join(
                get_package_share_directory('exercise4_tests'),
                'config', 'default_path.yaml'
            )
            if not os.path.isfile(follow_config) or not os.path.isfile(path_config):
                raise FileNotFoundError("Parameterdatei(en) nicht gefunden.")
        except Exception:
            cls._startup_failed = True
            return

        # --- 1) Gazebo/Robot starten (eigene Prozessgruppe) ---
        try:
            cls._gazebo_proc = subprocess.Popen(
                ["ros2", "launch", "jk_rollin_gazebo", "jk_rollin.gazebo.launch.py",
                    "use_rviz:=false", "headless:=False"],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                env=os.environ.copy(),
                preexec_fn=os.setsid,
            )
            atexit.register(_kill_proc_group, cls._gazebo_proc)
        except FileNotFoundError:
            cls._startup_failed = True
            return

        # Hilfsnode für Topic-/Graph-Polling
        cls._graph: RclpyNode = rclpy.create_node("path_follower_graph_probe")

        # --- 2) Auf /odom warten, damit die Sim wirklich läuft ---
        if not cls._wait_for_topic("/odom", timeout=45.0):
            cls._startup_failed = True
            return
        time.sleep(1.5)  # Controller settle

        # --- 3) generate_path starten (eigene Prozessgruppe) ---
        try:
            cls._gen_path_proc = subprocess.Popen(
                [
                    "ros2", "launch",
                    "my_diffdrive_kinematics", "generate_path.launch.py", "launch_rviz:=true",
                ],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                env=os.environ.copy(),
                preexec_fn=os.setsid,
            )
            atexit.register(_kill_proc_group, cls._gen_path_proc)
        except FileNotFoundError:
            cls._startup_failed = True
            return

        # auf /path warten (optional, aber hilft Stabilität)
        cls._wait_for_topic("/path", timeout=15.0)

        # --- 4) path_follower starten (eigene Prozessgruppe) ---
        try:
            cls._follower_proc = subprocess.Popen(
                [
                    "ros2", "run",
                    "my_diffdrive_kinematics", "path_follower",
                    "--ros-args", "--params-file", follow_config,
                ],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                env=os.environ.copy(),
                preexec_fn=os.setsid,
            )
            atexit.register(_kill_proc_group, cls._follower_proc)
        except FileNotFoundError:
            cls._startup_failed = True
            return

        # optional: auf /cmd_vel warten
        cls._wait_for_topic("/cmd_vel", timeout=15.0)

    @classmethod
    def tearDownClass(cls):
        # Hilfsnode schließen
        try:
            cls._graph.destroy_node()
        except Exception:
            pass

        # Prozesse sanft beenden: SIGINT -> SIGTERM -> SIGKILL
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

        # Reihenfolge: follower -> generate_path -> gazebo
        try:
            _graceful_pg_terminate(getattr(cls, "_follower_proc", None))
        except Exception:
            pass
        try:
            _graceful_pg_terminate(getattr(cls, "_gen_path_proc", None))
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
            # Abbruch falls Launch bereits beendet
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
            self.fail("Gazebo/Robot oder Knoten konnten nicht gestartet werden.")
        self.node: RclpyNode = rclpy.create_node('test_path_follower')
        self.dist_to_path_threshold = 0.3
        self.path_following_success_threshold = 0.7
        self.d_star = 0.5
        self.path_msg_rec = None  # sicherheitshalber

    def tearDown(self):
        self.node.destroy_node()

    # einmalige Path-Message merken
    def path_callback(self, msg: Path):
        if self.path_msg_rec is None:
            self.path_msg_rec = msg
            self.node.destroy_subscription(self.path_subscriber)

    def test_path_follower(self):
        odom_msgs_rec_buffer: list[Odometry] = []
        twist_msgs_rec_buffer: list[TwistStamped] = []
        stop_flag = False

        odom_subscriber = self.node.create_subscription(
            Odometry, 'odom', lambda msg: odom_msgs_rec_buffer.append(msg), 50
        )
        self.path_subscriber = self.node.create_subscription(
            Path, 'path', self.path_callback, 10
        )
        cmd_vel_subscriber = self.node.create_subscription(
            TwistStamped, 'cmd_vel', lambda msg: twist_msgs_rec_buffer.append(
                msg), 50
        )

        try:
            end_time = time.time() + 120.0
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=0.5)

                if twist_msgs_rec_buffer:
                    latest_twist_msg = twist_msgs_rec_buffer[-1]
                    if latest_twist_msg.twist.linear.x == 0.0 and latest_twist_msg.twist.angular.z == 0.0:
                        stop_flag = True
                        break

            # 1) Stop-Kommando angekommen?
            self.assertTrue(
                stop_flag,
                "Did not receive a stop command within 120s. Make sure the path follower sends a stop message "
                "after reaching the path's end."
            )

            # 2) Odoms vorhanden?
            self.assertGreater(
                len(odom_msgs_rec_buffer), 0,
                "Did not receive any odom messages. Please make sure that odom messages are published to 'odom' continuously."
            )

            # 3) Pfad vorhanden?
            self.assertIsNotNone(
                self.path_msg_rec,
                "Did not receive any path messages. Please make sure that path messages are published to 'path' continuously."
            )

            # 4) Pfadtreue prüfen (Anteil der Pfadpunkte, die nahe an einer Odom-Position liegen)
            matched_path_points = 0
            for pose in self.path_msg_rec.poses:
                path_position = pose.pose.position
                for odom in odom_msgs_rec_buffer:
                    odom_position = odom.pose.pose.position
                    d = np.sqrt(
                        (path_position.x - odom_position.x) ** 2
                        + (path_position.y - odom_position.y) ** 2
                    )
                    if d < self.dist_to_path_threshold:
                        matched_path_points += 1
                        break

            path_share_in_tolerance = float(
                matched_path_points) / max(1, len(self.path_msg_rec.poses))
            self.assertGreaterEqual(
                path_share_in_tolerance, self.path_following_success_threshold,
                "Robot did not follow path accurately enough. "
                f"Only {path_share_in_tolerance*100:.1f}% were reached within {self.dist_to_path_threshold:.2f} m. "
                f"Requirement: {self.path_following_success_threshold*100:.0f}%."
            )

            # 5) Abstand zum Ziel prüfen
            dist_target = distance(
                odom_msgs_rec_buffer[-1].pose.pose, self.path_msg_rec.poses[-1])
            dist_limit = self.d_star + 0.1
            self.assertLessEqual(
                dist_target, dist_limit,
                f"Robot stopped too far away from goal position: {dist_target:.3f}. "
                f"Max allowed distance to path's end is: {dist_limit:.3f}."
            )

        finally:
            self.node.destroy_subscription(odom_subscriber)
            # path_subscriber kann schon zerstört sein
            try:
                self.node.destroy_subscription(self.path_subscriber)
            except Exception:
                pass
            self.node.destroy_subscription(cmd_vel_subscriber)
