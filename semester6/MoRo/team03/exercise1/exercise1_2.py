from exercise1.transformations import *
import numpy as np


# Exercise 2.2 from lecture
def exercise_21_a_t_ab():
    """
    :return: The transformation matrix T^A_B from task 2.1.
    """
    r = rotz(theta=np.radians(180))
    R = rot2trans(r)
    T = trans([-2, 0, 0])

    T_AB = T @ R # matrix multiplication
    return T_AB


def exercise_21_a_t_bc():
    """
    :return: The transformation matrix T^B_C from task 2.1.
    """
    r = rotz(theta=np.radians(270))
    R = rot2trans(r)
    T = trans([-4, -1, 0])

    T_BC = T @ R
    return T_BC


def exercise_21_a_t_ac():
    """
    :return: The transformation matrix T^A_C from task 2.1.
    """
    r = rotz(theta=np.radians(90))
    R = rot2trans(r)
    T = trans([2, 1, 0])

    T_AC = T @ R
    return T_AC


def exercise_21_b_t_ac():
    """
    :return: The transformation matrix T^A_C calculated from T^A_B and T^B_C.
    """
    T_AC_calc = exercise_21_a_t_ab() @ exercise_21_a_t_bc()
    return T_AC_calc


def exercise_21_c_t_ca():
    """
    :return: The transformation matrix T^C_A calculated from the inverse of T^A_C.
    """
    T_CA = np.linalg.inv(exercise_21_a_t_ac())
    return T_CA


def exercise_21_d(pb):
    """
    Transforms the point P from coordinate system B to A.
    :param pb: The point P in coordinates with respect to B. P is not in homogenous representation.
    :return: P after the transformation. The result is not in homogenous representation.
    """
    pb_hom = np.append(pb, 1)

    P_hom = exercise_21_a_t_ab() @ pb_hom
    P = P_hom[:3]

    return P

