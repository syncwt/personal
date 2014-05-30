package com.beust.ray;

import java.util.List;

import com.google.common.collect.Lists;

public class Display {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private final Point upperLeft = new Point(0, HEIGHT, 0);
    private final Point lowerRight = new Point(WIDTH, 0, 0);

    public List<Point> allPoints() {
        List<Point> result = Lists.newArrayList();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                result.add(new Point(x, y, 0));
            }
        }
        return result;
    }
}
