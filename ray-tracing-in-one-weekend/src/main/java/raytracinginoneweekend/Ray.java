package raytracinginoneweekend;

import vector.Vec;

public class Ray {
    public Vec origin;
    public Vec direction;

    public Ray() {
        this.origin = new Vec();
        this.direction = new Vec();
    }

    public Ray(Vec origin, Vec direction) {
        this.origin = origin;
        this.direction = direction;
    }
    Vec at(double t) {
        return origin.add(direction.mul(t));
    }

    public Vec direction() {
        return direction;
    }

    public Vec origin() {
        return origin;
    }
}
