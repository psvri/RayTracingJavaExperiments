package raytracinginoneweekend;

import vector.Vec;

public class Metal implements Material {

    public Vec albedo;
    public double fuzz;

    public Metal(final Vec color, double f) {
        this.fuzz = f < 1 ? f : 1;
        albedo = color;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, Vec attenuation, Ray scattered) {
        Vec reflected = rIn.direction().unit_vector().reflect(rec.normal);

        var scattered_new = new Ray(rec.p, reflected.add(Utils.randomInUnitSphere().mul(fuzz)));
        scattered.origin = scattered_new.origin;
        scattered.direction = scattered_new.direction;

        attenuation.from(albedo);

        return (scattered.direction().dot(rec.normal) > 0);
    }
}
