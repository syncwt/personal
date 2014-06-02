package com.beust.ray;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Png {

    private BufferedImage image;
    private final int width;
    private final int height;

    public Png(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void setPoint(int x, int y, int color) {
        System.out.println("Setting " + x + "," + y);
        image.setRGB(x, height - 1 - y, color);
    }

    public void display() {
        JFrame frame = new JFrame();
        frame.setSize(width, height + 50);
        frame.getContentPane().setLayout(new FlowLayout());
        JLabel label = new JLabel(new ImageIcon(image));
//        label.setPreferredSize(new Dimension(width, height));
        frame.getContentPane().add(label);
//        frame.pack();
        frame.setVisible(true);
    }
}
