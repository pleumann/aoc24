package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Day 22 "Monkey Market".
 */
public class Puzzle {

    long calc(long secret, int rounds, HashMap<String, Long> data) {
        HashSet<String> seen = new HashSet();
        
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
            
            if (i >= 3) {                               // Minimum 4 prices
                if (!seen.contains(seq)) {              // 1st occurence only
                    seen.add(seq);
                    data.put(seq, data.getOrDefault(seq, 0L) + newPrice); // Add
                }
                
                seq = seq.substring(3);                 // Cut off oldest price
            }

            oldPrice = newPrice;
        }
        
        return secret;
    }
    
    void solve(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));

        long part1 = 0;
        HashMap<String, Long> data = new HashMap();

        for (String s: lines) {
            long secret = calc(Long.parseLong(s), 2000, data);
            System.out.print('.');
            // System.out.println(s + " -> " + secret);
            part1 += secret;
        }
        
        System.out.println();
        System.out.println();
        System.out.println("Total sequences: " + data.size());
        System.out.println();
        
        long part2 = 0;
        String optSeq = "";
        
        for (String k: data.keySet()) {   // Find best sequence of price changes
            long total = data.get(k);
            
            if (total > part2) {          // If total price is better, accept it
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
