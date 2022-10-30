package raytracinginoneweekend;

import vector.Vec;

public interface Material {
    boolean scatter(final Ray rIn, final HitRecord rec, Vec attenuation, Ray scattered);
}
