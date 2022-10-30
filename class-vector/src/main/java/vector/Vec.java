package vector;

import java.util.Arrays;
import java.lang.Math;

public class Vec {
    public double[] e;

    public Vec() {
        this.e = new double[] {0, 0, 0};
    }

    public Vec(double e0, double e1, double e2) {
        this.e = new double[] {e0, e1, e2};
    }

    public void from(Vec vec) {
        this.e = vec.e;
    }

    public double x() {
        return this.e[0];
    }

    public double y() {
        return this.e[1];
    }

    public double z() {
        return this.e[2];
    }

    public Vec negate() {
        return new Vec(-e[0], -e[1], -e[2]);
    }

    public double index(int pos) {
        return e[pos];
    }

    public Vec addAssign(final Vec v) {
        this.e[0] += v.e[0];
        this.e[1] += v.e[1];
        this.e[2] += v.e[2];
        return this;
    }

    public Vec mulAssign(final double t) {
        this.e[0] *= t;
        this.e[1] *= t;
        this.e[2] *= t;
        return this;
    }

    public Vec divAssign(final double t) {
        return this.mulAssign(1/t);
    }

    public double length() {
        return Math.sqrt(length_squared());
    }

    public double length_squared() {
        return e[0]*e[0] + e[1]*e[1] + e[2]*e[2];
    }

    public String toString() {
        return Arrays.toString(this.e);
    }

    public Vec add(final Vec v) {
        return new Vec(e[0] + v.e[0], e[1] + v.e[1], e[2] + v.e[2]);
    }

    public Vec sub(final Vec v) {
        return new Vec(e[0] - v.e[0], e[1] - v.e[1], e[2] - v.e[2]);
    }

    public Vec mul(final Vec v) {
        return new Vec(e[0] * v.e[0], e[1] * v.e[1], e[2] * v.e[2]);
    }

    public Vec mul(double t) {
        return new Vec(t*e[0], t*e[1], t*e[2]);
    }

    public Vec div(double t) {
        return mul(1/t);
    }

    public double dot(final Vec v) {
        return e[0] * v.e[0]
                + e[1] * v.e[1]
                + e[2] * v.e[2];
    }

    public Vec cross(final Vec v) {
        return new Vec(e[1] * v.e[2] - e[2] * v.e[1],
                e[2] * v.e[0] - e[0] * v.e[2],
                e[0] * v.e[1] - e[1] * v.e[0]);
    }

    public Vec unit_vector() {
        return div(length());
    }

    public boolean nearZero() {
        // Return true if the vector is close to zero in all dimensions.
        final var s = 1e-8;
        return (Math.abs(e[0]) < s) && (Math.abs(e[1]) < s) && (Math.abs(e[2]) < s);
    }

    public Vec reflect(final Vec n) {
        return this.sub(n.mul(dot(n) * 2));
    }

    public Vec refract(final Vec n, double etaiOverEtat) {
        var cosTheta = Math.min(negate().dot(n), 1.0);
        Vec rOutPerp = add(n.mul(cosTheta)).mul(etaiOverEtat);
        Vec rOutParallel = n.mul(-Math.sqrt(Math.abs(1.0 - rOutPerp.length_squared())));
        return rOutPerp.add(rOutParallel);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec v) {
            return Arrays.equals(this.e, v.e);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return e.hashCode();
    }
}
