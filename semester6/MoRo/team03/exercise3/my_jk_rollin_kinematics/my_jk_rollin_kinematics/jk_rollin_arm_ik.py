import numpy as np
import rclpy
from rclpy.node import Node
from geometry_msgs.msg import PoseStamped
from trajectory_msgs.msg import JointTrajectory, JointTrajectoryPoint
from tf_transformations import euler_from_quaternion

class JKRollinArmIk(Node):
    def __init__(self):
        super().__init__('jk_rollin_arm_ik')

        #subscription (like listener.py)
        self.subscription = self.create_subscription(
            PoseStamped,
            '/tool_pose',
            self.pose_callback,
            10)

        #publisher (like talk.py)
        self.publisher_ = self.create_publisher(
            JointTrajectory,
            '/arm_controller/joint_trajectory',
            10)

        self.arm_dimensions = {
            'l1': 0.093,
            'l2': 0.082,
            'l3': 0.051,
            'h': 0.021,
            'b': 0.047
        }

        self.get_logger().info("Ik Node wurde gestartet.")

    def pose_callback(self, msg: PoseStamped):

        try:
            msg.header.stamp = self.get_clock().now().to_msg() #time-stamp

            position = msg.pose.position
            orientation = msg.pose.orientation

            alpha, beta_1, beta_2, beta_3 = self.inverse_kinematics(position, orientation)

            trajectory = JointTrajectory()
            trajectory.joint_names = [
                'turntable', #alpha
                'shoulder', #beta1
                'elbow', #beta2
                'wrist' #beta3
            ]

            point = JointTrajectoryPoint()
            point.positions = [alpha, beta_1, beta_2, beta_3]

            point.time_from_start.sec = 1
            trajectory.points.append(point)
            self.publisher_.publish(trajectory)

        except ValueError as e:
            self.get_logger().warning(str(e))
            return


    def inverse_kinematics(self, position, orientation):
        x = position.x
        y = position.y
        z = position.z

        x_delta, y_delta = x, y
        alpha = np.arctan2(y_delta, x_delta)

        x_arm = np.hypot(x_delta, y_delta)
        z_arm = z - (self.arm_dimensions['h'] + self.arm_dimensions['b'])

        roll, pitch, yaw = euler_from_quaternion([
            orientation.x,
            orientation.y,
            orientation.z,
            orientation.w
        ])
        
        theta = pitch

        x_2 = x_arm - self.arm_dimensions['l3'] * np.cos(theta)
        z_2 = z_arm - self.arm_dimensions['l3'] * np.sin(theta)

        d = (x_2**2 + z_2**2 - self.arm_dimensions['l1']**2 - self.arm_dimensions['l2']**2) / (2 * self.arm_dimensions['l1'] * self.arm_dimensions['l2'])
        if abs(d) > 1.0:
            raise ValueError("Punkt kann nicht erreicht werden.")

        a = np.sqrt(x_2**2 + z_2**2)
        c = (a**2 - self.arm_dimensions['l1']**2 - self.arm_dimensions['l2']**2) / (2 * self.arm_dimensions['l1'])
        b = -np.sqrt(self.arm_dimensions['l2']**2 - c**2)

        theta_11 = np.arctan2(z_2, x_2)
        theta_12 = np.arctan2(b, self.arm_dimensions['l1'] + c)
        beta1 = theta_11 - theta_12
        
        beta2 = np.arctan2(b, c)

        beta3 = theta - beta1 - beta2

        return alpha, beta1, beta2, beta3

def main(args=None):
    rclpy.init(args=args)
    node = JKRollinArmIk()
    rclpy.spin(node)

    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
