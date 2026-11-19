#ifndef STEREOCAMERA_H
#define STEREOCAMERA_H

#include <vector>
#include <QVector3D>
#include "PerspectiveCamera.h"
#include "SceneObject.h"

class StereoCamera : public SceneObject
{
public:
    StereoCamera();
    ~StereoCamera(); // Destructor

    void draw(const RenderCamera& renderer, const QColor& color, float pointSize = 1.0f) const override;
    void affineMap(const QMatrix4x4& M) override;
    void reconstructAndDrawScene(const std::vector<SceneObject*>& sceneObjects, const RenderCamera& renderer) const;

    static QVector3D reconstructNormalCase(
        const PerspectiveCamera& cam1, const QVector3D& projPt1,
        const PerspectiveCamera& cam2, const QVector3D& projPt2,
        float b, float f, bool& isValid);

private:
    PerspectiveCamera* cam1;
    PerspectiveCamera* cam2;
    float b;
    float f;
};

#endif // STEREOCAMERA_H
