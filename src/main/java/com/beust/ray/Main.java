package com.beust.ray;


public class Main {

    public static void main(String[] args) {
//        new Math().test();
//        Png png = new Png(100, 100);
//        png.setPoint(10, 10, 0xffff0000);
//        png.display();
        new Main().run();
    }

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int W = WIDTH / 2;

    private static final Sphere SPHERE = new Sphere(new Point(W, W, W), W);
    private static final Point CAMERA = new Point(W, W, -W);
    private static final Point LIGHT = new Point(0, W, -50);
    private static final double AMBIENT_COEFFICIENT = 0.2;
    private static final double DIFFUSE_COEFFICIENT = 1 - AMBIENT_COEFFICIENT;

    private final Display DISPLAY = new Display(WIDTH, HEIGHT);

    private void run() {
        Png png = new Png(DISPLAY.getWidth(), DISPLAY.getHeight());
//        int x = 50;
//        int y = 50;
//        for (int x = 0; x < DISPLAY.getWidth(); x++) {
//            for (int y = 0; y < DISPLAY.getHeight(); y++) {
//                Point p = new Point(x, y, 0);
//                int color;
//                if (d(CAMERA, p) > 0) {
//                    Vector lightVector = new Vector(p, LIGHT).normalize();
//                    Vector normalVector = new Vector(SPHERE.getCenter(), p).normalize();
//                    double shade = M.dotProduct(lightVector, normalVector);
//                    System.out.println("Shade: " + shade);
//                    if (shade < 0) {
//                        shade = 0;
//                    }
//                    double objectColor = 255.0;
//                    int c = (int) java.lang.Math.round(objectColor
//                            * (AMBIENT_COEFFICIENT + DIFFUSE_COEFFICIENT * shade));
//                    color = 0xff000000 |
//                            (c << 16) |
//                            (c << 8) |
//                            (c);
//                    String hexColor = Integer.toHexString(color);
//                    System.out.println("Hex: " + hexColor);
//                    System.out.println("");
//                } else {
//                    color = 0xff0000ff;
//                }
//                png.setPoint(x, y, color);
//            }
//        }
        png.display();
    }

    private double d(Point viewer, Point displayPoint) {
        double x0 = viewer.getX();
        double y0 = viewer.getY();
        double z0 = viewer.getZ();
        double x1 = displayPoint.getX();
        double y1 = displayPoint.getY();
        double z1 = 0;

        double dx = x1 - x0;
        double dy = y1 - y0;
        double dz = z1 - z0;

        double cx = SPHERE.getCenter().getX();
        double cy = SPHERE.getCenter().getY();
        double cz = SPHERE.getCenter().getZ();

        double r = SPHERE.getRadius();
        double a = dx*dx + dy*dy + dz*dz;
        double b = 2*dx*(x0-cx) +  2*dy*(y0-cy) +  2*dz*(z0-cz);
        double c = cx*cx + cy*cy + cz*cz + x0*x0 + y0*y0 + z0*z0 +
                                -2*(cx*x0 + cy*y0 + cz*z0) - r*r;

        double d = b * b - 4 * a * c;
        return d;
    }

}
