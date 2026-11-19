# default imports
import time
import pytest
import unittest

# required launch imports
from launch import LaunchDescription
from launch_ros.actions import Node
from launch_testing.actions import ReadyToTest

# ROS imports
import rclpy
from rclpy.task import Future
from rcl_interfaces.msg import Log
from geometry_msgs.msg import PoseStamped
from tf_transformations import euler_from_quaternion, quaternion_from_euler
from trajectory_msgs.msg import JointTrajectory  # <-- neu

# additional imports
import numpy as np
import logging


def test_message_received(self, msgs_buffer):
    # Test, ob mindestens eine JointTrajectory-Nachricht empfangen wurde
    self.assertGreaterEqual(
        len(msgs_buffer), 1,
        "\033[0;31m No message received. Please make sure that:"
        "\n    * the ros2 workspace is sourced"
        "\n    * jk_rollin_arm_ik node subscribes a 'PoseStamped' message on topic '/tool_pose'"
        "\n    * jk_rollin_arm_ik node publishes a 'JointTrajectory' message to topic '/joint_states'.\033[0m"
    )


def test_joint_names(self, msgs_buffer):
    self.assertGreaterEqual(
        len(msgs_buffer), 1,
        "\033[0;31m No JointTrajectory message received. \033[0m"
    )

    traj_msg: JointTrajectory = msgs_buffer[0]

    expected_joint_names = ['turntable', 'shoulder', 'elbow', 'wrist']

    self.assertEqual(
        list(traj_msg.joint_names),
        expected_joint_names,
        "\033[0;31m JointTrajectory.joint_names did not match the expectation "
        "(expected ['turntable', 'shoulder', 'elbow', 'wrist']) \033[0m"
    )


@pytest.mark.rostest
def generate_test_description():
    kinematic_node = Node(
        package='my_jk_rollin_kinematics',
        executable='jk_rollin_arm_ik'
    )
    return (
        LaunchDescription([
            kinematic_node,
            ReadyToTest(),
        ]),
        {
        }
    )


class TestJkRollinArmIkNode(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        # Initialize the ROS context for the test node
        rclpy.init()

    @classmethod
    def tearDownClass(cls):
        # Shutdown the ROS context
        rclpy.shutdown()

    def setUp(self):
        # Create a ROS node for tests
        self.node = rclpy.create_node('test_jk_rollin_arm_ik_node')
        self.positions_in_circle = 100

        # Expected values unverändert übernommen
        self.expected_alphas = np.array([0., 0.014, 0.028, 0.042, 0.056, 0.069, 0.082,
                                         0.095, 0.108, 0.12, 0.131, 0.142, 0.152, 0.162,
                                         0.171, 0.179, 0.187, 0.193, 0.199, 0.205, 0.209,
                                         0.213, 0.215, 0.217, 0.218, 0.219, 0.218, 0.217,
                                         0.214, 0.211, 0.207, 0.202, 0.197, 0.19, 0.183,
                                         0.175, 0.166, 0.157, 0.147, 0.137, 0.125, 0.114,
                                         0.101, 0.089, 0.076, 0.063, 0.049, 0.035, 0.021,
                                         0.007, -0.007, -0.021, -0.035, -0.049, -0.063, -0.076,
                                         -0.089, -0.101, -0.114, -0.125, -0.137, -0.147, -0.157,
                                         -0.166, -0.175, -0.183, -0.19, -0.197, -0.202, -0.207,
                                         -0.211, -0.214, -0.217, -0.218, -0.219, -0.218, -0.217,
                                         -0.215, -0.213, -0.209, -0.205, -0.199, -0.193, -0.187,
                                         -0.179, -0.171, -0.162, -0.152, -0.142, -0.131, -0.12,
                                         -0.108, -0.095, -0.082, -0.069, -0.056, -0.042, -0.028,
                                         -0.014, -0.])

        self.expected_beta_1s = np.array([-0.651, -0.65, -0.649, -0.646, -0.643, -0.639, -0.634,
                                          -0.629, -0.624, -0.619, -0.614, -0.609, -0.604, -0.6,
                                          -0.596, -0.593, -0.59, -0.587, -0.586, -0.584, -0.584,
                                          -0.584, -0.584, -0.585, -0.586, -0.588, -0.591, -0.594,
                                          -0.597, -0.6, -0.604, -0.608, -0.613, -0.617, -0.622,
                                          -0.627, -0.632, -0.636, -0.641, -0.646, -0.65, -0.654,
                                          -0.658, -0.661, -0.664, -0.666, -0.668, -0.67, -0.671,
                                          -0.672, -0.672, -0.671, -0.67, -0.668, -0.666, -0.664,
                                          -0.661, -0.658, -0.654, -0.65, -0.646, -0.641, -0.636,
                                          -0.632, -0.627, -0.622, -0.617, -0.613, -0.608, -0.604,
                                          -0.6, -0.597, -0.594, -0.591, -0.588, -0.586, -0.585,
                                          -0.584, -0.584, -0.584, -0.584, -0.586, -0.587, -0.59,
                                          -0.593, -0.596, -0.6, -0.604, -0.609, -0.614, -0.619,
                                          -0.624, -0.629, -0.634, -0.639, -0.643, -0.646, -0.649,
                                          -0.65, -0.651])

        self.expected_beta_2s = np.array([-0.438, -0.44, -0.446, -0.456, -0.47, -0.487, -0.507,
                                          -0.529, -0.553, -0.579, -0.607, -0.635, -0.665, -0.695,
                                          -0.725, -0.756, -0.787, -0.818, -0.849, -0.88, -0.911,
                                          -0.941, -0.97, -0.999, -1.028, -1.056, -1.083, -1.109,
                                          -1.135, -1.159, -1.183, -1.206, -1.227, -1.248, -1.268,
                                          -1.286, -1.304, -1.32, -1.335, -1.349, -1.362, -1.373,
                                          -1.384, -1.393, -1.4, -1.407, -1.412, -1.416, -1.418,
                                          -1.42, -1.42, -1.418, -1.416, -1.412, -1.407, -1.4,
                                          -1.393, -1.384, -1.373, -1.362, -1.349, -1.335, -1.32,
                                          -1.304, -1.286, -1.268, -1.248, -1.227, -1.206, -1.183,
                                          -1.159, -1.135, -1.109, -1.083, -1.056, -1.028, -0.999,
                                          -0.97, -0.941, -0.911, -0.88, -0.849, -0.818, -0.787,
                                          -0.756, -0.725, -0.695, -0.665, -0.635, -0.607, -0.579,
                                          -0.553, -0.529, -0.507, -0.487, -0.47, -0.456, -0.446,
                                          -0.44, -0.438])

        self.expected_beta_3s = np.array([-0.482, -0.48, -0.476, -0.468, -0.458, -0.445, -0.43,
                                          -0.413, -0.394, -0.373, -0.351, -0.327, -0.302, -0.276,
                                          -0.25, -0.222, -0.194, -0.165, -0.136, -0.106, -0.077,
                                          -0.047, -0.017, 0.014, 0.043, 0.073, 0.103, 0.132,
                                          0.161, 0.189, 0.216, 0.243, 0.27, 0.295, 0.319,
                                          0.343, 0.365, 0.386, 0.406, 0.424, 0.441, 0.456,
                                          0.47, 0.483, 0.493, 0.502, 0.51, 0.515, 0.519,
                                          0.521, 0.521, 0.519, 0.515, 0.51, 0.502, 0.493,
                                          0.483, 0.47, 0.456, 0.441, 0.424, 0.406, 0.386,
                                          0.365, 0.343, 0.319, 0.295, 0.27, 0.243, 0.216,
                                          0.189, 0.161, 0.132, 0.103, 0.073, 0.043, 0.014,
                                          -0.017, -0.047, -0.077, -0.106, -0.136, -0.165, -0.194,
                                          -0.222, -0.25, -0.276, -0.302, -0.327, -0.351, -0.373,
                                          -0.394, -0.413, -0.43, -0.445, -0.458, -0.468, -0.476,
                                          -0.48, -0.482])

    def tearDown(self):
        self.node.destroy_node()

    def test_exception_for_unreachable_pose(self, launch_service, proc_output):
        log_msgs_buffer = []
        sub_log = self.node.create_subscription(
            Log,
            'rosout',
            lambda msg: log_msgs_buffer.append(msg),
            100
        )

        pub_points = self.node.create_publisher(
            PoseStamped,
            'tool_pose',
            1
        )

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()

        msg.pose.position.x = 0.227
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.068

        q = quaternion_from_euler(0, 0, 0, axes='rxyz')

        msg.pose.orientation.x = q[0]
        msg.pose.orientation.y = q[1]
        msg.pose.orientation.z = q[2]
        msg.pose.orientation.w = q[3]

        try:
            future = Future()
            rclpy.spin_once(self.node, timeout_sec=2)
            pub_points.publish(msg)
            rclpy.spin_until_future_complete(self.node, future, timeout_sec=2)

            warning_logged = False
            for log in log_msgs_buffer:
                if log.name == 'jk_rollin_arm_ik' and log.level == 30:
                    warning_logged = True

            self.assertTrue(
                warning_logged,
                "\033[0;31m No warnings logged for unreachable tool poses!\n"
                "\033[0;33m     * raise a ValueError Exception in our inverse kinematics for unreachable tool poses\n"
                "     * catch the Exception with:\n"
                "        try:\n"
                "            # do stuff and call inverse kinematics\n"
                "        except ValueError:\n"
                "           self.get_logger().warning( ... \n \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_log)

    def _get_first_point_positions(self, traj_msgs_buffer):
        """Hilfsfunktion: nimmt erste Trajektorien-Message und ersten Punkt."""
        self.assertGreaterEqual(
            len(traj_msgs_buffer), 1,
            "\033[0;31m No JointTrajectory message received. \033[0m"
        )
        traj: JointTrajectory = traj_msgs_buffer[0]
        self.assertGreaterEqual(
            len(traj.points), 1,
            "\033[0;31m JointTrajectory has no points. \033[0m"
        )
        positions = traj.points[0].positions
        self.assertGreaterEqual(
            len(positions), 4,
            "\033[0;31m JointTrajectoryPoint.positions has fewer than 4 entries. \033[0m"
        )
        return positions

    def test_inverse_kinematics_arm_forward(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(
            PoseStamped,
            'tool_pose',
            1
        )

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()

        msg.pose.position.x = 0.22599999999
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.068

        q = quaternion_from_euler(0, 0, 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            expected_joint_states = np.rad2deg(
                np.array([0.0, 0.0, 0.0, 0.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],  # turntable
                    positions[1],  # shoulder
                    positions[2],  # elbow
                    positions[3],  # wrist
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_forward_wrist_up(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.175
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.119

        q = quaternion_from_euler(0, np.deg2rad(90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            expected_joint_states = np.rad2deg(
                np.array([0.0, 0.0, 0.0, np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_forward_wrist_down(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.175
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.017

        q = quaternion_from_euler(0, np.deg2rad(-90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            expected_joint_states = np.rad2deg(
                np.array([0.0, 0.0, 0.0, -np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_upward_wrist_up(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.0
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.29399999999

        q = quaternion_from_euler(0, np.deg2rad(90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [turntable, shoulder, elbow, wrist] = [0, pi/2, 0, 0]
            expected_joint_states = np.rad2deg(
                np.array([0.0, np.pi / 2.0, 0.0, 0.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_upward_wrist_front(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.051
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.24299999999

        q = quaternion_from_euler(0, 0, 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [0, pi/2, 0, -pi/2]
            expected_joint_states = np.rad2deg(
                np.array([0.0, np.pi / 2.0, 0.0, -np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_forward_wrist_forward(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.133
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.161

        q = quaternion_from_euler(0, 0, 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [0, pi/2, -pi/2, 0]
            expected_joint_states = np.rad2deg(
                np.array([0.0, np.pi / 2.0, -np.pi / 2.0, 0.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_forward_wrist_up(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.082
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.212

        q = quaternion_from_euler(0, np.deg2rad(90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [0, pi/2, -pi/2, pi/2]
            expected_joint_states = np.rad2deg(
                np.array([0.0, np.pi / 2.0, -np.pi / 2.0, np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_forward_wrist_down(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.082
        msg.pose.position.y = 0.0
        msg.pose.position.z = 0.110

        q = quaternion_from_euler(0, np.deg2rad(-90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [0, pi/2, -pi/2, -pi/2]
            expected_joint_states = np.rad2deg(
                np.array([0.0, np.pi / 2.0, -np.pi / 2.0, -np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. "
                "Order is [turntable, shoulder, elbow, wrist] (deg). \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    # Die folgenden linken/rechten Varianten sind analog angepasst:
    # jeweils nur noch 4 Joints, Werte entsprechend aus dem ursprünglichen
    # 6-DoF-Array ohne die beiden Wheel-Joints übernommen.

    def test_inverse_kinematics_arm_bent_left_wrist_front(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.0
        msg.pose.position.y = 0.133
        msg.pose.position.z = 0.161

        q = quaternion_from_euler(0, 0, 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [pi/2, pi/2, -pi/2, 0]
            expected_joint_states = np.rad2deg(
                np.array([np.pi / 2.0, np.pi / 2.0, -np.pi / 2.0, 0.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_left_wrist_up(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.0
        msg.pose.position.y = 0.082
        msg.pose.position.z = 0.212

        q = quaternion_from_euler(0, np.deg2rad(90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [pi/2, pi/2, -pi/2, pi/2]
            expected_joint_states = np.rad2deg(
                np.array([np.pi / 2.0, np.pi / 2.0, -np.pi / 2.0, np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_left_wrist_down(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.0
        msg.pose.position.y = 0.082
        msg.pose.position.z = 0.110

        q = quaternion_from_euler(0, np.deg2rad(-90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [pi/2, pi/2, -np.pi/2, -np.pi/2]
            expected_joint_states = np.rad2deg(
                np.array([np.pi / 2.0, np.pi / 2.0, -np.pi / 2.0, -np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_right_wrist_front(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.0
        msg.pose.position.y = -0.133
        msg.pose.position.z = 0.161

        q = quaternion_from_euler(0, 0, 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [-pi/2, pi/2, -pi/2, 0]
            expected_joint_states = np.rad2deg(
                np.array([-np.pi / 2.0, np.pi / 2.0, -np.pi / 2.0, 0.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_right_wrist_up(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.0
        msg.pose.position.y = -0.082
        msg.pose.position.z = 0.212

        q = quaternion_from_euler(0, np.deg2rad(90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [-pi/2, pi/2, -pi/2, pi/2]
            expected_joint_states = np.rad2deg(
                np.array([-np.pi / 2.0, np.pi / 2.0, -np.pi / 2.0, np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)

    def test_inverse_kinematics_arm_bent_right_wrist_down(self, launch_service, proc_output):
        joint_msgs_rec_buffer: list[JointTrajectory] = []
        sub_joints = self.node.create_subscription(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            lambda msg: joint_msgs_rec_buffer.append(msg),
            1
        )

        pub_points = self.node.create_publisher(PoseStamped, 'tool_pose', 1)

        msg = PoseStamped()
        msg.header.stamp = self.node.get_clock().now().to_msg()
        msg.pose.position.x = 0.0
        msg.pose.position.y = -0.082
        msg.pose.position.z = 0.110

        q = quaternion_from_euler(0, np.deg2rad(-90), 0, axes='rxyz')
        msg.pose.orientation.x, msg.pose.orientation.y, msg.pose.orientation.z, msg.pose.orientation.w = q

        try:
            end_time = time.time() + 10
            while time.time() < end_time:
                rclpy.spin_once(self.node, timeout_sec=1)
                pub_points.publish(msg)
                rclpy.spin_once(self.node, timeout_sec=1)
                if len(joint_msgs_rec_buffer) >= 1:
                    break

            test_joint_names(self, joint_msgs_rec_buffer)

            # [-pi/2, pi/2, -pi/2, -pi/2]
            expected_joint_states = np.rad2deg(
                np.array([-np.pi / 2.0, np.pi / 2.0, -np.pi / 2.0, -np.pi / 2.0])
            ).round(2)

            positions = self._get_first_point_positions(joint_msgs_rec_buffer)
            result_joint_states = np.rad2deg(
                np.array([
                    positions[0],
                    positions[1],
                    positions[2],
                    positions[3],
                ])
            ).round(2)

            self.assertEqual(
                result_joint_states.tolist(), expected_joint_states.tolist(),
                "\033[0;31m JointTrajectoryPoint.positions did not match the expectation. \033[0m"
            )

        finally:
            self.node.destroy_subscription(sub_joints)
