//
// (c) Georg Umlauf, 2022
//
#pragma once

#include "SceneObject.h"
#include "RenderCamera.h"
#include <memory>
#include <vector>
#include <Eigen/Dense>

//Node for KdTree
struct KDNode {
    int   splitAxis;    //splitting axis
    float splitValue;   //median value along splitAxis

    //needed to visualize the partition
    QVector3D bboxMin;
    QVector3D bboxMax;

    //pointer
    std::unique_ptr<KDNode> left;
    std::unique_ptr<KDNode> right;

    std::vector<int> indices;       //indices of leaf nodes

    bool isLeaf() const { return !left && !right; }
};


//Node for OctTree
struct OctNode {
    QVector3D bboxMin;
    QVector3D bboxMax;

    //8 children or empty
    std::unique_ptr<OctNode> children[8];

    std::vector<int> indices;   //indices of leaf nodes

    bool isLeaf() const {
        for (auto& c : children) if (c) return false;
        return true;
    }
};


struct PCAResult
{
    QVector3D centroid;         // mean of all points
    QVector3D eigenvalues;      // λ₁ ≥ λ₂ ≥ λ₃ (variance along each axis)
    QVector3D eigenvectors[3];  // corresponding unit eigenvectors
};


//PointCloud
class PointCloud: public SceneObject, public QVector<QVector4D>
{
private:
    QVector3D    pointsBoundMin;
    QVector3D    pointsBoundMax;

    unsigned     pointSize       = 3;
    const float  pointCloudScale = 1.5f;

    //roots
    std::unique_ptr<KDNode>  kdRoot;
    std::unique_ptr<OctNode> octRoot;

    //kdTree helper methods
    std::unique_ptr<KDNode> buildKDNode(std::vector<int>& idx,
                                        const QVector3D& bmin,
                                        const QVector3D& bmax,
                                        int depth);

    void drawKDNode(const KDNode* node,
                    const RenderCamera& renderer,
                    int currentDepth,
                    int maxDepth) const;

    //octTree helper methods
    std::unique_ptr<OctNode> buildOctNode(std::vector<int>& idx,
                                          const QVector3D& bmin,
                                          const QVector3D& bmax,
                                          int depth);

    void drawOctNode(const OctNode* node,
                     const RenderCamera& renderer,
                     int currentDepth,
                     int maxDepth) const;

    //shared helper
    void drawBBox(const QVector3D& bmin,
                  const QVector3D& bmax,
                  const QColor& color,
                  const RenderCamera& renderer) const;

    mutable PCAResult pcaResult;
    mutable bool pcaComputed = false;

public:
    PointCloud();
    virtual ~PointCloud();

    bool loadPLY(const QString&);

    virtual void affineMap(const QMatrix4x4&) override;
    virtual void draw     (const RenderCamera& camera,
                      const QColor&       color      = COLOR_POINT_CLOUD,
                      float               point_size = 3.0f) const override;

    QVector3D getMin() const { return pointsBoundMin; }
    QVector3D getMax() const { return pointsBoundMax; }

    // tree build
    void buildKDTree();
    void buildOctTree();

    // tree accessors
    const KDNode*  getKDRoot()  const { return kdRoot.get();  }
    const OctNode* getOctRoot() const { return octRoot.get(); }

    // visualize first N levels of each tree
    void drawKDTree (const RenderCamera& renderer, int maxDepth = 3) const;
    void drawOctTree(const RenderCamera& renderer, int maxDepth = 3) const;

    // setup point size
    void     setPointSize(unsigned s);
    unsigned getPointSize(          ) const { return pointSize; }

    // compute PCA
    PCAResult computePCA() const;
    void drawPCA(const RenderCamera& renderer) const;

    // PCA-based alignment
    void alignTo(PointCloud& target);

    void saveTransformedPLY(const QString& filePath,
                            float angleDegree,
                            QVector3D translation) const;
};
