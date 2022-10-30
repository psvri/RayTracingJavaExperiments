package raytracinginoneweekend;

import vector.Vec;

public class Lambertian implements Material {

    Vec albedo;

    public Lambertian(final Vec color) {
        albedo = color;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, Vec attenuation, Ray scattered) {
        var scatter_direction = rec.normal.add(Utils.randomUnitVector());

        // Catch degenerate scatter direction
        if (scatter_direction.nearZero())
            scatter_direction = rec.normal;

        var scattered_new = new Ray(rec.p, scatter_direction);
        scattered.origin = scattered_new.origin;
        scattered.direction = scattered_new.direction;
        attenuation.from(albedo);
        return true;
    }
}
