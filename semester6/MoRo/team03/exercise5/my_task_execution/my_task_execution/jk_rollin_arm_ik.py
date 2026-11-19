import math


class InverseKinematics:
    def __init__(self):
        # Längen prüfen! (Beispielwerte)
        self.l1 = 0.16  # Schulterhöhe
        self.l2 = 0.30  # Oberarm
        self.l3 = 0.30  # Unterarm

    def calculate_joint_angles(self, x, y, z):
        q1 = math.atan2(y, x)

        r = math.sqrt(x ** 2 + y ** 2)
        z_arm = z - self.l1
        d_sq = r ** 2 + z_arm ** 2
        d = math.sqrt(d_sq)

        # Kosinussatz
        cos_q3 = (d_sq - self.l2 ** 2 - self.l3 ** 2) / (2 * self.l2 * self.l3)
        q3 = -math.acos(max(min(cos_q3, 1.0), -1.0))

        alpha = math.atan2(z_arm, r)
        cos_beta = (d_sq + self.l2 ** 2 - self.l3 ** 2) / (2 * d * self.l2)
        beta = math.acos(max(min(cos_beta, 1.0), -1.0))
        q2 = alpha - beta

        return [q1, q2, q3]