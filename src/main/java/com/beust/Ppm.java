package com.beust;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import com.google.common.io.Files;

public class Ppm {
    final int width, height;
    final int[][][] image;

    /**
     * Constructor for the objects. You need to set the width/height of the image to create.
     *
     * @param width width
     * @param height height
     */
    public Ppm(int width, int height) {
        this.width = width;
        this.height = height;

        image = new int[width][height][3];
    }

    public Ppm(File file) throws IOException {
        List<String> lines = Files.readLines(file, Charset.defaultCharset());
        Iterator<String> it = lines.iterator();
        if (! "P6".equals(it.next())) {
            throw new IllegalArgumentException("Expected P6");
        }
        String[] wl = it.next().split(" ");
        this.width = Integer.parseInt(wl[0]);
        this.height = Integer.parseInt(wl[1]);
        this.image = new int[width][height][3];
        int maxColor = Integer.parseInt(it.next());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String[] colors = it.next().split(" ");
                int i = 0;
                image[x][y][i] = Integer.parseInt(colors[i++]);
                image[x][y][i] = Integer.parseInt(colors[i++]);
                image[x][y][i] = Integer.parseInt(colors[i++]);
            }
        }
    }

    /**
     * Sets a pixel. Values can be in range 0...1024. If you use values bigger than 255 then you
     * need to set scanMaxValue to true in writeFile.
     *
     * @param x x
     * @param y y
     * @param r r
     * @param g g
     * @param b b
     */
    public void setPixel(int x, int y, int r, int g, int b) {
        image[x][y][0] = r;
        image[x][y][1] = g;
        image[x][y][2] = b;
    }

    protected void writeLine(BufferedWriter out, String line) throws Exception {
        out.write(line, 0, line.length());
        out.newLine();
    }

    /**
     * Writes a PPM-file to disk.
     *
     * @param fileName File to write to.
     * @param scanMaxValue Set this to true if you use r/g/b values bigger than 255.
     */
    public void writeFile(String fileName, boolean scanMaxValue) throws Exception {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

        writeLine(out, "P3");
        writeLine(out, "" + width + " " + height);

        if (scanMaxValue) {
            int maxVal = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    maxVal = Math.max(maxVal, image[x][y][0]);
                    maxVal = Math.max(maxVal, image[x][y][1]);
                    maxVal = Math.max(maxVal, image[x][y][2]);
                }
            }

            writeLine(out, "" + maxVal);
        } else {
            writeLine(out, "255");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                writeLine(out, "" + image[x][y][0] + " " + image[x][y][1] + " " + image[x][y][2]);
        }

        out.close();
    }

//    private BufferedImage parsePPM() throws IOException, PPMDecoderException {
//        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//        WritableRaster raster = img.getRaster();
//        for (int y = 0; y < height; ++y)
//            for (int x = 0; x < width; ++x)
//                for (int i = 0; i < 3; ++i) {
//                    parser.nextToken();
//                    if (parser.ttype == StreamTokenizer.TT_EOF)
//                        throw new EOFException("image appears to be truncated");
//                    if (parser.ttype != StreamTokenizer.TT_NUMBER)
//                        throw new PPMDecoderException("non-numeric value for sample " + i
//                                + " of pixel at (" + x + "," + y + ")");
//                    raster.setSample(x, y, i, (int) parser.nval);
//                }
//        return img;
//    }
        
    private static final int size = 256;

    public static Ppm readFile(String fileName) throws IOException {
        return new Ppm(new File(fileName));
    }

    public static File writeFile() {
        Ppm ppm = new Ppm(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i %2 == 0) {
                    ppm.setPixel(i, j, 0xff, 0, 0);
                } else {
                    ppm.setPixel(i, j, 0, 0xff, 0);
                }
            }
        }
        try {
            File f = new File(System.getProperty("java.io.tmpdir"), "a.ppm");
            ppm.writeFile(f.getAbsolutePath(), false);
            System.out.println(f);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Ppm ppm = readFile("c:/users/cbeust/Documents/a.ppm");
        new PpmViewer(ppm);
    }
}