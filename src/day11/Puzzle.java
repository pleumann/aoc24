package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Day 11 "Plutonian Pebbles"
 */
public class Puzzle {
    
    /**
     * Adds a value to a given key in a hash table.
     */
    void addValue(HashMap<Long, Long> values, long key, long value) {
        //System.out.printf("%dx%d ", value, key);
        values.put(key, values.getOrDefault(key, 0L) + value);
    }
    
    /**
     * Simulates a round for the given current values.
     */
    HashMap<Long, Long> simulate(HashMap<Long, Long> values) {
        HashMap<Long, Long> newValues = new HashMap();

        for (Long key: values.keySet()) {
            Long val = values.get(key);
            String ll = Long.toString(key);

            if (key == 0) {
                addValue(newValues, 1, val);
            } else if (ll.length() % 2 == 0) {
                int half = ll.length() / 2;
                
                Long x = Long.valueOf(ll.substring(0, half));
                Long y = Long.valueOf(ll.substring(half));

                addValue(newValues, x, val);
                addValue(newValues, y, val);
            } else {
                addValue(newValues, key * 2024, val);
            }
        }
        
        System.out.println();
        System.out.println();        

        int small = 0;
        int large = 0;
        
        for (Long v: newValues.keySet()) {
            if (v > 32767) {
                small++;
            } else {
                large++;
            }
        }
        
        System.out.printf("%d values, %d small and %d large\n", small + large, small, large);
        
        return newValues;
    }
    
    /**
     * Solves the whole puzzle for the input file name.
     */
    void solve(String name) throws IOException {
        
        String s = Files.readString(Path.of(name)).trim();
        String[] a = s.split(" ");

        HashMap<Long, Long> values = new HashMap();

        for (String t: a) {
            addValue(values, Long.parseLong(t), 1L);
        }

        System.out.println(); System.out.println();
        
        for (int round = 1; round <= 25; round++) {
            values = simulate(values);
        }
        
        long part1 = values.values().stream().reduce(0L, Long::sum);

        for (int round = 26; round <= 75; round++) {
            values = simulate(values);
        }
        
        long part2 = values.values().stream().reduce(0L, Long::sum);
        
        System.out.printf("Part 1: %15d\nPart 2: %15d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.11 Plutonian Pebbles ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
