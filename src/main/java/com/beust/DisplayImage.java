package com.beust;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class DisplayImage extends Canvas {

    Image img;

    public DisplayImage(Image img) {
        this.img = img;
        setSize(img.getWidth(this), img.getHeight(this));
    }

    @Override
    public void paint(Graphics gr) {
        gr.drawImage(img, 0, 0, this);
    }

}
