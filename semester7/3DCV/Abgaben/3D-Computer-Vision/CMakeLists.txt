cmake_minimum_required(VERSION 3.16)
project(Framework)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTORCC ON)

find_package(Qt6 REQUIRED COMPONENTS Core Gui Widgets OpenGL OpenGLWidgets)

set(CMAKE_PREFIX_PATH "C:/Qt/6.10.2/mingw_64")

include_directories(
    ${CMAKE_SOURCE_DIR}/GeneratedFiles
    ${CMAKE_SOURCE_DIR}/GeneratedFiles/Debug
    ${CMAKE_SOURCE_DIR}
    ${CMAKE_SOURCE_DIR}/external/eigen-3.4.0
    ${CMAKE_SOURCE_DIR}/external/freeglut/include
)

add_executable(Framework
    main.cpp
    mainwindow.cpp
    glwidget.cpp
    PointCloud.cpp
    GLConvenience.cpp
    QtConvenience.cpp
    Axes.cpp
    Cube.cpp
    Hexahedron.cpp
    Plane.cpp
    RenderCamera.cpp
    SceneManager.cpp
    SceneObject.cpp
    mainwindow.ui
)

target_link_libraries(Framework PRIVATE
    Qt6::Core
    Qt6::Gui
    Qt6::Widgets
    Qt6::OpenGL
    Qt6::OpenGLWidgets
    ${CMAKE_SOURCE_DIR}/external/freeglut/lib/x64/freeglut.lib
    opengl32
    glu32
)