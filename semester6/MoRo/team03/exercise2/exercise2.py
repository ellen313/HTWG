import numpy as np
import os
import matplotlib.pyplot as plt
from exercise1.transformations import *
from exercise1.transformations import rot2trans


class Robot:
    def __init__(self, x_r=2, y_r=1, theta=np.radians(30), l=0.6, h=0.2, r=0.1, a=0.1, b=0.1, l_1=0.5, l_2=0.5):
        self.x_r = x_r
        self.y_r = y_r
        self.theta = theta
        self.l = l
        self.h = h
        self.r = r
        self.a = a
        self.b = b
        self.l_1 = l_1
        self.l_2 = l_2

    # Exercise 2.1
    def forward_kinematics(self, alpha, beta_1, beta_2):
        p_a2 = (0, 0, 0, 1)

        #1. step: T_OR = Tl( xR, yR, r)^T) ∗ Rz(z, theta)
        T_OR = trans(np.array([self.x_r, self.y_r, self.r])) @ rot2trans(rotz(self.theta))

        #2.step: T_RDB = Tl( l/2 − a/2 , 0, h)^T)
        T_RDB = trans(np.array([(self.l / 2) - (self.a / 2), 0, self.h]))

        #3. step: T_DDB = Tl( 0, 0, b)^T) ∗ Rz(z,alpha) ∗ Rx(x, 90°)
        T_DBD = trans(np.array([0, 0, self.b])) @ rot2trans(rotz(alpha)) @ rot2trans(rotx(np.radians(90)))

        #4. step: T_DA1 = Tl( 0, 0, a/2)^T) ∗ Rz(z,beta1) ∗ Tl( l1, 0, 0)^T)
        T_DA1 = rot2trans(rotz(beta_1)) @ trans(np.array([self.l_1, 0, 0]))

        #5. step: 𝐓_A1A2 = Rz(z,beta2) ∗ Tl( l2, 0, 0)^T)
        T_A1A2 = rot2trans(rotz(beta_2)) @ trans(np.array([self.l_2, 0, 0]))

        #6. step: p_O
        p_O = T_OR @ T_RDB @ T_DBD @ T_DA1 @ T_A1A2 @ p_a2

        return p_O

    # Exercise 2.2
    def inverse_kinematics(self, p_r):
        #p_r ist die Position im Roboter-KS

        T_RDB = trans(np.array([(self.l / 2) - (self.a / 2), 0, self.h]))
        p_db = np.linalg.inv(T_RDB) @ p_r

        x_db = p_db[0]
        y_db = p_db[1]

        alpha = np.arctan2(y_db, x_db)

        T_DBD = trans(np.array([0, 0, self.b])) @ rot2trans(rotz(alpha)) @ rot2trans(rotx(np.radians(90)))
        p_d = np.linalg.inv(T_DBD) @ p_db

        x_d = p_d[0]
        y_d = p_d[1]

        #Distanzen von der Ziel Position nach D
        l = np.sqrt(x_d ** 2 + y_d ** 2) #Seitenansicht

        if l > self.l_1 + self.l_2:
            raise ValueError(f"Punkt {p_r} kann nicht erreicht werden, da l > l1 + l2.")

        #l = l_1^2 + l_2^2 - 2 * l_1 * l_2 * cos(beta2) -> Kosinussatz
        c_d = (l ** 2 - self.l_1 ** 2 - self.l_2 ** 2) / (2 * self.l_1)
        b_d = -np.sqrt(self.l_2 ** 2 - c_d ** 2)

        beta_2 = np.arctan2(b_d, c_d)  # elbow-up

        theta_11 = np.arctan2(y_d, x_d)
        theta_12 = np.arctan2(b_d, c_d + self.l_1)
        beta_1 = theta_11 - theta_12

        return alpha, beta_1, beta_2


# Exercise 2.3

def joint_angles_for_circle_motion(robot, km_r=(1, 0, 0.55, 1), radius=0.25, positions_in_circle=100):
    # positions_in_circle specifies how many points of the circle should be approached by the arm.
    # This function is not intended for plotting.
    # Instead, it calculates the progression of the joint angles.

    #Winkel erzeugen von 0 bis 2*pi
    angles = np.linspace(0, 2 * np.pi, positions_in_circle)

    #Gelenkwinkel
    alphas = np.zeros(positions_in_circle)
    beta_1s = np.zeros(positions_in_circle)
    beta_2s = np.zeros(positions_in_circle)

    for i, angle in enumerate(angles):
        x = km_r[0]
        y = km_r[1] + radius * np.sin(angle)
        z = km_r[2] + radius * np.cos(angle)
        p_r = (x, y, z, 1)

        try:
            alpha, beta_1, beta_2 = robot.inverse_kinematics(p_r)
            alphas[i] = alpha
            beta_1s[i] = beta_1
            beta_2s[i] = beta_2
        except ValueError:
            print(f"Punkt {p_r} nicht erreichbar")
            continue

    return alphas, beta_1s, beta_2s


def plot_angles(alphas, beta_1s, beta_2s, positions_in_circle=100):
    os.makedirs('plots', exist_ok=True)

    plt.figure(figsize=(8, 6))
    dregres = np.linspace(0, 360, positions_in_circle)
    plt.plot(dregres, np.degrees(alphas), label='alpha', linestyle='-')
    plt.plot(dregres, np.degrees(beta_1s), label='beta1', linestyle='-')
    plt.plot(dregres, np.degrees(beta_2s), label='beta2', linestyle='-')

    plt.title('Winkelverläufe')
    plt.xlabel('Position im Kreis [°]')
    plt.ylabel('Winkeleinstellung [°]')
    plt.legend()
    plt.grid(True)

    output_path = 'plots/gelenkwinkel.png'
    plt.savefig(output_path)
    plt.show()

def plot_circle(reached_points, km_r=(1, 0, 0.55, 1), radius=0.25):
    os.makedirs('plots', exist_ok=True)
    pts = np.array(reached_points)

    fig = plt.figure(figsize=(10,8))
    ax = fig.add_subplot(111, projection='3d')
    ax.plot(pts[:, 0], pts[:, 1], pts[:, 2], 'b-o', label='Erreichte Punkte')
    ax.scatter(pts[0, 0], pts[0, 1], pts[0, 2], color='red', label='Startpunkt')


    ax.set_title('Kreisbahn der Armspitze')
    ax.set_xlabel('x [m]')
    ax.set_ylabel('y [m]')
    ax.set_zlabel('z [m]')
    ax.legend()
    ax.grid(True)
    
    save_path = 'plots/kreis.png'
    plt.savefig(save_path)
    plt.show()

if __name__ == '__main__':
    robot = Robot()
    km_r = (1.0, 0.0, 0.55, 1.0)
    radius = 0.25
    positions_in_circle = 100

    alphas, beta_1s, beta_2s = joint_angles_for_circle_motion(robot, km_r, radius, positions_in_circle)
    plot_angles(alphas, beta_1s, beta_2s, positions_in_circle)

    reached_points = []
    for alpha, beta_1, beta_2 in zip(alphas, beta_1s, beta_2s):
        p = robot.forward_kinematics(alpha, beta_1, beta_2)
        reached_points.append(p[:3])

    plot_circle(reached_points)
