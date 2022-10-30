package raytracinginoneweekend;

import vector.Vec;

public class Dielectric implements Material {

    // Index of Refraction
    public final double ir;

    public Dielectric(double ir) {
        this.ir = ir;
    }

    @Override
    public boolean scatter(Ray rIn, HitRecord rec, Vec attenuation, Ray scattered) {
        attenuation.from(new Vec(1.0, 1.0, 1.0));
        double refractionRatio = rec.frontFace ? (1.0/ir) : ir;

        Vec unit_direction = rIn.direction().unit_vector();
        //Vec refracted = unit_direction.refract(rec.normal, refractionRatio);

        double cosTheta = Math.min(unit_direction.negate().dot(rec.normal), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);

        boolean cannot_refract = refractionRatio * sinTheta > 1.0;
        Vec direction;

        if (cannot_refract || reflectance(cosTheta, refractionRatio) > Utils.randomDouble())
            direction = unit_direction.reflect(rec.normal);
        else
            direction = unit_direction.refract(rec.normal, refractionRatio);

        var scattered_new = new Ray(rec.p, direction);
        scattered.origin = scattered_new.origin;
        scattered.direction = scattered_new.direction;

        return true;
    }

    private static double reflectance(double cosine, double refIndex) {
        // Use Schlick's approximation for reflectance.
        var r0 = (1-refIndex) / (1+refIndex);
        r0 = r0*r0;
        return r0 + (1-r0)*Math.pow((1 - cosine),5);
    }
}
