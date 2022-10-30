package raytracinginoneweekend;

import vector.Vec;

public class HitRecord {
    public Vec p;
    public Vec normal;
    public double t;
    public boolean frontFace;
    public Material material;

    public HitRecord() {
        p = new Vec();
        normal = new Vec();
        t = 0;
        frontFace = false;
    }

    public void setFaceNormal(final Ray r, final Vec outwardNormal) {
        frontFace = outwardNormal.dot(r.direction()) < 0;
        normal = frontFace ? outwardNormal : outwardNormal.negate();
    }
}
