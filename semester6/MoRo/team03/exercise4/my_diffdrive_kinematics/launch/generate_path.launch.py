from launch import LaunchDescription
from launch.conditions import IfCondition
from launch.actions import DeclareLaunchArgument, IncludeLaunchDescription
from launch.substitutions import LaunchConfiguration
from launch.launch_description_sources import PythonLaunchDescriptionSource
from launch_ros.actions import Node
from ament_index_python.packages import get_package_share_directory
import os


def generate_launch_description():
    base_path = os.path.realpath(
        get_package_share_directory('my_diffdrive_kinematics'))
    rviz_path = os.path.join(base_path, 'rviz', 'generate_path.rviz')

    launch_gazebo = LaunchConfiguration('launch_gazebo')
    launch_rviz = LaunchConfiguration('launch_rviz')

    arg_config = DeclareLaunchArgument('config_file',
                                       default_value=os.path.join(
                                           base_path, 'config', 'default_path.yaml'),
                                       description='Source from where to load points for path.')

    arg_rviz = DeclareLaunchArgument('launch_rviz',
                                     default_value='False',
                                     description='Flag to start Rviz with node.')

    arg_gazebo = DeclareLaunchArgument('launch_gazebo',
                                       default_value='False',
                                       description='Flag to start Gazebo with node.')

    arg_rviz_config = DeclareLaunchArgument('rviz_config',
                                            default_value=rviz_path,
                                            description='Default Path to Rviz '
                                                        'configuration to display Path.')

    path_node = Node(
        package='my_diffdrive_kinematics',
        executable='generate_path',
        name='generate_path_node',
        parameters=[LaunchConfiguration('config_file')]
    )

    rviz_node = Node(
        package='rviz2',
        executable='rviz2',
        name='rviz2',
        arguments=['-d', LaunchConfiguration('rviz_config')],
        condition=IfCondition(launch_rviz)
    )
    gazebo_node = IncludeLaunchDescription(
        PythonLaunchDescriptionSource([
            os.path.join(get_package_share_directory('jk_rollin_gazebo'),
                         'launch', 'jk_rollin.gazebo.launch.py')
        ]),
        launch_arguments={
            'use_rviz': 'false',
            'haedless': 'False'
        }.items(),
        condition=IfCondition(launch_gazebo)
    )

    return LaunchDescription([
        arg_config,
        arg_rviz_config,
        arg_rviz,
        arg_gazebo,
        path_node,
        rviz_node,
        gazebo_node
    ])
