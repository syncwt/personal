package com.beust.ray;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private static final Point ORIGIN = new Point(0, 0, 0);
    private static final Vector CAMERA = new Vector(new Point(50, -50, -100), ORIGIN);
    private static final Display DISPLAY = new Display();

    private void run() {
        for (Point p : DISPLAY.allPoints()) {
            Ray ray = new Ray(p, CAMERA);
            System.out.println("Calculating ray " + ray);
        }
    }
}
