#include "StereoCamera.h"
#include "PerspectiveCamera.h"
#include "Hexahedron.h"
#include <cmath>

// Stereo Normalfall: zwei identische Kameras die exakt parallel zueinander
// ausgerichtet sind und entlang der x Achse verschoben sind.
StereoCamera::StereoCamera()
{
    type = SceneObjectType::ST_STEREO_CAMERA;
    b = 2.0f; // baseline: Abstand zw. linken und rechten Kamera
    f = 2.0f; // focal length: Abstand der Bildebene vom Projektionszentrum

    // Linke
    cam1 = new PerspectiveCamera(
        0.0f, 0.0f, -5.0f,    // Position
        0.0f, 0.0f, 1.0f,     // Line of view (Z)
        0.0f, 1.0f, 0.0f,     // Up-Vector (Y)
        1.0f, 0.0f, 0.0f,     // Right-Vector (X)
        f,                    // Focal length
        3.0f, 2.0f,           // Width and height of image plane
        0.0f, 0.0f            // Image principal point
    );

    // 2nd camera (View from right, along x-axis toward the center)
    cam2 = new PerspectiveCamera(
        b, 0.0f, -5.0f,       // Position (translated with b on x)
        0.0f, 0.0f, 1.0f,     // Line of view (-X)
        0.0f, 1.0f, 0.0f,     // Up-Vector (Y)
        1.0f, 0.0f, 0.0f,     // Right-Vector (X)
        f,                    // Focal length
        3.0f, 2.0f,           // Width and height of image plane
        0.0f, 0.0f            // Image principal point offset
    );

    //task 3 - error propagation
    QMatrix4x4 error;
    error.rotate(-1.0f, 0.0f, 1.0f, 0.0f);
    cam2->affineMap(error);

}

StereoCamera::~StereoCamera()
{
    delete cam1;
    delete cam2;
}

void StereoCamera::draw(const RenderCamera& renderer, const QColor& color, float pointSize) const
{
    cam1->draw(renderer, color, pointSize);
    cam2->draw(renderer, color, pointSize);
}

void StereoCamera::affineMap(const QMatrix4x4& M)
{
    cam1->affineMap(M);
    cam2->affineMap(M);
}

// Rekonstruiert 3D-Punkt aus zwei 2D-Projektionen
QVector3D StereoCamera::reconstructNormalCase(
    const PerspectiveCamera& cam1, const QVector3D& projPt1, // Linke Kamera + 2D Punkt auf linker Bildebebene
    const PerspectiveCamera& cam2, const QVector3D& projPt2, // Rechte Kamera + 2D Punkt auf rechter Bildebebene
    float b, float f, bool& isValid)
{
    // 3D-Zentren der beiden Bildebenen (Position + Blickrichtung * Brennweite)
    QVector3D imageCenter1 = QVector3D(cam1.posX, cam1.posY, cam1.posZ) +
                             QVector3D(cam1.dirX, cam1.dirY, cam1.dirZ) * f;

    QVector3D imageCenter2 = QVector3D(cam2.posX, cam2.posY, cam2.posZ) +
                             QVector3D(cam2.dirX, cam2.dirY, cam2.dirZ) * f;

    QVector3D camRight(cam1.rightX, cam1.rightY, cam1.rightZ);
    QVector3D camUp(cam1.upX, cam1.upY, cam1.upZ);

    // Transformieren der globalen 3D-Punkte der Bildebene in lokale 2D-Bildkoordinaten
    float u1 = QVector3D::dotProduct(projPt1 - imageCenter1, camRight); // u = x-Koord. auf dem Bild
    float v1 = QVector3D::dotProduct(projPt1 - imageCenter1, camUp);    // v = y-Koord. auf dem Bild

    float u2 = QVector3D::dotProduct(projPt2 - imageCenter2, camRight);
    // float v2 = QVector3D::dotProduct(projPt2 - imageCenter2, camUp); // im Normalfall nicht benötigt, da v1 == v2 sein sollte

    // Disparität berechnen (Abstand zw. Punkt im linken und rechten Bild)
    float d = u1 - u2;

    if (std::abs(d) < 1e-4f) // wenn d nahe 0: Punkt unendlich weit weg
    {
        isValid = false;
        return QVector3D();
    }
    isValid = true;

    // Triangulation
    float Z = (f * b) / d; // Tiefe ausrechnen (Strahlensatz)
    float X = (u1 * Z) / f; // Lokale x-Pos rekonstruieren
    float Y = (v1 * Z) / f; // Lokale y-Pos rekonstruieren

    // Zurück transformieren der lokalen Koord. ins globale 3D-WeltKS
    QVector3D cam1Pos(cam1.posX, cam1.posY, cam1.posZ);
    QVector3D camDir(cam1.dirX, cam1.dirY, cam1.dirZ);

    QVector3D worldPoint = cam1Pos + camRight * X + camUp * Y + camDir * Z;

    return worldPoint;
}

void StereoCamera::reconstructAndDrawScene(const std::vector<SceneObject*>& sceneObjects, const RenderCamera& renderer) const
{
    for (SceneObject* obj : sceneObjects)
    {
        const Hexahedron* hex = dynamic_cast<const Hexahedron*>(obj);
        if (!hex) continue;

        const auto& pts = hex->getVertices();

        static const unsigned edges[24] = {
            0,1, 1,2, 2,3, 3,0,
            4,5, 5,6, 6,7, 7,4,
            0,4, 1,5, 2,6, 3,7
        };

        // Projizierten 2D Linien auf Bildebene der Kamera zeichnen
        for (unsigned i = 0; i < 24; i += 2)
        {
            QVector3D A = pts[edges[i]];
            QVector3D B = pts[edges[i + 1]];

            bool validA1, validB1, validA2, validB2;

            // Projizieren der Endpunkte der Kante in linke Kamera
            QVector3D projA1 = cam1->projectPoint(A, validA1);
            QVector3D projB1 = cam1->projectPoint(B, validB1);

            if (validA1 && validB1)
            {
                renderer.renderLine(projA1, projB1, Qt::green, 2.0f);
            }

            // Projizieren der Endpunkte der Kante in rechte Kamera
            QVector3D projA2 = cam2->projectPoint(A, validA2);
            QVector3D projB2 = cam2->projectPoint(B, validB2);

            if (validA2 && validB2)
            {
                renderer.renderLine(projA2, projB2, Qt::green, 2.0f);
            }

        }

        // Orginalen 3D-Punkte aus den 2D-Projektionen zurück berechnen und zeichnen
        for (const QVector3D& originalPoint : pts)
        {
            bool valid1, valid2;

            // Projizieren des Punktes auf beide Kameras
            QVector3D proj1 = cam1->projectPoint(originalPoint, valid1);
            QVector3D proj2 = cam2->projectPoint(originalPoint, valid2);

            if (valid1 && valid2)
            {
                bool validRecon;

                // Stereo-Reksontruktion durchführen
                QVector3D reconstructed = reconstructNormalCase(*cam1, proj1, *cam2, proj2, b, f, validRecon);

                if (validRecon)
                {
                    // 3D-Punkt zeichnen
                    renderer.renderPoint(reconstructed, Qt::magenta, 10.0f);
                }
            }
        }
    }
}
