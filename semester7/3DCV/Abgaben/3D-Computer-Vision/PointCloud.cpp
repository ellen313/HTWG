#include "PointCloud.h"

#include <fstream>
#include <sstream>
#include <iostream>
#include <algorithm>
#include <math.h>
#include <Eigen/Dense>

using namespace std;

PointCloud::PointCloud()
{
    type      = SceneObjectType::ST_POINT_CLOUD;
    pointSize = 3.0f;
}

PointCloud::~PointCloud()
{}


bool PointCloud::loadPLY(const QString& filePath)
{
    // open stream
    fstream is;
    is.open(filePath.toStdString().c_str(), fstream::in);

    // ensure format with magic header
    string line;
    getline(is, line);
    if (line != "ply") throw runtime_error("not a ply file");

    // parse header looking only for 'element vertex' section size
    // to know the amount of points
    unsigned pointsCount = 0;
    while (is.good()) {
        getline(is, line);
        if (line == "end_header") {
            break;
        } else {
            stringstream ss(line);
            string tag1, tag2, tag3;
            ss >> tag1 >> tag2 >> tag3;
            if (tag1 == "element" && tag2 == "vertex") {
                pointsCount = unsigned(atoi(tag3.c_str()));
            }
        }
    }

    // read and parse 'element vertex' section
    if (pointsCount > 0) {
        this->resize(pointsCount);
        float m = float(INT_MAX);
        pointsBoundMin = QVector3D( m, m, m);
        pointsBoundMax = QVector3D(-m,-m,-m);

        stringstream ss;
        string line;
        QVector4D *p = this->data();

        // reads x, y, z per line and stores them as QVector4D(x, y, z, 1.0)
        for (size_t i = 0; is.good() && i < pointsCount; ++i) {
            getline(is, line);
            ss.str(line);
            float x, y, z;
            ss >> x >> y >> z;

            *p++ = QVector4D(x, y, z, 1.0);

            // updates for AABB
            pointsBoundMax[0] = max(x, pointsBoundMax[0]);
            pointsBoundMax[1] = max(y, pointsBoundMax[1]);
            pointsBoundMax[2] = max(z, pointsBoundMax[2]);
            pointsBoundMin[0] = min(x, pointsBoundMin[0]);
            pointsBoundMin[1] = min(y, pointsBoundMin[1]);
            pointsBoundMin[2] = min(z, pointsBoundMin[2]);
        }

        // basic validation
        if (p - this->data() < (int)size()) throw runtime_error("broken ply file");

        cout << "number of points: " + to_string(pointsCount) << endl;

        // rescale data
        float a, s = 0;
        for (int i = 0; i < 3; i++) {
            a  = pointsBoundMax[i] - pointsBoundMin[i];
            s += a * a;
        }
        s = sqrt(s) / pointCloudScale;
        for (auto& p : *this) { p /= s; p[3] = 1.0; }
        //  for (int i=0; i < size(); i++) { (*this)[i]/=s; (*this)[i][3] = 1.0; }

        //scale AABB (Axis Aligned Bounding Box, Min/Max per axis)
        pointsBoundMin /= s; // normalize
        pointsBoundMax /= s;

        //building the trees after loading
        buildKDTree();
        buildOctTree();
    }
    return true;
}

void PointCloud::setPointSize(unsigned _pointSize)
{
    pointSize = _pointSize;
}

void PointCloud::affineMap(const QMatrix4x4& M)
{
    for (auto& p : *this) p = M.map(p);
}

void PointCloud::draw(const RenderCamera& camera, const QColor& color, float) const
{
    camera.renderPCL((*this), color, pointSize);
}


void PointCloud::drawBBox(const QVector3D& mn,
                          const QVector3D& mx,
                          const QColor&    color,
                          const RenderCamera& renderer) const
{
    //8 corners
    QVector3D c[8] = {
        {mn.x(), mn.y(), mn.z()},
        {mx.x(), mn.y(), mn.z()},
        {mx.x(), mx.y(), mn.z()},
        {mn.x(), mx.y(), mn.z()},
        {mn.x(), mn.y(), mx.z()},
        {mx.x(), mn.y(), mx.z()},
        {mx.x(), mx.y(), mx.z()},
        {mn.x(), mx.y(), mx.z()}
    };
    static const int edges[24] = {
        0,1, 1,2, 2,3, 3,0,   // bottom face
        4,5, 5,6, 6,7, 7,4,   // top face
        0,4, 1,5, 2,6, 3,7    // verticals
    };
    for (int i = 0; i < 24; i += 2)
        renderer.renderLine(c[edges[i]], c[edges[i+1]], color, 1.0f);
}

// A KD-tree recursively divides the space along an axis — alternating between the longest dimensions
void PointCloud::buildKDTree()
{
    vector<int> allIndices(this->size());
    for (int i = 0; i < (int)this->size(); ++i) allIndices[i] = i;

    //rekursive call
    kdRoot = buildKDNode(allIndices, pointsBoundMin, pointsBoundMax, 0);
    cout << "kd-tree built." << endl;
}

unique_ptr<KDNode> PointCloud::buildKDNode(vector<int>& idx,
                                           const QVector3D& bmin,
                                           const QVector3D& bmax,
                                           int depth)
{
    if (idx.empty()) return nullptr;

    //new node
    auto node       = make_unique<KDNode>();
    node->bboxMin   = bmin;
    node->bboxMax   = bmax;


    const int LEAF_SIZE = 10;
    //create node
    if ((int)idx.size() <= LEAF_SIZE) {
        node->indices  = idx;
        node->splitAxis  = 0;
        node->splitValue = 0.0f;
        return node;
    }

    //heuristic: longest axis
    QVector3D extent = bmax - bmin;
    int axis = 0;
    if (extent.y() > extent.x()) axis = 1;
    if (extent.z() > (axis == 0 ? extent.x() : extent.y())) axis = 2;
    //save splitaxis in node
    node->splitAxis = axis;

    //median split, sort points, take mid
    sort(idx.begin(), idx.end(), [&](int a, int b) {
        return (*this)[a][axis] < (*this)[b][axis];
    });

    // 2 Listen: x und y
    // -> Wir gehen die y Koordinaten durch (von dem x Median) und schauen: ist der y Wert größer oder kleiner als der X median
    // -> macht man für alle und kopiert die liste zurück
    // sort muss hoch gezogen werden für alle achsen dann einmal
    // aktueller Ansatz ist n²log; soll aber n²

    size_t mid = idx.size() / 2;
    node->splitValue = (*this)[idx[mid]][axis];

    //AABB split
    //left: bmin -> splitValue (upper limit)
    QVector3D leftMax  = bmax;
    //left: splitValue ->  bmax  (lower limit)
    QVector3D rightMin = bmin;

    if (axis == 0) { leftMax.setX(node->splitValue); rightMin.setX(node->splitValue); }
    if (axis == 1) { leftMax.setY(node->splitValue); rightMin.setY(node->splitValue); }
    if (axis == 2) { leftMax.setZ(node->splitValue); rightMin.setZ(node->splitValue); }

    //recursion
    vector<int> leftIdx (idx.begin(), idx.begin() + mid);
    vector<int> rightIdx(idx.begin() + mid, idx.end());

    //child nodes
    node->left  = buildKDNode(leftIdx,  bmin,     leftMax,  depth + 1);
    node->right = buildKDNode(rightIdx, rightMin, bmax,     depth + 1);

    return node;
}

void PointCloud::drawKDNode(const KDNode* node,
                            const RenderCamera& renderer,
                            int currentDepth,
                            int maxDepth) const
{
    if (!node || currentDepth > maxDepth) return;

    static const QColor levelColors[3] = {
        QColor(255,  60,  60),   // level 0 – red
        QColor(255, 165,   0),   // level 1 – orange
        QColor(255, 255,   0)    // level 2 – yellow
    };

    //selct color depending on depth
    QColor color = (currentDepth < 3) ? levelColors[currentDepth]
                                      : QColor(200, 200, 200);

    drawBBox(node->bboxMin, node->bboxMax, color, renderer);

    //max depth?
    if (currentDepth < maxDepth) {
        drawKDNode(node->left.get(),  renderer, currentDepth + 1, maxDepth);
        drawKDNode(node->right.get(), renderer, currentDepth + 1, maxDepth);
    }
}

//start point fpr visualization
void PointCloud::drawKDTree(const RenderCamera& renderer, int maxDepth) const
{
    drawKDNode(kdRoot.get(), renderer, 0, maxDepth - 1);
}


// An octree divides all three axes simultaneously → 8 child nodes (hence "oct")
void PointCloud::buildOctTree()
{
    vector<int> allIndices(this->size());
    for (int i = 0; i < (int)this->size(); ++i) allIndices[i] = i;

    octRoot = buildOctNode(allIndices, pointsBoundMin, pointsBoundMax, 0);
    cout << "oct-tree built." << endl;
}

unique_ptr<OctNode> PointCloud::buildOctNode(vector<int>& idx,
                                             const QVector3D& bmin,
                                             const QVector3D& bmax,
                                             int depth)
{
    if (idx.empty()) return nullptr;

    auto node     = make_unique<OctNode>();
    node->bboxMin = bmin;
    node->bboxMax = bmax;

    const int LEAF_SIZE = 10;
    if ((int)idx.size() <= LEAF_SIZE) {
        node->indices = idx;
        return node;
    }

    QVector3D mid = (bmin + bmax) * 0.5f;


    vector<int> childIdx[8];
    for (int i : idx) {
        const QVector4D& p = (*this)[i];
        int octant = 0;
        // Together, these three bits form a number between 0 and 7 — the octant index
        if (p.x() > mid.x()) octant |= 1; // 001
        if (p.y() > mid.y()) octant |= 2; // 010
        if (p.z() > mid.z()) octant |= 4; // 100
        childIdx[octant].push_back(i);
    }

    for (int o = 0; o < 8; ++o) {
        if (childIdx[o].empty()) continue;

        QVector3D cmin = bmin;
        QVector3D cmax = bmax;
        if (o & 1) cmin.setX(mid.x()); else cmax.setX(mid.x());
        if (o & 2) cmin.setY(mid.y()); else cmax.setY(mid.y());
        if (o & 4) cmin.setZ(mid.z()); else cmax.setZ(mid.z());

        node->children[o] = buildOctNode(childIdx[o], cmin, cmax, depth + 1);
    }

    return node;
}

void PointCloud::drawOctNode(const OctNode* node,
                             const RenderCamera& renderer,
                             int currentDepth,
                             int maxDepth) const
{
    if (!node || currentDepth > maxDepth) return;

    static const QColor levelColors[3] = {
        QColor(  0, 220, 220),   // level 0 – cyan
        QColor(220,   0, 220),   // level 1 – magenta
        QColor( 60,  60, 255)    // level 2 – blue
    };
    QColor color = (currentDepth < 3) ? levelColors[currentDepth]
                                      : QColor(200, 200, 200);

    drawBBox(node->bboxMin, node->bboxMax, color, renderer);

    if (currentDepth < maxDepth) {
        for (auto& child : node->children)
            drawOctNode(child.get(), renderer, currentDepth + 1, maxDepth);
    }
}

void PointCloud::drawOctTree(const RenderCamera& renderer, int maxDepth) const
{
    drawOctNode(octRoot.get(), renderer, 0, maxDepth - 1);
}


PCAResult PointCloud::computePCA() const
{
    PCAResult result;

    const int n = static_cast<int>(QVector<QVector4D>::size());
    if (n == 0) return result;

    // Centroid
    Eigen::Vector3d centroid = Eigen::Vector3d::Zero();
    for (int i = 0; i < n; ++i)
    {
        const QVector4D& p = QVector<QVector4D>::operator[](i);
        centroid(0) += p.x();
        centroid(1) += p.y();
        centroid(2) += p.z();
    }
    centroid /= static_cast<double>(n);

    result.centroid = QVector3D(
        float(centroid(0)),
        float(centroid(1)),
        float(centroid(2)));

    // build 3D covariance matrix C = (1/n) Σ (pᵢ−μ)(pᵢ−μ)ᵀ
    Eigen::Matrix3d C = Eigen::Matrix3d::Zero();
    for (int i = 0; i < n; ++i)
    {
        const QVector4D& p = QVector<QVector4D>::operator[](i);
        Eigen::Vector3d d;
        d(0) = p.x() - centroid(0);
        d(1) = p.y() - centroid(1);
        d(2) = p.z() - centroid(2);
        C += d * d.transpose();
    }
    C /= static_cast<double>(n);

    // Symmetric eigen decomposition
    // Returns eignvalues in ascending order
    Eigen::SelfAdjointEigenSolver<Eigen::Matrix3d> solver(C);

    // solver.eigenvalues() -> ascending vector [λ₃, λ₂, λ₁]
    // solver.eigenvectors() -> columns are the corresponding unit vectors

    const Eigen::Vector3d& vals = solver.eigenvalues();
    const Eigen::Matrix3d& vecs = solver.eigenvectors();

    // Store in descending order (index 2 = largest)
    for (int k = 0; k < 3; ++k)
    {
        int src = 2 - k; // 2,1,0 -> larger first
        result.eigenvalues[k] = float(vals[src]);
        result.eigenvectors[k] = QVector3D(
            float(vecs(0, src)),
            float(vecs(1, src)),
            float(vecs(2, src)));
    }

    pcaResult = result;
    pcaComputed = true;

    // Print
    cout << "[PCA centroid: ("
         << result.centroid.x() << ", "
         << result.centroid.y() << ", "
         << result.centroid.z() << ")" << endl;

    for (int k = 0; k < 3; ++k)
    {
        cout << "[PCA] lambda" << k+1 << " = " << result.eigenvalues[k]
             << "  v = ("
             << result.eigenvectors[k].x() << ", "
             << result.eigenvectors[k].y() << ", "
             << result.eigenvectors[k].z() << ")" << endl;
    }

    return result;
}


void PointCloud::drawPCA(const RenderCamera& renderer) const
{
    if (!pcaComputed)
    {
        pcaResult = computePCA();
        pcaComputed = true;
    }

    const PCAResult& r = pcaResult;

    // Arrow length = viaScale * sqrt(eigenvalue) = viaScale * stddev along axis
    const float viaScale = 3.0f;

    static const QColor axisColors[3] = {
        QColor(255,  40,  40),   // lambda1 - red   - primary axis (most variance)
        QColor(  0, 200,  60),   // lambda2 - green - secondary axis
        QColor(  0, 100, 255)    // lambda3 - blue  - least variance
    };

    for (int k = 0; k < 3; ++k)
    {
        float len = viaScale * std::sqrt(std::max(0.0f, r.eigenvalues[k]));
        QVector3D tip = r.centroid + len * r.eigenvectors[k];
        renderer.renderLine(r.centroid, tip, axisColors[k], 3.0f);
    }
}


void PointCloud::alignTo(PointCloud& target)
{
    cout << "[Align] Source points: " << QVector<QVector4D>::size() << endl;
    cout << "[Align] Target points: " << target.QVector<QVector4D>::size() << endl;

    if (QVector<QVector4D>::size() == 0 || target.QVector<QVector4D>::size() == 0) {
        cout << "[Align] ERROR: empty point cloud, aborting." << endl;
        return;
    }

    // ── 1. Compute PCA for both clouds ───────────────────────────────────────
    PCAResult r1 = this->computePCA();
    PCAResult r2 = target.computePCA();

    cout << "[Align] PCA source centroid:  ("
         << r1.centroid.x() << ", " << r1.centroid.y() << ", " << r1.centroid.z() << ")" << endl;
    cout << "[Align] PCA target centroid:  ("
         << r2.centroid.x() << ", " << r2.centroid.y() << ", " << r2.centroid.z() << ")" << endl;

    // Build eigen matrices
    Eigen::Matrix3d V1, V2;
    for (int k = 0; k < 3; ++k)
    {
        V1(0,k) = r1.eigenvectors[k].x();
        V1(1,k) = r1.eigenvectors[k].y();
        V1(2,k) = r1.eigenvectors[k].z();

        V2(0,k) = r2.eigenvectors[k].x();
        V2(1,k) = r2.eigenvectors[k].y();
        V2(2,k) = r2.eigenvectors[k].z();
    }

    // Fix sign ambiguity -> if eigenvector k of V1 points away from the
    // corresponding one in V2, flip it
    for (int k = 0; k < 3; ++k)
    {
        if (V1.col(k).dot(V2.col(k)) < 0.0)
            V1.col(k) = -V1.col(k);
    }

    // Rotation (maps V1's frame on V2's frame)
    Eigen::Matrix3d R = V2 * V1.transpose();

    // Build QMatrix4x4 for full transform
    Eigen::Vector3d c1(r1.centroid.x(), r1.centroid.y(), r1.centroid.z());
    Eigen::Vector3d c2(r2.centroid.x(), r2.centroid.y(), r2.centroid.z());
    Eigen::Vector3d t = c2 - R * c1; // translation part

    QMatrix4x4 M(
        float(R(0,0)), float(R(0,1)), float(R(0,2)), float(t(0)),
        float(R(1,0)), float(R(1,1)), float(R(1,2)), float(t(1)),
        float(R(2,0)), float(R(2,1)), float(R(2,2)), float(t(2)),
        0.0f,         0.0f,          0.0f,          1.0f
    );

    // Apply tranform to every point in this
    this->affineMap(M);

    // Invalidate cached PCA so it gets recomputed after alignment
    pcaComputed = false;

    cout << "[Align] Translation: ("
         << t(0) << ", " << t(1) << ", " << t(2) << ")" << endl;
    cout << "[Align] Rotation matrix R:" << endl;
    for (int i = 0; i < 3; ++i)
        cout << " " << R(i,0) << " " << R(i,1) << " " << R(i,2) << endl;
}


void PointCloud::saveTransformedPLY(const QString& filePath,
                                    float angleDegrees,
                                    QVector3D translation) const
{
    // Build rotation matrix around Y axis
    float a = qDegreesToRadians(angleDegrees);
    QMatrix4x4 R;
    R.setToIdentity();
    R.rotate(angleDegrees, 0.0f, 1.0f, 0.0f);

    QMatrix4x4 T;
    T.setToIdentity();
    T.translate(translation);

    QMatrix4x4 M = T * R;

    // Write PLY
    std::ofstream out(filePath.toStdString());
    out << "ply\n";
    out << "format ascii 1.0\n";
    out << "element vertex " << this->size() << "\n";
    out << "property float x\n";
    out << "property float y\n";
    out << "property float z\n";
    out << "end_header\n";

    for (int i = 0; i < (int)this->size(); ++i)
    {
        QVector4D p = M.map(QVector<QVector4D>::operator[](i));
        out << p.x() << " " << p.y() << " " << p.z() << "\n";
    }

    cout << "[Save] Written transformed PLY to: "
         << filePath.toStdString() << endl;
}
