package raytracinginoneweekend;

import vector.Vec;

public record Sphere(Vec center, double radius, Material material) implements Hittable {

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        Vec oc = r.origin().sub(center);
        var a = r.direction().length_squared();
        var half_b = r.direction().dot(oc);
        var c = oc.length_squared() - radius*radius;

        var discriminant = half_b*half_b - a*c;
        if (discriminant < 0) return false;
        var sqrtd = Math.sqrt(discriminant);

        // Find the nearest root that lies in the acceptable range.
        var root = (-half_b - sqrtd) / a;
        if (root < tMin || tMax < root) {
            root = (-half_b + sqrtd) / a;
            if (root < tMin || tMax < root)
                return false;
        }

        rec.t = root;
        rec.p = r.at(rec.t);
        Vec outward_normal = rec.p.sub(center).div(radius);
        rec.setFaceNormal(r, outward_normal);
        rec.material = material;

        return true;
    }
}
