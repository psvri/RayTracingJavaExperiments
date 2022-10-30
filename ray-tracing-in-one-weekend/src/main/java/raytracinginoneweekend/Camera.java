package raytracinginoneweekend;

import vector.Vec;

public class Camera {

    private final Vec origin;
    private final Vec lowerLeftCorner;
    private final Vec horizontal;
    private final Vec vertical;
    private final Vec u;
    private final Vec v;
    private final Vec w;
    private final double lensRadius;

    public Camera(
            Vec lookfrom,
            Vec lookat,
            Vec vup,
            double vfov, // vertical field-of-view in degrees
            double aspectRatio,
            double aperture,
            double focusDist
    ) {
        var theta = Utils.degreesToRadians(vfov);
        var h = Math.tan(theta/2);
        var viewportHeight = 2.0 * h;
        var viewportWidth = aspectRatio * viewportHeight;

        /*var focal_length = 1.0;

        origin = new Vec(0, 0, 0);
        horizontal = new Vec(viewportWidth, 0.0, 0.0);
        vertical = new Vec(0.0, viewportHeight, 0.0);
        lowerLeftCorner = origin.sub(horizontal.div(2)).sub(vertical.div(2)).sub(new Vec(0, 0, focal_length));*/

        w = lookfrom.sub(lookat).unit_vector();
        u = vup.cross(w);
        v = w.cross(u);

        origin = lookfrom;
        horizontal = u.mul(viewportWidth).mul(focusDist);
        vertical = v.mul(viewportHeight).mul(focusDist);
        lowerLeftCorner = origin.sub(horizontal.div(2)).sub(vertical.div(2)).sub(w.mul(focusDist));

        lensRadius = aperture / 2;
    }

    public Ray getRay(double s, double t) {

        var rd = Utils.randomInUnitDisk().mul(lensRadius);
        var offset = u.mul(rd.x()).add(v.mul(rd.y()));

        return new Ray(origin.add(offset),
                lowerLeftCorner.add(horizontal.mul(s)).add(vertical.mul(t).sub(origin).sub(offset)));
    }
}
