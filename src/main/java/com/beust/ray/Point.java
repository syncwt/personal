package com.beust.ray;

import com.google.common.base.Objects;

public class Point {
    private final int x;
    private final int y;
    private final int z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass())
                .addValue("[" + x + "," + y + "," + z + "]")
                .toString();
    }
}
