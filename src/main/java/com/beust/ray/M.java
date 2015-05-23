package com.beust.ray;

public class M {
    public static double dotProduct(Vector v1, Vector v2) {
//        System.out.println("dotProduct " + v1.getX() + "*" + v2.getX()
//                + " + " + v1.getY() + "*" + v2.getY()
//                + " + " + v1.getZ() + "*" + v2.getZ());
        double result = v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
        return result;
    }

    public void test() {
        System.out.println("Dot:" + M.dotProduct(
                new Vector(0, 3, -7),
                new Vector(2, 3, 1)));

        System.out.println("Dot:" + M.dotProduct(
                new Vector(new Point(5, 5, 5), new Point(5, 8, -2)),
                new Vector(new Point(5, 5, 5), new Point(7, 8, 6))));
    }
}
