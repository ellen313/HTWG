from exercise1.transformations import *
import numpy as np


# Exercise 2.2 from lecture
def exercise_22_a_t_oa():
    """
    :return: The homogenous transformation matrix T^O_A from Task 2.2.
    """
    r = rot(theta=np.radians(0))
    R = rot2trans(r)
    T = trans([1, 1])

    T_OA = T @ R
    return T_OA

def exercise_22_a_t_ob():
    """
    :return: The homogenous transformation matrix T^O_B from Task 2.2.
    """
    r = rot(theta=np.radians(30))
    R = rot2trans(r)
    T = trans([3, 2])

    T_OB = T @ R
    return T_OB


def exercise_22_b(pb):
    """
    Transforms the point P from coordinate system B to O.
    :param pb: The point P in coordinates with respect to B. P is not in homogenous representation.
    :return: P after the transformation. The result is not in homogenous representation.
    """
    pb_hom = np.append(pb, 1)

    P_O_hom = exercise_22_a_t_ob() @ pb_hom
    P_O = P_O_hom[:2]

    return P_O


def exercise_22_c_t_ab():
    """
    :return: The homogenous transformation matrix T^A_B calculated from T^O_A and T^O_B.
    """
    T_AO = np.linalg.inv(exercise_22_a_t_oa())
    T_AB = T_AO @ exercise_22_a_t_ob()

    return T_AB


def exercise_22_d(pb):
    """
    Transforms the point P from coordinate system B to A.
    :param pb: The point P in coordinates with respect to B. P is not in homogenous representation.
    :return: P after the transformation. The result is not in homogenous representation.
    """
    pb_hom = np.append(pb, 1)

    P_A_hom = exercise_22_c_t_ab() @ pb_hom
    P_A = P_A_hom[:2]

    return P_A


def exercise_22_e(pa):
    """
    Performs the rotation of p^A with T^A_B.
    :param pa: The point P in coordinates with respect to B. P is not in homogenous representation.
    :return: P after the operation. The result is not in homogenous representation.
    """
    pa_hom = np.append(pa, 1)

    Q_A_hom = exercise_22_c_t_ab() @ pa_hom
    Q_A = Q_A_hom[:2]

    return Q_A

