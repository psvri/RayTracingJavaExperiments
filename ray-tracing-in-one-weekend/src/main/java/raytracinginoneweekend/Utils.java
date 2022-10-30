package raytracinginoneweekend;

import java.util.concurrent.ThreadLocalRandom;
import vector.Vec;

public class Utils {
    public static double INFINITY = Double.POSITIVE_INFINITY;
    public static double PI = Math.PI;

    public static double degreesToRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double randomDouble(double min, double max) {
        // Returns a random real in [min,max).
        return min + (max-min)*randomDouble();
    }

    public static double clamp(double x, double min, double max) {
        if (x < min) return min;
        return Math.min(x, max);
    }

    public static Vec randomVec() {
        return new Vec(randomDouble(), randomDouble(), randomDouble());
    }

    public static Vec randomVec(double min, double max) {
        return new Vec(randomDouble(min, max), randomDouble(min, max), randomDouble(min, max));
    }

    public static Vec randomInUnitSphere() {
        while (true) {
            var p = randomVec(-1, 1);
            if (p.length() >= 1) continue;
            return p;
        }
    }

    public static Vec randomUnitVector() {
        return randomInUnitSphere().unit_vector();
    }

    public static Vec randomInHemisphere(final Vec normal) {
        Vec inUnitSphere = randomInUnitSphere();
        if (inUnitSphere.dot(normal) > 0.0) // In the same hemisphere as the normal
            return inUnitSphere;
        else
            return inUnitSphere.negate();
    }

    public static Vec randomInUnitDisk() {
        while (true) {
            var p = new Vec(randomDouble(-1,1), randomDouble(-1,1), 0);
            if (p.length_squared() >= 1) continue;
            return p;
        }
    }
}
