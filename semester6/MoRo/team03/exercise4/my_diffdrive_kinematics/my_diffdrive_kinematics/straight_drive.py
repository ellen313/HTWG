import rclpy
from rclpy.node import Node
from nav_msgs.msg import Odometry
from geometry_msgs.msg import TwistStamped
import math

class StraightDrive(Node):
    def __init__(self):
        super().__init__('straight_drive_node')

        self.target_reached = False
        self.start_pose_received = False
        self.should_drive = True

        self.declare_parameter("v", 0.0)
        self.declare_parameter("distance", 0.0)

        self.velocity = self.get_parameter("v").get_parameter_value().double_value
        self.target_distance = self.get_parameter("distance").get_parameter_value().double_value

        self.get_logger().info("v: " + str(self.velocity))
        self.get_logger().info("distance: " + str(self.target_distance))

        # Publisher
        self.publisher = self.create_publisher(
            TwistStamped,
            '/cmd_vel',
            10)

        # Subscriber (Odom)
        self.subscription = self.create_subscription(
            Odometry,
            '/odom',
            self.odom_callback,
            10)

        self.start_x = 0.0
        self.start_y = 0.0
        self.current_distance = 0.0
        self.last_log_distance = 0.0

        self.timer = self.create_timer(0.1, self.timer_callback)
        
    def timer_callback(self):
        if not self.target_reached and self.should_drive:
            self.drive_forward()

    def odom_callback(self, msg: Odometry):
        # save starting position
        if not self.start_pose_received:
            self.start_x = msg.pose.pose.position.x
            self.start_y = msg.pose.pose.position.y
            self.start_pose_received = True
            self.get_logger().info(f"Starting position: ({self.start_x:.3f}, {self.start_y:.3f})")
            
            self.drive_forward()
            return

        # calculate distance traveled
        dx = msg.pose.pose.position.x - self.start_x
        dy = msg.pose.pose.position.y - self.start_y
        self.current_distance = math.sqrt(dx*dx + dy*dy)


        self.get_logger().info(f"Current path: {self.current_distance:.3f} m")

        # check if target was reached
        if self.current_distance >= self.target_distance:
            if not self.target_reached:
                self.target_reached = True
                self.should_drive = False
                self.stop_robot()
                self.get_logger().info("Target reached")


    def drive_forward(self):
        twist = TwistStamped()
        twist.header.stamp = self.get_clock().now().to_msg()
        twist.twist.linear.x = self.velocity
        twist.twist.angular.z = 0.0
        self.publisher.publish(twist)
        self.get_logger().debug(f"Driving: {self.velocity} m/s")

    def stop_robot(self):
        twist = TwistStamped()
        twist.header.stamp = self.get_clock().now().to_msg()
        twist.twist.linear.x = 0.0
        twist.twist.angular.z = 0.0

        self.publisher.publish(twist)
        self.get_logger().info("Stop command sent.")

def main(args=None):
    rclpy.init(args=args)
    straight_drive = StraightDrive()

    while not straight_drive.target_reached:
        rclpy.spin_once(straight_drive)

    straight_drive.stop_robot()
    straight_drive.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
