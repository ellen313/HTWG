import rclpy
from rclpy.node import Node
from geometry_msgs.msg import TwistStamped, PoseStamped
from nav_msgs.msg import Odometry
from std_msgs.msg import Bool
import math


def get_yaw_from_pose(pose):
    q = pose.orientation
    siny_cosp = 2 * (q.w * q.z + q.x * q.y)
    cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z)
    return math.atan2(siny_cosp, cosy_cosp)


def normalize_angle(angle):
    while angle > math.pi: angle -= 2.0 * math.pi
    while angle < -math.pi: angle += 2.0 * math.pi
    return angle


class ApproachGoal(Node):
    def __init__(self):
        super().__init__('approach_goal')
        self.declare_parameter('tol', 0.05)
        self.declare_parameter('v_max', 0.5)
        self.declare_parameter('omega_max', 2.0)
        self.declare_parameter('Kv', 1.5)
        self.declare_parameter('Kw', 4.0)

        self.cmd_pub = self.create_publisher(TwistStamped, '/cmd_vel', 10)
        self.goal_reached_pub = self.create_publisher(Bool, '/goal_reached', 10)
        self.odom_sub = self.create_subscription(Odometry, '/odom', self.odom_callback, 10)
        self.goal_sub = self.create_subscription(PoseStamped, '/navigation_goal', self.goal_callback, 10)

        self.timer = self.create_timer(0.05, self.control_loop)
        self.current_pose = None
        self.active_goal = None
        self.goal_reached_sent = False

    def odom_callback(self, msg):
        self.current_pose = msg.pose.pose

    def goal_callback(self, msg):
        self.active_goal = msg.pose.position
        self.goal_reached_sent = False
        self.get_logger().info(f"Neues Ziel: x={self.active_goal.x:.2f}, y={self.active_goal.y:.2f}")

    def control_loop(self):
        if self.current_pose is None or self.active_goal is None:
            return

        curr_x = self.current_pose.position.x
        curr_y = self.current_pose.position.y
        curr_yaw = get_yaw_from_pose(self.current_pose)
        dx = self.active_goal.x - curr_x
        dy = self.active_goal.y - curr_y
        dist_error = math.sqrt(dx ** 2 + dy ** 2)

        if dist_error < self.get_parameter('tol').value:
            stop_msg = TwistStamped()
            stop_msg.header.stamp = self.get_clock().now().to_msg()
            self.cmd_pub.publish(stop_msg)
            if not self.goal_reached_sent:
                self.goal_reached_pub.publish(Bool(data=True))
                self.goal_reached_sent = True
            return

        desired_yaw = math.atan2(dy, dx)
        yaw_error = normalize_angle(desired_yaw - curr_yaw)

        v = self.get_parameter('Kv').value * dist_error
        omega = self.get_parameter('Kw').value * yaw_error

        v = max(min(v, self.get_parameter('v_max').value), 0.0)
        omega = max(min(omega, self.get_parameter('omega_max').value), -self.get_parameter('omega_max').value)

        twist = TwistStamped()
        twist.header.stamp = self.get_clock().now().to_msg()
        twist.header.frame_id = 'base_link'
        twist.twist.linear.x = float(v)
        twist.twist.angular.z = float(omega)
        self.cmd_pub.publish(twist)


def main(args=None):
    rclpy.init(args=args)
    node = ApproachGoal()
    rclpy.spin(node)
    node.destroy_node()
    rclpy.shutdown()


if __name__ == '__main__':
    main()