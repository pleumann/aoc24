package day19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Day 19 "Linen Layout". Recursive exploral with simple per-level caching.
 */
public class Puzzle {
    
    long explore(String s, int index, long[] cache, String[] towels) {
        if (index == s.length()) {
            return 1;
        }

        if (cache[index] != -1) {
            return cache[index];
        }
        
        long tmp = 0;
        
        for (String t: towels) {
            if (s.substring(index).startsWith(t)) {
                tmp = tmp + explore(s, index + t.length(), cache, towels);
            }
        }

        cache[index] = tmp;
        
        return tmp;
    }
    
    void solve(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String s = br.readLine();
        String[] towels = s.split((", "));
        
        System.out.println("" + towels.length + " towels found.");
        System.out.println();
        
        br.readLine();
        
        int part1 = 0;
        long part2 = 0;
        
        s = br.readLine();
        while (s != null) {
            System.out.print('.');
            
            long[] cache = new long[s.length()];
            Arrays.fill(cache, -1);
            long l = explore(s, 0, cache, towels);
            if (l != 0) {
                part1++;
            }
            part2 += l;

            s = br.readLine();
        }

        System.out.println();
        System.out.println();
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
        
    } 

    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.19 Linen Layout ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
