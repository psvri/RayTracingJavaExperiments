package raytracinginoneweekend;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import vector.Vec;

public class Main {

    public static Color getColor(Vec colorVec, int samplesPerPixel) {

        var r = colorVec.x();
        var g = colorVec.y();
        var b = colorVec.z();

        // Divide the color by the number of samples and gamma-correct for gamma=2.0.
        var scale = 1.0 / samplesPerPixel;
        r = Math.sqrt(scale * r);
        g = Math.sqrt(scale * g);
        b = Math.sqrt(scale * b);

        return new Color((int) (256 * Utils.clamp(r, 0.0, 0.999)),
                (int)(256 * Utils.clamp(g, 0.0, 0.999)),
                (int)(256 * Utils.clamp(b, 0.0, 0.999)));
    }

    public static Vec rayColor(final Ray r, final Hittable world, int depth) {

        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (depth <= 0)
            return new Vec();

        HitRecord rec = new HitRecord();

        if (world.hit(r, 0.001, Utils.INFINITY, rec)) {
            Ray scattered = new Ray();
            Vec attenuation = new Vec();
            if (rec.material.scatter(r, rec, attenuation, scattered))
                return attenuation.mul(rayColor(scattered, world, depth-1));
            return new Vec();
        }

        var unit_direction = r.direction().unit_vector();
        var t = 0.5*(unit_direction.y() + 1.0);
        return new Vec(1.0, 1.0, 1.0).mul(1.0-t).add(new Vec(0.5, 0.7, 1.0).mul(t));
    }

    public static HittableList createWorld() {
        HittableList world = new HittableList();

        var ground_material = new Lambertian(new Vec(0.5, 0.5, 0.5));
        world.add(new Sphere(new Vec(0,-1000,0), 1000, ground_material));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                var choose_mat = Utils.randomDouble();
                Vec center = new Vec(a + 0.9*Utils.randomDouble(), 0.2, b + 0.9*Utils.randomDouble());

                if ((center.sub(new Vec(4, 0.2, 0))).length() > 0.9) {
                    Material sphereMaterial;

                    if (choose_mat < 0.8) {
                        // diffuse
                        var albedo = Utils.randomVec().mul(Utils.randomVec());
                        sphereMaterial = new Lambertian(albedo);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else if (choose_mat < 0.95) {
                        // metal
                        var albedo = Utils.randomVec(0.5, 1);
                        var fuzz = Utils.randomDouble(0, 0.5);
                        sphereMaterial = new Metal(albedo, fuzz);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else {
                        // glass
                        sphereMaterial = new Dielectric(1.5);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }
                }
            }
        }

        var material1 =new Dielectric(1.5);
        world.add(new Sphere(new Vec(0, 1, 0), 1.0, material1));

        var material2 = new Lambertian(new Vec(0.4, 0.2, 0.1));
        world.add(new Sphere(new Vec(-4, 1, 0), 1.0, material2));

        var material3 = new Metal(new Vec(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vec(4, 1, 0), 1.0, material3));

        return world;
    }

    public static void main(String[] args) throws Exception {

        // Image
        final double ASPECT_RATIO = 16.0 / 9.0;
        //final int IMAGE_WIDTH = 1280;
        final int IMAGE_WIDTH = 428;
        final int IMAGE_HEIGHT = (int)(IMAGE_WIDTH / ASPECT_RATIO);
        final int SAMPLES_PER_PIXEL = 100;
        final int MAX_DEPTH = 50;
        final double APERTURE = 0.1;
        final Vec LOOK_FROM = new Vec(13,2,3);
        final Vec LOOK_AT = new Vec(0,0,0);
        final Vec VUP = new Vec(0,1,0);
        final double DIST_TO_FOCUS = 10;

        // World
        HittableList world = createWorld();

        // Camera
        Camera cam = new Camera(LOOK_FROM, LOOK_AT, VUP, 20, ASPECT_RATIO, APERTURE, DIST_TO_FOCUS);

        // Render
        var image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        var counter = new AtomicInteger();

        IntStream.range(0, IMAGE_HEIGHT).parallel().forEach(j-> IntStream.range(0, IMAGE_WIDTH).parallel().forEach(i -> {
            var pixelColor = new Vec();
            for (int s = 0; s < SAMPLES_PER_PIXEL; ++s) {
                var u = (i + Utils.randomDouble()) / (IMAGE_WIDTH-1);
                var v = (j + Utils.randomDouble()) / (IMAGE_HEIGHT-1);
                Ray r = cam.getRay(u, v);
                pixelColor.addAssign(rayColor(r, world, MAX_DEPTH));
            }

            image.setRGB(i,
                    IMAGE_HEIGHT-1 - j,
                    getColor(pixelColor, SAMPLES_PER_PIXEL).getRGB());
            counter.getAndIncrement();
            System.out.print("Completed " + counter.get() + ":" + IMAGE_HEIGHT * IMAGE_WIDTH + "\r");
        }));

        /*try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, IMAGE_HEIGHT).parallel().forEach(j-> IntStream.range(0, IMAGE_WIDTH).parallel().forEach(i -> {
                executor.submit(() -> {
                    var pixelColor = new Vec();
                    for (int s = 0; s < SAMPLES_PER_PIXEL; ++s) {
                        var u = (i + Utils.randomDouble()) / (IMAGE_WIDTH-1);
                        var v = (j + Utils.randomDouble()) / (IMAGE_HEIGHT-1);
                        Ray r = cam.getRay(u, v);
                        pixelColor.addAssign(rayColor(r, world, MAX_DEPTH));
                    }

                    image.setRGB(i,
                            IMAGE_HEIGHT-1 - j,
                            getColor(pixelColor, SAMPLES_PER_PIXEL).getRGB());
                    counter.getAndIncrement();
                    System.out.print("Completed " + counter.get() + ":" + IMAGE_HEIGHT * IMAGE_WIDTH + "\r");
                });
            }));
        }*/

        File outputFile = new File("output.png");
        ImageIO.write(image, "png", outputFile);
    }
}