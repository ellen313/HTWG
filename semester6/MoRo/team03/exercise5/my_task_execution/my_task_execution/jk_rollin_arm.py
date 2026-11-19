import math
import rclpy
from rclpy.node import Node
from geometry_msgs.msg import PoseStamped
from trajectory_msgs.msg import JointTrajectory, JointTrajectoryPoint
from builtin_interfaces.msg import Duration
from tf_transformations import euler_from_quaternion


class JKRollinArmIK(Node):
    def __init__(self):
        super().__init__('jk_rollin_arm_ik')

        # Subscribe to the tool pose (x,y,z) requested by the main node
        self.subscription = self.create_subscription(
            PoseStamped, '/tool_pose', self.ik_callback, 10)

        # Publish joint angles to the arm controller
        self.publisher_ = self.create_publisher(
            JointTrajectory, '/arm_controller/joint_trajectory', 10)

        # Robot Geometry Parameters (from Exercise 3)
        self.r = 0.0      # Offset
        self.h = 0.068    # Base height (h + b)
        self.l1 = 0.093   # Shoulder length
        self.l2 = 0.082   # Forearm length
        self.l3 = 0.051   # Gripper length

    def ik_callback(self, msg):
        x = msg.pose.position.x
        y = msg.pose.position.y
        z = msg.pose.position.z

        # Orientation
        orientation_q = msg.pose.orientation
        (roll, pitch, yaw) = euler_from_quaternion(
            [orientation_q.x, orientation_q.y, orientation_q.z, orientation_q.w])

        # 1. Turntable Angle (alpha)
        alpha = math.atan2(y, x)

        # 2. Projection to 2D plane
        r_ground = math.sqrt(x ** 2 + y ** 2)
        r_eff = r_ground - self.r
        z_eff = z - self.h

        # 3. Wrist Position (remove gripper length)
        # Note: pitch=0 means horizontal gripper
        r_wrist = r_eff - self.l3 * math.cos(pitch)
        z_wrist = z_eff - self.l3 * math.sin(pitch)

        d = math.sqrt(r_wrist ** 2 + z_wrist ** 2)
        max_reach = self.l1 + self.l2

        if d > (max_reach + 0.001):
            self.get_logger().warning(f'Target unreachable! dist={d:.3f}, max={max_reach:.3f}')
            return

        try:
            # Law of Cosines for Elbow
            cos_beta2 = (r_wrist ** 2 + z_wrist ** 2 - self.l1 ** 2 - self.l2 ** 2) / (2 * self.l1 * self.l2)
            cos_beta2 = max(-1.0, min(1.0, cos_beta2)) # Clamp for math safety

            # Elbow-Up Configuration (Negative angle)
            beta2 = -math.acos(cos_beta2)

            k1 = self.l1 + self.l2 * math.cos(beta2)
            k2 = self.l2 * math.sin(beta2)
            beta1 = math.atan2(z_wrist, r_wrist) - math.atan2(k2, k1)

            beta3 = pitch - (beta1 + beta2)

            # Publish Trajectory
            traj_msg = JointTrajectory()
            traj_msg.joint_names = ['turntable', 'shoulder', 'elbow', 'wrist']

            point = JointTrajectoryPoint()
            point.positions = [float(alpha), float(beta1), float(beta2), float(beta3)]
            point.time_from_start = Duration(sec=0, nanosec=500000000) # Move in 0.5s

            traj_msg.points.append(point)
            self.publisher_.publish(traj_msg)

        except ValueError as e:
            self.get_logger().warning(f'Math Error: {e}')


def main(args=None):
    rclpy.init(args=args)
    node = JKRollinArmIK()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()


if __name__ == '__main__':
    main()