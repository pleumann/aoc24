package day13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    double[] toNumbers(String s) {
        String[] a = s.split("[^0-9]+");
       
        double[] result = new double[a.length];
        for (int i = 1; i < a.length; i++) {
            result[i - 1] = Double.parseDouble(a[i]);
        }
        
        return result;
    }

    long[] toLongArray(String s) {
        String[] a = s.split("[^0-9]+");
       
        long[] result = new long[a.length];
        for (int i = 1; i < a.length; i++) {
            result[i - 1] = Long.parseLong(a[i]);
        }
        
        return result;
    }
    
    double[] doCramer2(double a1, double b1, double c1, double a2, double b2, double c2) {
        double det = a1 * b2 - a2 * b1;
        
        if (det == 0) {
            return null;
        }
        
        double[] res = new double[2];
        
        res[0] = (c1 * b2 - c2 * b1) / det;
        res[1] = (a1 * c2 - a2 * c1) / det;
        
        return res;
    }

    long[] doCramer(long a1, long b1, long c1, long a2, long b2, long c2) {
        long det = a1 * b2 - a2 * b1;
        
        if (det == 0) {
            return null;
        }
        
        long[] res = new long[2];
        
        res[0] = (c1 * b2 - c2 * b1) / det;
        res[1] = (a1 * c2 - a2 * c1) / det;
        
        if (res[0] * a1 + res[1] * b1 != c1 || res[0] * a2 + res[1] * b2 != c2) {
            return null;
        }
        
        return res;
    }
    
    long solve(String name, int part) throws IOException {

        long result = 0;
        
        BufferedReader r = new BufferedReader(new FileReader(name));
        String s = r.readLine();
        while (s != null) {
            String t = r.readLine();
            String u = r.readLine();
            
            System.out.printf("%-20s %-20s %-23s -> ", s, t, u);
            
            double[] a = toNumbers(s);
            double[] b = toNumbers(t);
            double[] c = toNumbers(u);
            
            if (part == 2) {
                c[0] += 10000000000000.0;
                c[1] += 10000000000000.0;
            }

            double[] d = doCramer2(a[0], b[0], c[0], a[1], b[1], c[1]);
            if (d != null) {
                double p = d[0];
                double q = d[1];
                if (p == Math.round(p) && q == Math.round(q)) {
                    long cost = 3 * Math.round(p) + Math.round(q);
                    result += cost;
                    System.out.println("Integer");
                } else {
                    System.out.println("Decimal");
                }
            } else {
                System.out.println("Invalid");
            }
            
            r.readLine();
            s = r.readLine();
        }

        return result;
    }

    long solve2(String name, int part) throws IOException {

        long result = 0;
        
        BufferedReader r = new BufferedReader(new FileReader(name));
        String s = r.readLine();
        while (s != null) {
            String t = r.readLine();
            String u = r.readLine();
            
            System.out.printf("%-20s %-20s %-23s -> ", s, t, u);
            
            long[] a = toLongArray(s);
            long[] b = toLongArray(t);
            long[] c = toLongArray(u);
            
            if (part == 2) {
                c[0] += 10000000000000l;
                c[1] += 10000000000000l;
            }

            long[] d = doCramer(a[0], b[0], c[0], a[1], b[1], c[1]);
            if (d != null) {
                long cost = 3 * d[0] + d[1];
                result += cost;
                System.out.println("Solvable");
            } else {
                System.out.println("Unsolvable");
            }
            
            r.readLine();
            s = r.readLine();
        }

        return result;
    }

    void solve(String name) throws IOException {
        long part1 = solve2(name, 1);
        long part2 = solve2(name, 2);
        
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