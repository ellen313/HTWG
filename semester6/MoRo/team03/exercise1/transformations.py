# Funktionen zur Berechnung von Koordinatentransformationen in 2D und 3D
#
# M. Blaich; 13.10.2023;

import numpy as np


def rot(theta):
    #the parameter theta is already in radians
    rot_matr = np.array([
        [np.cos(theta), -np.sin(theta)],
        [np.sin(theta), np.cos(theta)]
     ])
    return rot_matr


def rotx(theta):
    rotx_matr = np.array([
        [1, 0, 0],
        [0, np.cos(theta), -np.sin(theta)],
        [0, np.sin(theta), np.cos(theta)]
    ])
    return rotx_matr


def roty(theta):
    roty_matr = np.array([
        [np.cos(theta), 0, np.sin(theta)],
        [0, 1, 0],
        [-np.sin(theta), 0, np.cos(theta)]
     ])
    return roty_matr


def rotz(theta):
    rotz_matr = np.array([
        [np.cos(theta), -np.sin(theta), 0],
        [np.sin(theta), np.cos(theta), 0],
        [0, 0, 1]
    ])
    return rotz_matr


def rot2trans(r):
    n = r.shape[0] # see if 2 or 3 dimensional (shape -> (2,2) or (2,2,2))
    Tf = np.identity(n + 1)
    Tf[:n, :n] = r # add rotation matrix to the upper left
    return Tf


def trans(t):
    t = np.array(t)
    n = len(t)
    Tl = np.identity(n + 1)
    Tl[:n, -1] = t # first n rows and last column
    return Tl
