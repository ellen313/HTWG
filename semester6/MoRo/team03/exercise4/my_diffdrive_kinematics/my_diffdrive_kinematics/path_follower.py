import math
import time
import rclpy
import time
from rclpy.node import Node
from nav_msgs.msg import Path, Odometry
from geometry_msgs.msg import TwistStamped
from visualization_msgs.msg import Marker


def normalize_angle(angle):
    while angle > math.pi:
        angle -= 2.0 * math.pi
    while angle < -math.pi:
        angle += 2.0 * math.pi
    return angle


def get_yaw(q):
    siny_cosp = 2 * (q.w * q.z + q.x * q.y)
    cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z)
    return math.atan2(siny_cosp, cosy_cosp)


class PathFollower(Node):
    def __init__(self):
        super().__init__('path_follower')

        # Parameter
        self.declare_parameter('v_p', 0.2)
        self.declare_parameter('d_star', 0.5)
        self.declare_parameter('Kv', 4.0)
        self.declare_parameter('Kw', 2.5)
        self.declare_parameter('v_max', 0.5)
        self.declare_parameter('omega_max', 1.0)

        self.v_p = self.get_parameter('v_p').value
        self.d_star = self.get_parameter('d_star').value
        self.Kv = self.get_parameter('Kv').value
        self.Kw = self.get_parameter('Kw').value
        self.v_max = self.get_parameter('v_max').value
        self.omega_max = self.get_parameter('omega_max').value

        self.twist_publisher = self.create_publisher(
            TwistStamped,
            '/cmd_vel',
            10)
        self.marker_publisher = self.create_publisher(
            Marker,
            'carrot',
            10)
        self.path_subscription = self.create_subscription(
            Path,
            '/path',
            self.path_callback,
            10)
        self.odom_subscription = self.create_subscription(
            Odometry,
            '/odom',
            self.odom_callback,
            10)

        self.path = None
        self.carrot_index = 0.0
        self.robot_pose = None
        self.target_reached = False
        self.finishing_mode = False
        self.finish_start_time = None
        self.last_time = self.get_clock().now()

        self.timer = self.create_timer(0.05, self.control_loop)
        self.get_logger().info("PathFollower node started")

    def path_callback(self, msg: Path):
        if self.path is None and len(msg.poses) >= 2:
            self.path = msg.poses
            self.get_logger().info(f"Path received with {len(self.path)} poses")

    def odom_callback(self, msg: Odometry):
        self.robot_pose = msg.pose.pose

    def control_loop(self):
        current_time = self.get_clock().now()
        dt = (current_time - self.last_time).nanoseconds / 1e9
        self.last_time = current_time

        if self.path is None or self.robot_pose is None or self.target_reached:
            return

        if not self.finishing_mode:
            path_resolution = 0.1
            step_dist = self.v_p * dt
            self.carrot_index += step_dist / path_resolution

        current_idx = int(self.carrot_index)
        if current_idx >= len(self.path) - 1:
            if not self.finishing_mode:
                self.finishing_mode = True
                self.finish_start_time = self.get_clock().now()
                self.get_logger().info("Carrot at end. Entering finishing mode")
            current_idx = len(self.path) - 1

        carrot_pose = self.path[current_idx].pose.position
        self.publish_marker(carrot_pose)

        rx = self.robot_pose.position.x
        ry = self.robot_pose.position.y
        ryaw = get_yaw(self.robot_pose.orientation)

        dx = carrot_pose.x - rx
        dy = carrot_pose.y - ry
        d = math.sqrt(dx ** 2 + dy ** 2)

        if self.finishing_mode:
            err = abs(d - self.d_star)
            elapsed = (self.get_clock().now() - self.finish_start_time).nanoseconds / 1e9
            if err < 0.05 or elapsed > 5.0:
                self.target_reached = True
                self.get_logger().info(f"Goal reached. Dist: {d:.3f}")

                for _ in range(5):
                    self.stop_robot()
                    time.sleep(0.1)
                return

        v_cmd = self.Kv * (d - self.d_star)
        omega_cmd = self.Kw * normalize_angle(math.atan2(dy, dx) - ryaw)

        v_cmd = max(0.0, min(self.v_max, v_cmd))
        omega_cmd = max(-self.omega_max, min(self.omega_max, omega_cmd))

        twist = TwistStamped()
        twist.header.stamp = self.get_clock().now().to_msg()
        twist.header.frame_id = 'base_link'
        twist.twist.linear.x = float(v_cmd)
        twist.twist.angular.z = float(omega_cmd)
        self.twist_publisher.publish(twist)

    def publish_marker(self, position):
        marker = Marker()
        marker.header.frame_id = "odom"
        marker.header.stamp = self.get_clock().now().to_msg()
        marker.type = Marker.SPHERE
        marker.action = Marker.ADD
        marker.pose.position.x = position.x
        marker.pose.position.y = position.y
        marker.pose.position.z = position.z
        marker.pose.orientation.w = 1.0
        marker.scale.x = marker.scale.y = marker.scale.z = 0.2
        marker.color.a = 1.0
        marker.color.r = 1.0
        marker.color.g = 0.5
        self.marker_publisher.publish(marker)

    def stop_robot(self):
        twist = TwistStamped()
        twist.header.stamp = self.get_clock().now().to_msg()
        twist.twist.linear.x = 0.0
        twist.twist.angular.z = 0.0
        self.twist_publisher.publish(twist)

def main(args=None):
    rclpy.init(args=args)
    path_follower = PathFollower()
    rclpy.spin(path_follower)
    path_follower.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
