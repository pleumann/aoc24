package day14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    class Robot {
        int x;
        int y;
        
        int dx;
        int dy;
        
        public Robot(String s) {
            String[] a = s.split("p=| v=|,");
            
            x = Integer.parseInt(a[1]);
            y = Integer.parseInt(a[2]);
            dx = Integer.parseInt(a[3]);
            dy = Integer.parseInt(a[4]);
        }
    }

    ArrayList<Robot> robots = new ArrayList();
    
    int threshold = 10;
    
    boolean step(int width, int height, int factor, int threshold) {
        int[] columns = new int[width];
        int[] rows = new int[height];

        int maxv = 0;
        int maxh = 0;
        
        boolean interest = false;
        
        for (Robot r: robots) {
            r.x = (r.x + factor * (r.dx + width)) % width;
            r.y = (r.y + factor * (r.dy + height)) % height;

            columns[r.x]++;
            if (columns[r.x] > threshold) {
                maxv = Math.max(maxv, columns[r.x]);
                interest = true;
            }

            rows[r.y]++;
            if (rows[r.y] > threshold) {
                maxh = Math.max(maxh, columns[r.x]);
                interest = true;
            }
        }
        
        return maxh > 32 && maxv > 32;
    }
    
    void simulate(int width, int height) {
        step(width, height, 99, 0);
        long count = 99;
        
        int threshold = 1;
        while (true) {
            count += 1;
            if (step(width, height, 1, threshold)) {
                dump(count);
                
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//
//                }
                
                threshold++;
            } else {
                System.out.print("\033[2;120H" + count + " (" + threshold + ")");
            }
        }
    }
    
    void simulate2(int width, int height, int steps) {
        int k = 98;
        
        for (int i = 1; i < steps; i++) {
            
            int nw = 0;
            int ne = 0;
            int sw = 0;
            int se = 0;
            
            int[] top = new int[width];
            Arrays.fill(top, Integer.MAX_VALUE);
            
            for (Robot r: robots) {
                r.x = (r.x + r.dx + width) % width;
                r.y = (r.y + r.dy + height) % height;
                
                if (r.y < top[r.x]) {
                    top[r.x] = r.y;
                }
                
                if (r.x < width / 2 && r.y < height / 2) {
                    nw++;
                } else if (r.x < width / 2 && r.y > height / 2) {
                    sw++;
                } else if (r.x > width / 2 && r.y < height / 2) {
                    ne++;
                } else if (r.x > width / 2 && r.y > height / 2) {
                    se++;
                }
                
            }

//            int wrong = 10;
//            
//            for (int j = 1; j < width / 2; j++) {
//                if (top[width / 2 - j] < top[width / 2 - j + 1] || top[width / 2 + j] < top[width / 2 + j - 1]) {
//                    wrong--;
//                    if (wrong == 0) {
//                        break;
//                    }
//                }
//            }
            
            if (i % 101 == 99) {
                System.out.println("At " + steps + " steps:");
                dump(i);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {

                }
                
                k = k + 1;
            }
        }
    }

    int safety(int width, int height) {
        int nw = 0;
        int ne = 0;
        int sw = 0;
        int se = 0;
        
        for (Robot r: robots) {
            if (r.x < width / 2 && r.y < height / 2) {
                nw++;
            } else if (r.x < width / 2 && r.y > height / 2) {
                sw++;
            } else if (r.x > width / 2 && r.y < height / 2) {
                ne++;
            } else if (r.x > width / 2 && r.y > height / 2) {
                se++;
            }
        }
        
        return nw * ne * sw * se;
    }
    
    void dump(long i) {
        System.out.print("\033[2J\033[H");
        System.out.print("\033[1;120H" + i);

        for (Robot r: robots) {
//            if (r.y < 40) {
                System.out.printf("\033[%d;%dH#", 1 + r.y, 1 + r.x);
//            }
        }        
    }
    
    void solve(String name) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(name));
        String s = br.readLine();
        while (s != null) {
            robots.add(new Robot(s));
            s = br.readLine();
        }
        
        //simulate(11, 7, 100);
        simulate(101, 103);
        
        //int part1 = safety(11, 7);
        int part1 = safety(101, 103);

        int part2 = 0;
        
        System.out.printf("\nPart 1: %d\nPart 2: %d\n", part1, part2);
    }
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.02 n/a ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}