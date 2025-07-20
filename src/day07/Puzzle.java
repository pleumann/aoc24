package day07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author joerg
 */
public class Puzzle {

    boolean explore(long target, long current, long[] values, int index, boolean concat) {
        if (index == values.length) {
            return current == target;
        }
        
        if (current > target) {
            return false;
        }
        
        if (explore(target, current * values[index], values, index + 1, concat)) {
            return true;
        }
        
        if (explore(target, current + values[index], values, index + 1, concat)) {
            return true;
        }
        
        if (concat) {
            long l = Long.parseLong(Long.toString(current) + Long.toString(values[index]));
            if (explore(target, l, values, index + 1, concat)) {
                return true;
            }
        }

        return false;
    }
    
    void solve(String name) throws IOException {
        long part1 = 0;
        long part2 = 0;
        
        BufferedReader r = new BufferedReader(new FileReader(name));
        String s = r.readLine();
        while (s != null) {
            System.out.println(s);
            
            String[] a = s.split("(\\:|\\ )+");
            long target = Long.parseLong(a[0]);
            long[] values = new long[a.length - 1];
            for (int i = 1; i < a.length; i++) {
                values[i - 1] = Long.parseLong(a[i]);
            }
            
            if (explore(target, values[0], values, 1, false)) {
                part1 += target;
            }

            if (explore(target, values[0], values, 1, true)) {
                part2 += target;
            }
            
            s = r.readLine();
        }
        
        System.out.printf("\nPart 1: %10d\nPart 2: %10d\n\n", part1, part2);
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
