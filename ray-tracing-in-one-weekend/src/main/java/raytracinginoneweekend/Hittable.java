package raytracinginoneweekend;

public interface Hittable {
    boolean hit(final Ray r, double tMin, double tMax, HitRecord rec);
}
