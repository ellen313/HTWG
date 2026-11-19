import numpy as np
import rclpy
from rclpy.node import Node
from geometry_msgs.msg import PoseStamped

class JKRollinDrawCircle(Node):
    def __init__(self):
        super().__init__('jk_rollin_draw_circle')

        self.point = np.array([0.180, 0.000, 0.140])

        self.current_point = 0
        self.num_points = 100
        self.rotation = np.linspace(0 , 2 * np.pi, self.num_points)


        #publisher (like talk.py)
        self.publisher_ = self.create_publisher(
            PoseStamped,
            '/tool_pose',
            10)

        #Timer for 10Hz
        self.timer = self.create_timer(0.1, self.circle)

        self.get_logger().info("Circle Drawing wurde gestartet.")

    def circle(self):

        current_pos = np.array([self.point[0], 0.000, 0.04, 1.0])

        translation = np.array([0, self.point[1], self.point[2]])
        theta = -self.rotation[self.current_point]

        cos_theta =np.cos(theta)
        sin_theta = np.sin(theta)
        rotation_matrix = np.array([
            [1, 0, 0], 
            [0, cos_theta, -sin_theta], 
            [0, sin_theta, cos_theta]
        ])

        trans_matrix = np.eye(4)
        trans_matrix[:3, :3] = rotation_matrix
        trans_matrix[:3, 3] = translation

        trans_pos = trans_matrix @ current_pos
        x, y, z = trans_pos[0], trans_pos[1], trans_pos[2]

        msg = PoseStamped()
        msg.header.stamp = self.get_clock().now().to_msg()
        msg.header.frame_id = 'base_frame'

        msg.pose.position.x = float(x)
        msg.pose.position.y = float(y)
        msg.pose.position.z = float(z)

        msg.pose.orientation.x = 0.0
        msg.pose.orientation.y = 0.0
        msg.pose.orientation.z = 0.0
        msg.pose.orientation.w = 1.0

        self.publisher_.publish(msg)

        self.get_logger().info(f"Punkt {self.current_point + 1}/{self.num_points}: ({x:.3f}, {y:.3f}, {z:.3f})")

        self.current_point = (self.current_point + 1) % self.num_points

def main(args=None):
    rclpy.init(args=args)
    node = JKRollinDrawCircle()

    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        pass
    finally:
        node.destroy_node()
        rclpy.shutdown()

if __name__ == '__main__':
    main()
