package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Day 22 "Monkey Market".
 */
public class Puzzle {
    
    ArrayList<HashMap<String, Long>> data = new ArrayList();

    long calc(long secret, int rounds, HashMap<String, Long> map) {
        long oldPrice = secret % 10;

        String seq = "";
        
        for (int i = 0; i < rounds; i++) {
            // Part 1 is just binary math
            secret = secret ^ (secret <<  6) % 16777216; // *64
            secret = secret ^ (secret >>  5) % 16777216; // :32
            secret = secret ^ (secret << 11) % 16777216; // *2048
            
            // Part 2 is tracking prices
            long newPrice = secret % 10;                 // "ones digit"
            long diff = newPrice - oldPrice;
            
            seq = seq + String.format("%2d ", diff);
            
            if (seq.length() == 12) {                    // Minimum 4 prices
                if (!map.containsKey(seq)) {             // 1st occurence only
                    map.put(seq, newPrice);
                }
                seq = seq.substring(3);                  // Cut off oldest price
            }

            oldPrice = newPrice;
        }
        
        return secret;
    }
    
    void solve(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));

        long part1 = 0;

        HashSet<String> keys = new HashSet();
        for (String s: lines) {
            HashMap<String, Long> map = new HashMap();
            long secret = calc(Long.parseLong(s), 2000, map);
            System.out.print('.');
            // System.out.println(s + " -> " + secret);
            part1 += secret;
            
            data.add(map);              // Store whole map for later
            keys.addAll(map.keySet());  // Store all keys for later
        }
        
        System.out.println();
        System.out.println();
        System.out.println("Total sequences: " + keys.size());
        System.out.println();
        
        long part2 = 0;
        String optSeq = "";
        
        for (String k: keys) {          // Find best sequence of price changes
            long total = 0;
            
            for (HashMap<String, Long> map: data) {
                total += map.getOrDefault(k, 0l);
            }
            
            if (total > part2) {        // If total price is better, accept it
                part2 = total;
                optSeq = k;
            }
        }
        
        System.out.printf("Part 1: %11d\n", part1);
        System.out.printf("Part 2: %11d (for %s)\n", part2, optSeq.trim());
        System.out.println();
    } 

    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.22 Monkey Market ***");
        System.out.println();
        
        new Puzzle().solve(args[0]);
    }
    
}
