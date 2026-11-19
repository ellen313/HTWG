import rclpy
from rclpy.node import Node
from geometry_msgs.msg import PoseStamped, TwistStamped
from std_msgs.msg import Bool
from rclpy.qos import QoSProfile, ReliabilityPolicy
import math
import time
from tf_transformations import euler_from_quaternion


def normalize_angle(angle):
    while angle > math.pi: angle -= 2.0 * math.pi
    while angle < -math.pi: angle += 2.0 * math.pi
    return angle


class CollectAndPlaceHexagons(Node):
    def __init__(self):
        super().__init__('collect_and_place_hexagons')

        # --- Communication Setup ---
        # 1. Localization: Use Reliable (as confirmed by your 'ros2 topic info')
        qos_policy = QoSProfile(reliability=ReliabilityPolicy.RELIABLE, depth=10)
        self.pose_sub = self.create_subscription(PoseStamped, '/gt_pose', self.pose_callback, qos_policy)

        # 2. Navigation
        self.cmd_pub = self.create_publisher(TwistStamped, '/cmd_vel', 10)

        # 3. Gripper & Arm
        self.gripper_pub = self.create_publisher(Bool, '/vacuum_gripper/attach', 10)
        self.arm_pub = self.create_publisher(PoseStamped, '/tool_pose', 10)

        # --- State Variables ---
        self.current_pose = None
        self.current_yaw = 0.0
        self.state = 'IDLE'
        self.state_start_time = 0
        self.first_pose_received = False

        # Capture the time when the node starts
        self.node_start_time = time.time()

        # --- Targets ---
        self.pickup_pose = {'x': 1.0, 'y': 1.0}
        self.drop_pose = {'x': 0.5, 'y': 2.5}

        self.timer = self.create_timer(0.05, self.control_loop)
        self.get_logger().info('CollectAndPlace Node Started. Waiting for 5s startup delay...')

    def pose_callback(self, msg):
        self.current_pose = msg.pose
        q = msg.pose.orientation
        (_, _, self.current_yaw) = euler_from_quaternion([q.x, q.y, q.z, q.w])

        if not self.first_pose_received:
            self.get_logger().info(f"!!! POSE RECEIVED: x={msg.pose.position.x:.2f} !!!")
            self.first_pose_received = True

    def drive_to(self, target_x, target_y):
        """Returns True if target reached"""
        if self.current_pose is None: return False

        dx = target_x - self.current_pose.position.x
        dy = target_y - self.current_pose.position.y
        dist = math.sqrt(dx ** 2 + dy ** 2)

        cmd = TwistStamped()
        cmd.header.stamp = self.get_clock().now().to_msg()
        cmd.header.frame_id = 'base_link'

        if dist > 0.05:  # Tolerance
            desired_yaw = math.atan2(dy, dx)
            yaw_err = normalize_angle(desired_yaw - self.current_yaw)

            # Backward driving logic
            v_cmd = 1.0 * dist
            if abs(yaw_err) > math.pi / 2:
                desired_yaw = normalize_angle(desired_yaw + math.pi)
                yaw_err = normalize_angle(desired_yaw - self.current_yaw)
                v_cmd = -v_cmd

            # Speed limits
            v_cmd = max(min(v_cmd, 0.25), -0.25)
            w_cmd = 4.0 * yaw_err
            w_cmd = max(min(w_cmd, 1.0), -1.0)

            cmd.twist.linear.x = float(v_cmd)
            cmd.twist.angular.z = float(w_cmd)
            self.cmd_pub.publish(cmd)
            return False
        else:
            # Stop
            cmd.twist.linear.x = 0.0
            cmd.twist.angular.z = 0.0
            self.cmd_pub.publish(cmd)
            return True

    def move_arm(self, x, y, z):
        msg = PoseStamped()
        msg.header.stamp = self.get_clock().now().to_msg()
        msg.pose.position.x = float(x)
        msg.pose.position.y = float(y)
        msg.pose.position.z = float(z)
        msg.pose.orientation.w = 1.0
        self.arm_pub.publish(msg)

    def set_gripper(self, state):
        msg = Bool()
        msg.data = state
        self.gripper_pub.publish(msg)

    def control_loop(self):
        # We need the pose to be valid before doing anything
        if self.current_pose is None: return

        # --- STATE MACHINE ---
        if self.state == 'IDLE':
            # NEW: Wait for 5 seconds before starting the mission
            elapsed_startup = time.time() - self.node_start_time
            if elapsed_startup > 5.0:
                self.state = 'DRIVE_TO_PICKUP'
                self.get_logger().info("Startup delay complete. State: DRIVE_TO_PICKUP")
            else:
                # Optional: log waiting status every second roughly
                pass

        elif self.state == 'DRIVE_TO_PICKUP':
            self.move_arm(0.18, 0.0, 0.20)
            if self.drive_to(self.pickup_pose['x'], self.pickup_pose['y']):
                self.state = 'LOWER_ARM'
                self.state_start_time = time.time()
                self.get_logger().info("State: LOWER_ARM")

        elif self.state == 'LOWER_ARM':
            self.move_arm(0.18, 0.0, 0.085)
            self.set_gripper(True)
            if time.time() - self.state_start_time > 3.0:
                self.state = 'LIFT_ARM'
                self.state_start_time = time.time()
                self.get_logger().info("State: LIFT_ARM")

        elif self.state == 'LIFT_ARM':
            self.move_arm(0.18, 0.0, 0.25)
            self.set_gripper(True)
            if time.time() - self.state_start_time > 2.0:
                self.state = 'DRIVE_TO_DROP'
                self.get_logger().info("State: DRIVE_TO_DROP")

        elif self.state == 'DRIVE_TO_DROP':
            self.set_gripper(True)
            if self.drive_to(self.drop_pose['x'], self.drop_pose['y']):
                self.state = 'PLACE_OBJECT'
                self.state_start_time = time.time()
                self.get_logger().info("State: PLACE_OBJECT")

        elif self.state == 'PLACE_OBJECT':
            self.move_arm(0.25, 0.0, 0.16)
            if time.time() - self.state_start_time > 3.0:
                self.set_gripper(False)
                self.get_logger().info("MISSION COMPLETE")
                self.state = 'DONE'


def main(args=None):
    rclpy.init(args=args)
    node = CollectAndPlaceHexagons()
    try:
        rclpy.spin(node)
    except KeyboardInterrupt:
        pass
    finally:
        node.destroy_node()
        rclpy.shutdown()


if __name__ == '__main__':
    main()