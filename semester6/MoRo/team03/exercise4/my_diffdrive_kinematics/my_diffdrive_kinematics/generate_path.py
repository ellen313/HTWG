import rclpy
from rclpy.node import Node
from nav_msgs.msg import Path
from geometry_msgs.msg import PoseStamped
import numpy as np
from rcl_interfaces.msg import ParameterDescriptor


class GeneratePath(Node):
    def __init__(self):
        super().__init__('generate_path_node')

        default_x = [0.0, 1.0]
        default_y = [0.0, 0.0]

        self.declare_parameter('waypoints_x', default_x)
        self.declare_parameter('waypoints_y', default_y)

        try:
            wx = self.get_parameter('waypoints_x').value
            wy = self.get_parameter('waypoints_y').value

            self.wp_x = [float(x) for x in wx]
            self.wp_y = [float(y) for y in wy]
        except Exception as e:
            self.get_logger().error(f"Failed to load parameters: {e}")
            self.wp_x = default_x
            self.wp_y = default_y

        self.path_pub = self.create_publisher(Path, '/path', 10)
        self.timer = self.create_timer(1.0, self.publish_path)

        self.path = Path()
        self.build_path()
        self.get_logger().info(f"generate_path node started with {len(self.path.poses)}")

    def build_path(self):
        self.path.header.frame_id = "odom"

        if len(self.wp_x) < 2 or len(self.wp_x) != len(self.wp_y):
            self.get_logger().error("Invalid")
            self.path.poses = []
            return

        poses = []
        step_size = 0.1

        for i in range(len(self.wp_x) - 1):
            p1 = np.array([self.wp_x[i], self.wp_y[i]])
            p2 = np.array([self.wp_x[i + 1], self.wp_y[i + 1]])

            dist = np.linalg.norm(p2 - p1)

            if dist < 1e-6:
                continue

            num_steps = int(dist / step_size)
            if num_steps < 1:
                num_steps = 1

            for j in range(num_steps):
                alpha = j / float(num_steps)
                point = (1.0 - alpha) * p1 + alpha * p2

                pose = PoseStamped()
                pose.header.frame_id = "odom"
                pose.pose.position.x = float(point[0])
                pose.pose.position.y = float(point[1])
                pose.pose.orientation.w = 1.0
                poses.append(pose)

        last_pose = PoseStamped()
        last_pose.header.frame_id = "odom"
        last_pose.pose.position.x = float(self.wp_x[-1])
        last_pose.pose.position.y = float(self.wp_y[-1])
        last_pose.pose.orientation.w = 1.0
        poses.append(last_pose)

        self.path.poses = poses

    def publish_path(self):
        self.path.header.stamp = self.get_clock().now().to_msg()
        self.path_pub.publish(self.path)


def main(args=None):
    rclpy.init(args=args)
    node = GeneratePath()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
