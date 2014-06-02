package com.beust.ray;

public class Sphere {
    private final Point center;
    private final double radius;

    public Sphere(Point origin, double radius) {
        this.center = origin;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
