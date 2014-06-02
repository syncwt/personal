package com.beust.ray;

import java.util.List;

import com.google.common.collect.Lists;

public class Display {

    private int width;
    private int height;

    public Display(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public List<Point> allPoints() {
        List<Point> result = Lists.newArrayList();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.add(new Point(x, y, 0));
            }
        }
        return result;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
