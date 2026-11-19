from launch import LaunchDescription
from launch.actions import IncludeLaunchDescription
from launch.launch_description_sources import PythonLaunchDescriptionSource
from launch_ros.actions import Node
from ament_index_python.packages import get_package_share_directory
import os

def generate_launch_description():
    pkg_share = get_package_share_directory('my_task_execution')
    config_file = os.path.join(pkg_share, 'config', 'collect_and_place_hexagons.yaml')

    jk_rollin_gazebo_pkg = get_package_share_directory('jk_rollin_gazebo')
    gazebo_launch = IncludeLaunchDescription(
        PythonLaunchDescriptionSource(
            os.path.join(jk_rollin_gazebo_pkg, 'launch', 'jk_rollin.gazebo.launch.py')
        ),
        launch_arguments={
            'world_package': 'eurobot_gazebo',
            'world_file': 'eurobot22.sdf'
        }.items()
    )

    # Main Task Node
    collect_and_place = Node(
        package='my_task_execution',
        executable='collect_and_place_hexagons',
        name='collect_and_place_hexagons',
        output='screen',
        parameters=[config_file],
    )

    # --- ADDED: Inverse Kinematics Node ---
    # We assume this executable is defined in your setup.py
    # If not, we might need to add it to entry_points first!
    # For now, let's assume you will add it.
    arm_ik_node = Node(
         package='my_task_execution',
         executable='arm_ik', # We need to register this name in setup.py
         name='jk_rollin_arm_ik',
         output='screen'
    )

    return LaunchDescription([
        gazebo_launch,
        collect_and_place,
        arm_ik_node # <--- Don't forget to add this to the list
    ])