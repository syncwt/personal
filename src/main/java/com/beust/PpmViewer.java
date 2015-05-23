package com.beust;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.IOException;

import com.beust.ppm.ByteRGBPixmap;
import com.beust.ppm.Pixmap;

public class PpmViewer extends Frame {

    // constructeur pour un argument Pixmap
    public PpmViewer(String name, ByteRGBPixmap rgb) {
        super(name);
        setLocation(50, 50);
        // fabrication des pixels couleur au format usuel AWT : ColorModel.RGBdefault
        int[] pixels = new int[rgb.size];
        for (int i = 0; i < pixels.length; i++) {
            byte red = rgb.r.data[i];
            byte green = rgb.g.data[i];
            byte blue = rgb.b.data[i];
            red = (byte) (i % 2 == 0 ? 0xff : 0);
            blue = (byte) (i % 2 == 1 ? 0xff : 0);
            System.out.println("DisplayRGB r:" + red + " g:" + green + " b:" + blue);
            pixels[i] = 0xFF000000 + (Pixmap.intValue(red) << 16) | (Pixmap.intValue(green) << 8)
                    | Pixmap.intValue(blue);
        }
        // construit une image avec ces pixels
        MemoryImageSource source = new MemoryImageSource(rgb.width, rgb.height, pixels, 0,
                rgb.width);
        Image img = Toolkit.getDefaultToolkit().createImage(source);
        add(new DisplayImage(img));
        pack();
        show();
    }

    // constructeur pour un argument fichier PPM
    public PpmViewer(String filename) throws IOException {
        this(filename, new ByteRGBPixmap(filename));
    }

    private byte toByte(int n) {
        byte result = (byte) n;
        return (byte) (result & 0xff);
    }

    public PpmViewer(Ppm ppm) {
        super("PPM viewer");
        setLocation(50, 50);
        int[] pixels = new int[ppm.width * ppm.height];
        int pixelIndex = 0;
        for (int x = 0; x < ppm.width; x++) {
            for (int y = 0; y < ppm.height; y++) {
                int i = 0;
                int red = ppm.image[x][y][i++];
                int green = ppm.image[x][y][i++];
                int blue = ppm.image[x][y][i++];
                System.out.println("RGB: " + red + " " + green + " " + blue);
                pixels[pixelIndex++] = 0xFF000000 +
                        (red << 16) |
                        (green << 8) |
                        blue;
            }
        }
        MemoryImageSource source = new MemoryImageSource(ppm.width, ppm.height, pixels, 0,
                ppm.width);
        Image img = Toolkit.getDefaultToolkit().createImage(source);
        add(new DisplayImage(img));
        pack();
        show();
    }

    public static void main(String[] args) {
        try {
            new PpmViewer(Ppm.readFile("c:/users/cbeust/Documents/a.ppm"));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    
}