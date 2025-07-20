/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    HashMap<String, Long> cache = new HashMap();
    
    boolean explore(String s, int index, String[] towels) {
        if (index == s.length()) {
            return true;
        }
        
        for (String t: towels) {
            if (s.substring(index).startsWith(t)) {
                if (explore(s, index + t.length(), towels)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    long explore2(String s, int index, String[] towels) {
        if (index == s.length()) {
            return 1;
        }
        
        long tmp = 0;
        
        for (String t: towels) {
            if (s.substring(index).startsWith(t)) {
                String key = s + "-" + index + "-" + t;
                Long value = cache.get(key);
                if (value == null) {
                    value = explore2(s, index + t.length(), towels);
                    cache.put(key, value);
                }
                
                tmp = tmp + value;
            }
        }
        
        return tmp;
    }

    long explore3(String s, int index, long[] cache, String[] towels) {
        if (index == s.length()) {
            return 1;
        }

        if (cache[index] != -1) {
            return cache[index];
        }
        
        long tmp = 0;
        
        for (String t: towels) {
            if (s.substring(index).startsWith(t)) {
                tmp = tmp + explore2(s, index + t.length(), towels);
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
        
        br.readLine();
        
        int part1 = 0;
        long part2 = 0;
        
        s = br.readLine();
        while (s != null) {
            System.out.println(s);
            
            if (explore(s, 0, towels)) {
                part1++;
            }
            long[] cache = new long[s.length()];
            Arrays.fill(cache, -1);
            part2 += explore3(s, 0, cache, towels);
            s = br.readLine();
        }

        System.out.println(part1);
        System.out.println(part2);
        
    } 

    public static void main(String[] args) throws IOException {
        new Puzzle().solve(args[0]);
    }
    
}
