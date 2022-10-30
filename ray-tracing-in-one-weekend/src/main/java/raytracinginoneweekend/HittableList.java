package raytracinginoneweekend;

import java.util.ArrayList;
import java.util.List;

public class HittableList implements Hittable {

    public List<Hittable> objects = new ArrayList<>();

    public HittableList() {

    }

    public HittableList(Hittable object) {
        add(object);
    }

    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hit_anything = false;
        var closest_so_far = tMax;

        for (final var object : objects) {
            if (object.hit(r, tMin, closest_so_far, tempRec)) {
                hit_anything = true;
                closest_so_far = tempRec.t;
                rec.t = tempRec.t;
                rec.normal = tempRec.normal;
                rec.frontFace = tempRec.frontFace;
                rec.p = tempRec.p;
                rec.material = tempRec.material;
            }
        }

        return hit_anything;
    }
}
