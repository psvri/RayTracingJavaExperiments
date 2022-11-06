package vector;

import org.junit.jupiter.api.Test;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySession;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.Math;

public class VecTest {

    @Test
    void testVecInit() {
        var vec1 = new Vec();
        assertEquals(vec1, new Vec(0, 0, 0));

        vec1 = new Vec(1, 2, 3);
        assertEquals(vec1.x(), 1);
        assertEquals(vec1.y(), 2);
        assertEquals(vec1.z(), 3);

        assertEquals(vec1.index(0), 1);
        assertEquals(vec1.index(1), 2);
        assertEquals(vec1.index(2), 3);

        assertEquals(vec1.length_squared(), 14.0);
        assertEquals(vec1.length(), Math.sqrt(14.0));

    }

    @Test
    void testVecFunctions() {
        var vec1 = new Vec(1, 2, 3);
        assertEquals(vec1.negate(), new Vec(-1, -2, -3));

        assertEquals(vec1.addAssign(vec1), new Vec(2, 4, 6));
        assertEquals(vec1.mulAssign(2), new Vec(4, 8, 12));
        assertEquals(vec1.divAssign(2), new Vec(2, 4, 6));

        assertEquals(vec1.add(vec1.negate()), new Vec());
        assertEquals(vec1.sub(vec1.negate()), new Vec(4, 8, 12));
        assertEquals(vec1.mul(10), new Vec(20, 40, 60));
        assertEquals(vec1.mul(vec1), new Vec(4, 16, 36));
        assertEquals(vec1.div(2), new Vec(1, 2, 3));

        assertEquals(vec1.dot(vec1), 56);
        assertEquals(new Vec(1, 0, 0).dot(new Vec(0, 1, 0)), 0);

        assertEquals(vec1.cross(vec1), new Vec());
        assertEquals(vec1.cross(new Vec(3, 2, 1)), new Vec(-8, 16, -8));

        assertEquals(vec1.unit_vector(), new Vec(2 / Math.sqrt(56), 4 /
                Math.sqrt(56), 6 / Math.sqrt(56)));
        assertEquals(new Vec(1, 0, 0).unit_vector(), new Vec(1, 0, 0));

        assertEquals(new Vec(1e-9, 1e-9, 1e-9).nearZero(), true);
        assertEquals(new Vec(1e-7, 1e-7, 1e-7).nearZero(), false);

    }

    /*
     * @Test
     * void testFFI() {
     * System.out.println(System.getProperty("java.library.path"));
     * var structLayout = MemoryLayout.structLayout(MemoryLayout.sequenceLayout(3,
     * C_DOUBLE).withName("vector3"));
     * var segment = SegmentAllocator.implicitAllocator();
     * var vec3_struct = segment.allocate(structLayout);
     * var rust_vec = test_fn(segment, 1.0, 2.0, 3.0);
     * System.out.println(rust_vec);
     * System.out.println(rust_vec.isNative());
     * System.out.println(rust_vec.byteSize());
     * System.out.println(rust_vec.getAtIndex(ValueLayout.JAVA_DOUBLE, 0));
     * System.out.println(rust_vec.getAtIndex(ValueLayout.JAVA_DOUBLE, 1));
     * System.out.println(rust_vec.getAtIndex(ValueLayout.JAVA_DOUBLE, 2));
     * System.out.println(x(rust_vec));
     * show(rust_vec);
     * System.out.println(length(rust_vec));
     * System.out.println();
     * 
     * fill_with_values(vec3_struct, 10, 20, 30);
     * show(vec3_struct);
     * vec3_struct.setAtIndex(ValueLayout.JAVA_DOUBLE, 0, 100.0);
     * vec3_struct.setAtIndex(ValueLayout.JAVA_DOUBLE, 1, 200.0);
     * show(vec3_struct);
     * System.out.println(vec3_struct.getAtIndex(ValueLayout.JAVA_DOUBLE, 0));
     * System.out.println(vec3_struct.getAtIndex(ValueLayout.JAVA_DOUBLE, 1));
     * System.out.println(vec3_struct.getAtIndex(ValueLayout.JAVA_DOUBLE, 2));
     * System.out.println(x(vec3_struct));
     * System.out.println(length(vec3_struct));
     * }
     */
}