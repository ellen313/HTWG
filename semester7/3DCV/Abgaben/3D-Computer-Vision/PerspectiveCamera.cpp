#include "PerspectiveCamera.h"
#include "Hexahedron.h"
#include <iostream>

PerspectiveCamera::PerspectiveCamera(
    float pX, float pY, float pZ,
    float dX, float dY, float dZ,
    float uX, float uY, float uZ,
    float rX, float rY, float rZ,
    float fLen, float pWidth, float pHeight,
    float hX, float hY)
{
    type = SceneObjectType::ST_PERSPECTIVE_CAMERA;

    posX = pX; posY = pY; posZ = pZ;       // Standort
    dirX = dX; dirY = dY; dirZ = dZ;       // Blickrichtung
    upX = uX; upY = uY; upZ = uZ;          // Oben-Vektor
    rightX = rX; rightY = rY; rightZ = rZ; // Rechts-Vektor

    focallength = fLen;    // Brennweite/Abstand zur Bildebene
    planeWidth = pWidth;
    planeHeight = pHeight;

    // Bildhauptverschiebung
    hx = hX;
    hy = hY;
}

// Draw method implementation; Visualozation of camera
void PerspectiveCamera::draw(const RenderCamera& renderer, const QColor& color, float pointSize) const
{
    // Calculate center of image plane (principal point)
    // -> from camera position go 'focallength' wide in direction of view
    float optX = posX + dirX * focallength;
    float optY = posY + dirY * focallength;
    float optZ = posZ + dirZ * focallength;

    float px = optX + rightX * hx + upX * hy;
    float py = optY + rightY * hx + upY * hy;
    float pz = optZ + rightZ* hx + upZ * hy;

    float hw = planeWidth / 2.0f;
    float hh = planeHeight / 2.0f;

    // Calculate corners of image plane
    float tr_x = px + rightX * hw + upX * hh; // top right
    float tr_y = py + rightY * hw + upY * hh;
    float tr_z = pz + rightZ * hw + upZ * hh;

    float tl_x = px - rightX * hw + upX * hh; // top left
    float tl_y = py - rightY * hw + upY * hh;
    float tl_z = pz - rightZ * hw + upZ * hh;

    float bl_x = px - rightX * hw - upX * hh; // bottom left
    float bl_y = py - rightY * hw - upY * hh;
    float bl_z = pz - rightZ * hw - upZ * hh;

    float br_x = px + rightX * hw - upX * hh; // bottom right
    float br_y = py + rightY * hw - upY * hh;
    float br_z = pz + rightZ * hw - upZ * hh;

    // Create points for framework
    QVector3D center(posX, posY, posZ);
    QVector3D tr(tr_x, tr_y, tr_z);
    QVector3D tl(tl_x, tl_y, tl_z);
    QVector3D bl(bl_x, bl_y, bl_z);
    QVector3D br(br_x, br_y, br_z);

    // Draw frustum
    renderer.renderLine(center, tr, color, pointSize);
    renderer.renderLine(center, tl, color, pointSize);
    renderer.renderLine(center, bl, color, pointSize);
    renderer.renderLine(center, br, color, pointSize);

    // Draw image plane
    renderer.renderLine(tr, tl, color, pointSize);
    renderer.renderLine(tl, bl, color, pointSize);
    renderer.renderLine(bl, br, color, pointSize);
    renderer.renderLine(br, tr, color, pointSize);

    // Draw local coordinate system
    QVector3D rightPt(posX + rightX, posY + rightY, posZ + rightZ);
    QVector3D upPt(posX + upX, posY + upY, posZ + upZ);
    QVector3D dirPt(posX + dirX, posY + dirY, posZ + dirZ);

    renderer.renderLine(center, rightPt, Qt::red, pointSize); // X axis
    renderer.renderLine(center, upPt, Qt::green, pointSize);  // Y axis
    renderer.renderLine(center, dirPt, Qt::blue, pointSize);  // Z axis
}

void PerspectiveCamera::affineMap(const QMatrix4x4& M)
{
    QVector4D pos(posX, posY, posZ, 1.0f);
    pos = M * pos;
    posX = pos.x();
    posY = pos.y();
    posZ = pos.z();

    QVector4D dir(dirX, dirY, dirZ, 0.0f);
    dir = M * dir;
    dirX = dir.x(); dirY = dir.y(); dirZ = dir.z();

    QVector4D up(upX, upY, upZ, 0.0f);
    up = M * up;
    upX = up.x(); upY = up.y(); upZ = up.z();

    QVector4D right(rightX, rightY, rightZ, 0.0f);
    right = M * right;
    rightX = right.x(); rightY = right.y(); rightZ = right.z();
}

QVector3D PerspectiveCamera::projectPoint(const QVector3D& worldPoint, bool& isValid) const
{
    QVector3D center(posX, posY, posZ);
    QVector3D camDir(dirX, dirY, dirZ);
    QVector3D camRight(rightX, rightY, rightZ);
    QVector3D camUp(upX, upY, upZ);

    // Vector from camera to object
    QVector3D v = worldPoint - center;

    float z = QVector3D::dotProduct(v, camDir);

    // Clipping
    if (z <= 0.01f)
    {
        isValid = false;
        return QVector3D();
    }
    isValid = true;

    // Distances of the point from the camera's central axis
    float x = QVector3D::dotProduct(v, camRight);
    float y = QVector3D::dotProduct(v, camUp);

    // Position on image plane
    float u = (x / z) * focallength; // objects appear smaller the more far away
    float v_proj = (y / z) * focallength;

    isValid = true;

    QVector3D imageCenter = center + camDir * focallength;
    QVector3D projectedWorldPoint = imageCenter + camRight * u + camUp * v_proj;

    return projectedWorldPoint;

}

void PerspectiveCamera::projectAndDrawScene(const std::vector<SceneObject*>& sceneObjects, const RenderCamera& renderer) const
{

    std::cout << "projectAndDrawScene CALLED" << std::endl;

    for (SceneObject* obj : sceneObjects)
    {
        if (obj->getType() == SceneObjectType::ST_PERSPECTIVE_CAMERA)
        {
            continue;
        }

        const Hexahedron* hex = dynamic_cast<const Hexahedron*>(obj);
        if (!hex) continue;

        const auto& pts = hex->getVertices();

        static const unsigned edges[24] = {
            0,1, 1,2, 2,3, 3,0,
            4,5, 5,6, 6,7, 7,4,
            0,4, 1,5, 2,6, 3,7
        };

        QVector3D C(posX, posY, posZ);
        QVector3D F(dirX, dirY, dirZ);
        QVector3D R(rightX, rightY, rightZ);
        QVector3D U(upX, upY, upZ);

        // for each edge both points are projected on image plane of camera
        for (unsigned i = 0; i < 24; i += 2)
        {
            QVector3D A = pts[edges[i]];
            QVector3D B = pts[edges[i + 1]];

            bool validA, validB;

            QVector3D projA = projectPoint(A, validA);
            QVector3D projB = projectPoint(B, validB);

            // if both points are in front of camera, green line is drawn between projected points
            if (validA && validB)
                renderer.renderLine(projA, projB, Qt::green, 2.0f);
        }
    }
}