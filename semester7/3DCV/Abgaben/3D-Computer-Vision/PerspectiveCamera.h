#ifndef PERSPECTIVECAMERA_H
#define PERSPECTIVECAMERA_H

#include "SceneObject.h"
#include "Hexahedron.h"
#include <vector>
#include <iostream>

class PerspectiveCamera: public SceneObject
{
public:
    float posX, posY, posZ;         // Center of projection
    float dirX, dirY, dirZ;         // Line of sight (-Z of camera)
    float upX, upY, upZ;            // Up-Vector (Y of camera)
    float rightX, rightY, rightZ;   // Right-Vector (X of camera

    float focallength;              // Focal length (Brennweite)
    float planeWidth, planeHeight;  // Image plane size
    float hx, hy;                   // Bildhauptverschiebung

public:
    PerspectiveCamera(
        float pX, float pY, float pZ,
        float dX, float dY, float dZ,
        float uX, float uY, float uZ,
        float rX, float rY, float rZ,
        float fLen, float pWidth, float pHeight,
        float hX, float hY);

    void draw(const RenderCamera& renderer, const QColor& color, float pointSize) const override;

    void affineMap(const QMatrix4x4& map) override;

public:
    QVector3D projectPoint(const QVector3D& worldPoint, bool& isValid) const;
    void projectAndDrawScene(const std::vector<SceneObject*>& sceneObjects, const RenderCamera& renderer) const;

};

#endif // PERSPECTIVECAMERA_H
