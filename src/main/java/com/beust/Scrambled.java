package com.beust;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Scrambled {

    private static final Set<String> WORDS = Sets.newHashSet();

    {
        URL r = getClass().getClassLoader().getResource("words.txt");
        String f = r.getFile();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(f)));
            String word = br.readLine();
            int count = 0;
            while (word != null) {
                count++;
                WORDS.add(word);
                word = br.readLine();
            }
            System.out.println("Read " + count + " words");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Scrambled().run();
    }

   // Should find venus, earth, mars, ceres, asteroids, jupiter, saturn, neptune
   // uranus, pluto, dwarf, planet, moon
    private static String PLANETS =
            "dhobshneptuney" +
            "uejihunysthaor" +
            "dnauueeemaenwa" +
            "wnaiplutonaodh" +
            "aghplizooerusu" +
            "rdeihctmnwtnsh" +
            "fhyhopbeoqhiue" +
            "racoeaarrteoae" +
            "usaturncplanet" +
            "rtaehftueuleee" +
            "ieucufarovceio" +
            "arfairayaoeirh" +
            "toainiabearnae" +
            "piatepenaaehua" +
            "ediddoedutsets" +
            "eszeehophslums";

    private static int WIDTH = 14;
    private static int HEIGHT = 16;

    private static String TEST_GRID =
            "abc" + 
            "def" +
            "ghi";

    private static final String GRID = PLANETS;

    public static char getLetter(int x, int y) {
        return GRID.charAt(y * WIDTH + x);
    }

    enum Direction {
        NE(1, -1),
        E(1, 0),
        SE(1, 1),
        S(0, 1),
        SW(-1, 1),
        W(-1, 0),
        NW(-1, -1),
        N(0, -1)
        ;
        private int dx;
        private int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    class Result {
        protected int x;
        protected int y;
        protected Direction direction;
        protected int length;
        protected String word;

        public Result(int x, int y, Direction direction, int length, String word) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.length = length;
            this.word = word;
        }
    }

    class Worker extends Result {
        public Worker(int x, int y, Direction direction, int length) {
            super(x, y, direction, length, null);
        }

        public Result getResult() {
            StringBuilder sb = new StringBuilder();
            int xx = x;
            int yy = y;
            for (int i = 0; i < length; i++) {
                sb.append(getLetter(xx, yy));
                xx += direction.dx;
                yy += direction.dy;
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
                    break;
                }
            }

            String word = sb.toString();
            if (word.startsWith("asteroid")) {
                System.out.println("AST");
            }
            if (sb.length() == length && WORDS.contains(word)) {
                return new Result(x, y, direction, length, word);
            } else {
                return null;
            }
        }
    }

    private static final int MIN_LENGTH = 4;

    private void run() {
        List<Result> results = Lists.newArrayList();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                for (int length = MIN_LENGTH; length <= WIDTH || length <= HEIGHT; length++) {
                    for (Direction d : Direction.values()) {
                        Worker w = new Worker(x, y, d, length);
                        Result r = w.getResult();
                        if (r != null) {
                            results.add(r);
                        }
                    }
                }
            }
        }

        System.out.println("Found " + results.size() + " words:");
        for (Result r : results) {
            System.out.println("  " + r.word + " (" + r.x + "," + r.y + "," + r.direction + ")");
        }
    }
}
