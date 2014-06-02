package com.beust.ray;

import com.google.common.base.Objects;

public class Vector {
    private final Point value;

    public Vector(double x, double y, double z) {
        this.value = new Point(x, y, z);
    }

    public Vector(Point start, Point end) {
        this(end.getX() - start.getX(),
                end.getY() - start.getY(),
                end.getZ() - start.getZ());
    }

    public Vector normalize() {
        double x = getX();
        double y = getY();
        double z = getZ();
        double length = java.lang.Math.sqrt(x*x + y*y + z*z);
        return new Vector(x / length, y / length, z / length);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass())
                .add("value", value)
                .toString();
    }

    public double getX() {
        return value.getX();
    }

    public double getY() {
        return value.getY();
    }

    public double getZ() {
        return value.getZ();
    }

    public Point getValue() {
        return value;
    }
}
