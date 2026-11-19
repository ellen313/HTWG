import math
import rclpy
from rclpy.node import Node
from nav_msgs.msg import Odometry
from geometry_msgs.msg import TwistStamped
from tf_transformations import euler_from_quaternion

class ApproachGoal(Node):
    def __init__(self):
        super().__init__('approach_goal')

        self.declare_parameter('x', 0.0)
        self.declare_parameter('y', 0.0)
        self.declare_parameter('tol', 0.1)
        self.declare_parameter('v_max', 0.25)
        self.declare_parameter('omega_max', 2.5)
        self.declare_parameter('Kv', 0.8)
        self.declare_parameter('Komega', 2.0)

        self.goal_x = self.get_parameter('x').value
        self.goal_y = self.get_parameter('y').value
        self.tol = self.get_parameter('tol').value
        self.v_max = self.get_parameter('v_max').value
        self.omega_max = self.get_parameter('omega_max').value
        self.Kv = self.get_parameter('Kv').value
        self.Komega = self.get_parameter('Komega').value

        # Publisher
        self.publisher = self.create_publisher(
            TwistStamped,
            '/cmd_vel',
            10)

        # Subscriber
        self.subscription = self.create_subscription(
            Odometry,
            '/odom',
            self.odom_callback,
            10
        )

        self.current_pose = None
        self.target_reached = False

        self.timer = self.create_timer(0.05, self.control_loop)

        self.get_logger().info('approach_goal node started')

    def odom_callback(self, msg: Odometry):
        self.current_pose = msg.pose.pose

    def control_loop(self):
        if self.target_reached or self.current_pose is None:
            return

        x = self.current_pose.position.x
        y = self.current_pose.position.y
        q = self.current_pose.orientation

        _, _, theta = euler_from_quaternion([q.x, q.y, q.z, q.w])

        dx = self.goal_x - x
        dy = self.goal_y - y
        rho = math.sqrt(dx*dx + dy*dy)

        twist = TwistStamped()
        twist.header.stamp = self.get_clock().now().to_msg()

        # stop condition
        if rho < self.tol:
            twist.twist.linear.x = 0.0
            twist.twist.angular.z = 0.0
            self.publisher.publish(twist)
            self.target_reached = True
            return

        theta_star = math.atan2(dy, dx)
        diff = self.normalize_angle(theta_star - theta)

        v = self.Kv * rho
        v = min(v, self.v_max)
        v = max(v, 0.0)

        omega = self.Komega * diff
        omega = max(-self.omega_max, min(self.omega_max, omega))

        twist.twist.linear.x = v
        twist.twist.angular.z = omega
        self.publisher.publish(twist)

    def normalize_angle(self, angle):
        while angle > math.pi:
            angle -= 2.0 * math.pi
        while angle < -math.pi:
            angle += 2.0 * math.pi
        return angle

def main(args=None):
    rclpy.init(args=args)
    node = ApproachGoal()

    while rclpy.ok() and not node.target_reached:
        rclpy.spin_once(node)

    stop_msg = TwistStamped()
    stop_msg.header.stamp = node.get_clock().now().to_msg()
    stop_msg.twist.linear.x = 0.0
    stop_msg.twist.angular.z = 0.0

    for _ in range(5):
        node.publisher.publish(stop_msg)
        time.sleep(0.1)

    node.destroy_node()
    rclpy.shutdown()


if __name__ == '__main__':
    main()
