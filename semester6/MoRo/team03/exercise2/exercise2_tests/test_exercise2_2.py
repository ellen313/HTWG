import numpy as np
import pytest
from exercise2.exercise2 import *


# TODO: Extend tests for more test cases if necessary.

def test_inverse_kinematics():
    # When testing, make sure that the expected solution also meets the Elbow-Up definition.
    # The following example shows a possible Elbow-Up configuration.
    expected_alpha = np.radians(20)
    expected_beta_1 = np.radians(40)
    expected_beta_2 = np.radians(-50)

    robot = Robot()
    p_r = (1.07263144, 0.29941336, 0.53456972, 1.)

    print(p_r)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_arm_forward():
    expected_alpha = np.radians(0)
    expected_beta_1 = np.radians(0)
    expected_beta_2 = np.radians(0)

    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (1.25, 0.0, 0.3, 1.)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_arm_backward():
    expected_alpha = np.radians(180)
    expected_beta_1 = np.radians(0)
    expected_beta_2 = np.radians(0)

    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (-0.75, 0.0, 0.3, 1.)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_arm_upward():
    expected_alpha = np.radians(0)
    expected_beta_1 = np.radians(90)
    expected_beta_2 = np.radians(0)

    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (0.25, 0.0, 1.3, 1.)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_arm_bent_forward():
    expected_alpha = np.radians(0)
    expected_beta_1 = np.radians(90)
    expected_beta_2 = np.radians(-90)

    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (0.75, 0.0, 0.8, 1.)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_arm_bent_backward():
    expected_alpha = np.radians(180)
    expected_beta_1 = np.radians(90)
    expected_beta_2 = np.radians(-90)

    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (-0.25, 0.0, 0.8, 1.)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_arm_bent_left():
    expected_alpha = np.radians(90)
    expected_beta_1 = np.radians(90)
    expected_beta_2 = np.radians(-90)

    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (0.25, 0.5, 0.8, 1.)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_arm_bent_right():
    expected_alpha = np.radians(-90)
    expected_beta_1 = np.radians(90)
    expected_beta_2 = np.radians(-90)

    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (0.25, -0.5, 0.8, 1.)

    res_alpha, res_beta_1, res_beta_2 = robot.inverse_kinematics(p_r)
    result = np.rad2deg((res_alpha, res_beta_1, res_beta_2)).round(3)
    expected = np.rad2deg(
        (expected_alpha, expected_beta_1, expected_beta_2)).round(3)

    assert np.array_equal(result, expected)


def test_inverse_kinematics_value_error():
    robot = Robot(x_r=0, y_r=0, theta=0)
    p_r = (1.26, 0.0, 0.3, 1.0)
    with pytest.raises(ValueError):
        robot.inverse_kinematics(p_r)


def test_inverse_kinematics_case_p_r6():
    #specific test for pipeline case p_r6
    p_r = (0.25, 0, 0.3, 1)

    students_robot = Robot(x_r=0, y_r=0, theta=0, l=0.6, h=0.2, r=0.1, a=0.1, b=0.1, l_1=0.5, l_2=0.5)

    result = np.round(students_robot.inverse_kinematics(p_r), 3)
    expected = np.round((0.0, -1.571, -3.142), 3)

    assert np.array_equal(result, expected), f"Failed p_r6: got {result}, expected {expected}"