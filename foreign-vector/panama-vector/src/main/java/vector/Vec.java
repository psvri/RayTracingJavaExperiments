package vector;

import java.util.Arrays;
import java.lang.Math;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySession;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

import rust.vector.lib_h;

import static rust.vector.lib_h.C_DOUBLE;

public class Vec {
    public MemorySegment rustVec;

    private static final GroupLayout STRUCT_LAYOUT = MemoryLayout
            .structLayout(MemoryLayout.sequenceLayout(3, C_DOUBLE).withName("vector3"));

    public Vec() {
        var segment = SegmentAllocator.implicitAllocator();
        this.rustVec = segment.allocate(STRUCT_LAYOUT);
    }

    public Vec(MemorySegment rustVec) {
        this.rustVec = rustVec;
    }

    public Vec(double e0, double e1, double e2) {
        var segment = SegmentAllocator.implicitAllocator();
        this.rustVec = segment.allocate(STRUCT_LAYOUT);
        lib_h.fill_with_values(this.rustVec, e0, e1, e2);
    }

    public void from(Vec vec) {
        this.rustVec = vec.rustVec;
    }

    public double x() {
        return lib_h.x(this.rustVec);
    }

    public double y() {
        return lib_h.y(this.rustVec);
    }

    public double z() {
        return lib_h.z(this.rustVec);
    }

    public Vec negate() {
        var newVec = new Vec();
        lib_h.neg_abi(this.rustVec, newVec.rustVec);
        return newVec;
    }

    public double index(int pos) {
        return lib_h.index_abi(this.rustVec, pos);
    }

    public Vec addAssign(final Vec v) {
        lib_h.add_assign_abi(this.rustVec, v.rustVec);
        return this;
    }

    public Vec mulAssign(final double t) {
        lib_h.mul_assign_abi(this.rustVec, t);
        return this;
    }

    public Vec divAssign(final double t) {
        lib_h.div_assign_abi(this.rustVec, t);
        return this;
    }

    public double length() {
        return lib_h.length(this.rustVec);
    }

    public double length_squared() {
        return lib_h.length_squared(this.rustVec);
    }

    public Vec add(final Vec v) {
        var newVec = new Vec();
        lib_h.add_abi(this.rustVec, v.rustVec, newVec.rustVec);
        return newVec;
    }

    public Vec sub(final Vec v) {
        var newVec = new Vec();
        lib_h.sub_abi(this.rustVec, v.rustVec, newVec.rustVec);
        return newVec;
    }

    public Vec mul(final Vec v) {
        var newVec = new Vec();
        lib_h.mul_abi(this.rustVec, v.rustVec, newVec.rustVec);
        return newVec;
    }

    public Vec mul(double t) {
        var newVec = new Vec();
        lib_h.mul_abi_double(this.rustVec, t, newVec.rustVec);
        return newVec;
    }

    public Vec div(double t) {
        var newVec = new Vec();
        lib_h.div_abi(this.rustVec, t, newVec.rustVec);
        return newVec;
    }

    public double dot(final Vec v) {
        return lib_h.dot(this.rustVec, v.rustVec);
    }

    public Vec cross(final Vec v) {
        var newVec = new Vec();
        lib_h.cross_abi(this.rustVec, v.rustVec, newVec.rustVec);
        return newVec;
    }

    public Vec unit_vector() {
        var newVec = new Vec();
        lib_h.unit_vector_abi(this.rustVec, newVec.rustVec);
        return newVec;
    }

    public boolean nearZero() {
        // Return true if the vector is close to zero in all dimensions.
        return lib_h.near_zero(this.rustVec);
    }

    public Vec reflect(final Vec n) {
        var newVec = new Vec();
        lib_h.reflect_abi(this.rustVec, n.rustVec, newVec.rustVec);
        return newVec;
    }

    public Vec refract(final Vec n, double etaiOverEtat) {
        var newVec = new Vec();
        lib_h.refract_abi(this.rustVec, n.rustVec, etaiOverEtat, newVec.rustVec);
        return newVec;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec v) {
            return lib_h.eq_abi(this.rustVec, v.rustVec);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return rustVec.hashCode();
    }
}
