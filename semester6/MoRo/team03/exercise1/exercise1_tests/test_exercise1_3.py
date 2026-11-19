import numpy as np
from exercise1.exercise1_3 import *


# Tests for exercise 2.2 from lecture
def test_exercise_22_a_t_oa():
    """
    Tests the result of exercise_22_a_t_oa() against the correct solution for T^O_A from the lecture.
    """
    result = exercise_22_a_t_oa().round(3)
    expected = np.array([
        [1, 0, 1],
        [0, 1, 1],
        [0, 0, 1]])

    assert np.array_equal(result, expected), "Arrays are not equal.\n\nExpected:\n{}\n\nGot:\n{}".format(
        "\n".join(["  " + str(row) for row in expected]),
        "\n".join(["  " + str(row) for row in result])
    )


def test_exercise_22_a_t_ob():
    """
    Tests the result of exercise_22_a_t_ob() against the correct solution for T^O_B from the lecture.
    """
    result = exercise_22_a_t_ob().round(2)
    expected = np.array([
        [0.87, -0.5, 3],
        [0.5, 0.87, 2],
        [0, 0, 1]])

    assert np.array_equal(result, expected), "Arrays are not equal.\n\nExpected:\n{}\n\nGot:\n{}".format(
        "\n".join(["  " + str(row) for row in expected]),
        "\n".join(["  " + str(row) for row in result])
    )


def test_exercise_22_b():
    """
    Tests the result of exercise_22_b() against the correct solution for p^O from the lecture.
    The argument p^B of exercise_22_b() must not be in homogenous coordinates, as well as p^O.
    """
    result = exercise_22_b(pb = [1, 1]).round(2)
    expected = np.array([3.37, 3.37])

    assert np.array_equal(result, expected), "Arrays are not equal.\n\nExpected:\n{}\n\nGot:\n{}".format(
        "\n".join(["  " + str(row) for row in expected]),
        "\n".join(["  " + str(row) for row in result])
    )


def test_exercise_22_c_t_ab():
    """
    Tests the result of exercise_22_c_t_ab() against the correct solution for T^A_B from the lecture.
    """
    result = exercise_22_c_t_ab().round(2)
    expected = np.array([
        [0.87, -0.5, 2],
        [0.5, 0.87, 1],
        [0, 0, 1]])

    assert np.array_equal(result, expected), "Arrays are not equal.\n\nExpected:\n{}\n\nGot:\n{}".format(
        "\n".join(["  " + str(row) for row in expected]),
        "\n".join(["  " + str(row) for row in result])
    )


def test_exercise_22_d():
    """
    Tests the result of exercise_22_d() against the correct solution for p^A from the lecture.
    The argument p^B of exercise_22_d() must not be in homogenous coordinates, as well as p^A.
    """
    result = exercise_22_d(pb=[1, 1]).round(2)
    expected = np.array([2.37, 2.37])

    assert np.array_equal(result, expected), "Arrays are not equal.\n\nExpected:\n{}\n\nGot:\n{}".format(
        "\n".join(["  " + str(row) for row in expected]),
        "\n".join(["  " + str(row) for row in result])
    )


def test_exercise_22_e():
    """
    Tests the result of exercise_22_e() against the result of the rotation of p^A from the lecture.
    The argument p^A of exercise_22_e() must not be in homogenous coordinates, as well as the result.
    """
    result = exercise_22_e(pa=exercise_22_d(pb=[1, 1])).round(2)
    expected = np.array([2.87, 4.23])

    assert np.array_equal(result, expected), "Arrays are not equal.\n\nExpected:\n{}\n\nGot:\n{}".format(
        "\n".join(["  " + str(row) for row in expected]),
        "\n".join(["  " + str(row) for row in result])
    )
