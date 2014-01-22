package com.beust;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.google.common.io.CountingInputStream;

public class WidthHeight {
    private static void log(String s) {
        System.out.println("[AppMain] " + s);
    }

    private void dump(int width, int height, long count) {
        System.out.println("Width/height: " + width + "x" + height);
        System.out.println("Bytes read: " + count);
        System.out.println("");
    }

    public void withSimpleImageInfo(String url) throws IOException {
        System.out.println("============== SimpleImageInfo");
        CountingInputStream is = new CountingInputStream(
                new URL(url).openStream());
        SimpleImageInfo info = new SimpleImageInfo(is);
        dump(info.getWidth(), info.getHeight(), is.getCount());
    }

    public void withImageReaders(String url) {
        Integer width = null;
        Integer height = null;
        ImageInputStream in = null;
        try {
            System.out.println("============== ImageReaders");
            CountingInputStream is = new CountingInputStream(new URL(url).openStream());
            in = ImageIO.createImageInputStream(is);
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    width = reader.getWidth(0);
                    height = reader.getHeight(0);
                    dump(width, height, is.getCount());
                } finally {
                    reader.dispose();
                }
            }
        } catch(IOException ex) {
            log("Couldn't read width/height for " + url);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public void withSimpleIo(String url) {
        Integer width = null;
        Integer height = null;
        ImageInputStream in = null;
        try {
            System.out.println("============== Simple ImageIO ");
            CountingInputStream is = new CountingInputStream(new URL(url).openStream());
            BufferedImage image = ImageIO.read(is);
            if (image != null) {
                try {
                    width = image.getWidth();
                    height = image.getHeight();
                    dump(width, height, is.getCount());
               } finally {
                }
            }
        } catch(IOException ex) {
            log("Couldn't read width/height for " + url);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        System.out.println("Width/height: " + width + "x" + height);
    }

    public static void main(String[] args) throws IOException {
        for (String pic : new String[] {
            "https://www.google.com/images/srpr/logo11w.png",
            "https://secure.gravatar.com/avatar/dd37331186010bca3b71bb3d2ff06553?s=80&d=404"
                }) {
            new WidthHeight().withSimpleIo(pic);
            new WidthHeight().withImageReaders(pic);
            new WidthHeight().withSimpleImageInfo(pic);
        }
    }
}