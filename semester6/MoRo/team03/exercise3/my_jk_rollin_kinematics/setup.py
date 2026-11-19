from setuptools import find_packages, setup

package_name = 'my_jk_rollin_kinematics'

setup(
    name=package_name,
    version='0.1.0',
    packages=find_packages(exclude=['test']),
    data_files=[
        ('share/ament_index/resource_index/packages',
            ['resource/' + package_name]),
        ('share/' + package_name, ['package.xml']),
    ],
    install_requires=['setuptools'],
    zip_safe=True,
    maintainer='moro-3-ws26',
    maintainer_email='robolab@htwg-konstanz.de',
    description='Kinematics and trajectory calculation for the JK Rollin robot arm.',
    license='Apache-2.0',
    extras_require={
        'test': [
            'pytest',
        ],
    },
    entry_points={
        'console_scripts': [
            'jk_rollin_arm_ik = my_jk_rollin_kinematics.jk_rollin_arm_ik:main',
            'jk_rollin_draw_circle = my_jk_rollin_kinematics.jk_rollin_draw_circle:main'
        ],
    },
)
