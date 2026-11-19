# ----------------------------------------------------
# Project file for Framework
# ----------------------------------------------------

TEMPLATE = app
TARGET = Framework

# Qt modules
QT += core gui widgets opengl openglwidgets

# Build configuration
CONFIG += debug c++20
DEFINES += QT_DLL QT_OPENGL_LIB QT_WIDGETS_LIB

# Include paths
INCLUDEPATH += $$PWD/GeneratedFiles \
               $$PWD/GeneratedFiles/Debug \
               $$PWD \
               $$PWD/external/eigen-3.4.0 \
               $$PWD/external/freeglut/include

# Library paths and linking
LIBS += -L$$PWD/external/freeglut/lib/x64 -lfreeglut -lopengl32 -lglu32

# Generated files directories
MOC_DIR += $$PWD/GeneratedFiles/debug
OBJECTS_DIR += $$PWD/debug
UI_DIR += $$PWD/GeneratedFiles
RCC_DIR += $$PWD/GeneratedFiles

# Source, header, and form files
HEADERS += $$PWD/glwidget.h \
           $$PWD/mainwindow.h \
           $$PWD/PointCloud.h \
           $$PWD/GLConvenience.h \
           $$PWD/QtConvenience.h \
           $$PWD/Axes.h \
           $$PWD/Cube.h \
           $$PWD/Hexahedron.h \
           $$PWD/Plane.h \
           $$PWD/RenderCamera.h \
           $$PWD/SceneManager.h \
           $$PWD/SceneObject.h \
           PerspectiveCamera.h \
           StereoCamera.h

SOURCES += $$PWD/glwidget.cpp \
           $$PWD/mainwindow.cpp \
           $$PWD/main.cpp \
           $$PWD/PointCloud.cpp \
           $$PWD/GLConvenience.cpp \
           $$PWD/QtConvenience.cpp \
           $$PWD/Axes.cpp \
           $$PWD/Cube.cpp \
           $$PWD/Hexahedron.cpp \
           $$PWD/Plane.cpp \
           $$PWD/RenderCamera.cpp \
           $$PWD/SceneManager.cpp \
           $$PWD/SceneObject.cpp \
           PerspectiveCamera.cpp \
           StereoCamera.cpp

FORMS += $$PWD/mainwindow.ui

# Dependency path
DEPENDPATH += $$PWD