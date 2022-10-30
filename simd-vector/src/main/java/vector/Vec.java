package vector;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;

public class Vec {

    public DoubleVector e;

    public Vec() {
        e = DoubleVector.broadcast(DoubleVector.SPECIES_256, 0);
    }

    public Vec(DoubleVector simdVec) {
        this.e = simdVec;
    }

    public Vec(double e0, double e1, double e2) {
        e = DoubleVector.fromArray(DoubleVector.SPECIES_256, new double[]{e0, e1, e2, 0}, 0);
    }

    public void from(Vec vec) {
        this.e = vec.e;
    }

    public double x() {
        return this.e.lane(0);
    }

    public double y() {
        return this.e.lane(1);
    }

    public double z() {
        return this.e.lane(2);
    }

    public Vec negate() {
        return new Vec(this.e.neg());
    }

    public double index(int pos) {
        return this.e.lane(pos);
    }

    public Vec addAssign(final Vec v) {
        this.e = this.e.add(v.e);
        return this;
    }

    public Vec mulAssign(final double t) {
        this.e = this.e.mul(t);
        return this;
    }

    public Vec divAssign(final double t) {
        this.e = this.e.div(t);
        return this;
    }

    public double length() {
        return Math.sqrt(length_squared());
    }

    public double length_squared() {
        return this.e.mul(this.e).reduceLanes(VectorOperators.ADD);
    }

    public String toString() {
        return this.e.toString();
    }

    public Vec add(final Vec v) {
        return new Vec(this.e.add( v.e));
    }

    public Vec sub(final Vec v) {
        return new Vec(this.e.sub( v.e));
    }

    public Vec mul(final Vec v) {
        return new Vec(this.e.mul( v.e));
    }

    public Vec mul(double t) {
        return new Vec(this.e.mul(t));
    }

    public Vec div(double t) {
        return new Vec(this.e.div(t));
    }

    public double dot(final Vec v) {
        return this.e.mul(v.e).reduceLanes(VectorOperators.ADD);
    }

    public Vec cross(final Vec v) {
        return new Vec(index(1) * v.index(2) - index(2) * v.index(1),
                index(2) * v.index(0) - index(0) * v.index(2),
                index(0) * v.index(1) - index(1) * v.index(0));
    }

    public Vec unit_vector() {
        var denominator = Math.sqrt(this.e.mul(this.e).reduceLanes(VectorOperators.ADD));
        return new Vec(this.e.div(denominator));
    }

    public boolean nearZero() {
        // Return true if the vector is close to zero in all dimensions.
        final var s = 1e-8;
        var result = e.abs().lt(s);
        return result.laneIsSet(0) && result.laneIsSet(1) && result.laneIsSet(2);
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
            return this.e.equals(v.e) ;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.e.hashCode();
    }
}
