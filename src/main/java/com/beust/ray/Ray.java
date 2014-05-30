package com.beust.ray;

import com.google.common.base.Objects;

public class Ray {
    private final Point origin;
    private final Vector vector;

    public Ray(Point origin, Vector vector) {
        this.origin = origin;
        this.vector = vector;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass())
                .add("origin", origin)
                .add("vector", vector)
                .toString();
    }
}
