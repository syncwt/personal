package com.beust.ray;

import com.google.common.base.Objects;

public class Vector {
    private Point start;
    private Point end;

    public Vector(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(getClass())
                .add("start", start)
                .add("end", end)
                .toString();
    }

}
