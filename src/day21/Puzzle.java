package day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Day 21 "Keypad Conundrum", recursive solution with caching.
 */
public class Puzzle {
   
    /**
     * Represents a single keypad.
     */
    class Pad {
        
        /**
         * The key of our keypad, in order top-left to bottom-right.
         */
        String keys;

        /**
         * The next keypad in the line, if any.
         */
        Pad next;

        /**
         * The level of our keypad, just for debugging purposes.
         */
        int level;

        /**
         * Cache of the cost for moves within this keypad.
         */
        HashMap<String, Long> cache = new HashMap();

        /**
         * Creates a new keypad.
         */
        public Pad(String keys, Pad next, int level) {
            this.keys = keys;
            this.next = next;
            this.level = level;
        }

        /**
         * Recursively generates the list of all possible ways to get from
         * (x1, y1) to (x2, y2). Only disting ways are collected. Ways involving
         * the "missing key" are discarded. We can always safely move towards
         * the target. It is not possible that a detour is cheaper than a direct
         * way.
         */
        HashSet<String> ways(int x1, int y1, int x2, int y2, String s, HashSet<String> h) {
            if (keys.charAt(x1 * 3 + y1) == ' ') {
                return h;
            }
            
            if (x1 == x2 && y1 == y2) {
                System.out.printf("Level %2d: Path(%d,%d -> %d,%d) = %s\n", level, x1, y1, x2, y2, s);
                h.add(s + 'A');
                return h;
            }
            
            if (x1 < x2) {
                ways(x1 + 1, y1, x2, y2, s + 'v', h);
            } else if (x1 > x2) {
                ways(x1 - 1, y1, x2, y2, s + '^', h);
            }

            if (y1 < y2) {
                ways(x1, y1 + 1, x2, y2, s + '>', h);
            } else if (y1 > y2) {
                ways(x1, y1 - 1, x2, y2, s + '<', h);
            }
            
            return h;
        }
        
        /**
         * Calculates the cost for the given path, potentially triggering moves
         * further down in the chain of robots.
         */
        long cost(String s) {

            if (next == null) {
                return s.length();
            }

            if (cache.containsKey(s)) {
                return cache.get(s);
            }
            
            int x1 = keys.indexOf('A') / 3;
            int y1 = keys.indexOf('A') % 3;

            long cost = 0;
            
            for (char c: s.toCharArray()) {
                int x2 = keys.indexOf(c) / 3;
                int y2 = keys.indexOf(c) % 3;
                
           
                long delta = Long.MAX_VALUE;
                for (String t: ways(x1, y1, x2, y2, "", new HashSet<String>())) {
                    long cc = next.cost(t);
                    System.out.printf("Level %2d: Cost (%s) = %d\n", level, t, cc);
                    delta = Math.min(delta, cc);
                }
                cost += delta;
                
                x1 = x2;
                y1 = y2;
            }
            
            cache.put(s, cost);
            
            return cost;
        }

    }
    
    /**
     * Solves the puzzle for the given input and the given number of robots.
     */
    private long solve(BufferedReader bufferedReader, int robots) throws IOException {
        long result = 0;
        
        Pad p = new Pad(" ^A<v>", null, 0);
        
        for (int i = 1; i <= robots; i++) {
            p = new Pad(" ^A<v>", p, i);
        }
        
        p = new Pad("789456123 0A", p, robots + 1);
        
        String s = bufferedReader.readLine();
        while (s != null) {
            System.out.println("--- " + s + " ---");
            long c = p.cost(s);
            System.out.println();
            System.out.println("Cost is: " + c);
            System.out.println();
            
            Pad q = p;
            while (q != null) {
                System.out.println("Cache for layer " + q.level);
                for (String key: q.cache.keySet()) {
                    System.out.println("  " + key + " --> " + q.cache.get(key));
                }
                q = q.next;
            }
            
            result += Long.parseLong(s.substring(0, 3)) * c;
            s = bufferedReader.readLine();
        }
        
        return result;
    }
    
    /**
     * Canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.21 Keypad Condundrum ***");
        System.out.println();

        long part1 = new Puzzle().solve(new BufferedReader(new FileReader(args[0])), 2);
        long part2 = new Puzzle().solve(new BufferedReader(new FileReader(args[0])), 26);
        
        System.out.println();
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
        System.out.println();
    }

}
