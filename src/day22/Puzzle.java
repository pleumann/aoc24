/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    ArrayList<HashMap<String, Long>> data = new ArrayList();

    long calc(long secret, int rounds, HashMap<String, Long> map) {
        long oldOnes = secret % 10; // Long.bitCount(secret);

        String recent = "";
        
        for (int i = 0; i < rounds; i++) {
            secret = secret ^ (secret << 6) % 16777216;
            secret = secret ^ (secret >> 5) % 16777216;
            secret = secret ^ (secret << 11) % 16777216;
            long newOnes = secret % 10; // Long.bitCount(secret);
            long diff = newOnes - oldOnes;
            //System.out.printf("  %10d %2d %2d\n", secret, newOnes, diff);
            
            recent = recent + String.format("%2d ", diff);
            
            if (recent.length() == 12) {
                //long j = map.getOrDefault(recent, 0l);
                if (!map.containsKey(recent)) {
                    map.put(recent, newOnes);
                }
                recent = recent.substring(3);
            }

            oldOnes = newOnes;
        }
        
        return secret;
    }
    
    void solve(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));

        long part1 = 0;
        long part2 = 0;

        HashSet<String> keys = new HashSet();
        for (String s: lines) {
            HashMap<String, Long> map = new HashMap();
            long secret = calc(Long.parseLong(s), 2000, map);
            data.add(map);
            keys.addAll(map.keySet());

            System.out.println(s + " -> " + secret);
            part1 += secret;
        }
        
        System.out.println("Total keys: " + keys.size());
        
        long best = 0;
        String kk = "";
        
        for (String k: keys) {
            long total = 0;
            
            for (HashMap<String, Long> map: data) {
                total += map.getOrDefault(k, 0l);
            }
            
            if (total > best) {
                best = total;
                kk = k;
            }
        }
        
        System.out.println(part1);
        System.out.println(best + " for " + kk);
    } 

    public static void main(String[] args) throws IOException {
        new Puzzle().solve(args[0]);
    }
    
}
