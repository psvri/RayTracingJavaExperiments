package vector;

import org.junit.jupiter.api.Test;
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

        assertEquals(vec1.unit_vector(), new Vec(2 / Math.sqrt(56), 4 / Math.sqrt(56), 6 / Math.sqrt(56)));
        assertEquals(new Vec(1, 0, 0).unit_vector(), new Vec(1, 0, 0));
    }
}