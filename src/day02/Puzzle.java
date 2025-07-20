/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    int[] parseLevels(String s) {
        String[] a = s.split(" ");
        int[] result = new int[a.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = Integer.parseInt(a[i]);
        }
        
        return result;
        
//        return Arrays.stream(s.split(" ")).mapToInt(Integer::parseInt).toArray();
    }
    
    boolean checkLevels(int[] levels) {
        int lastSign = 0;
        
        for (int i = 1; i < levels.length; i++) {
            int diff = levels[i] - levels[i - 1];
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false;
            }
            
            int sign = Integer.signum(diff);
            if (lastSign != 0 && lastSign != sign) {
                return false;
            }
            
            lastSign = sign;
        }
        
        return true;
    }
    
    int[] remove(int[] levels, int dampener) {
        int[] r = new int[levels.length - 1];
        
        System.arraycopy(levels, 0, r, 0, dampener);
        System.arraycopy(levels, dampener + 1, r, dampener, levels.length - dampener - 1);
        
        return r;
    }
    
    void solve(BufferedReader r) throws IOException {
        int part1 = 0;
        int part2 = 0;
        
        String s = r.readLine();
        while (s != null) {
            int[] levels = parseLevels(s);
            
            if (checkLevels(levels)) {
                part1++;
                part2++;
            } else {
                for (int i = 0; i < levels.length; i++) {
                    if (checkLevels(remove(levels, i))) {
                        part2++;
                        break;
                    }
                }
            }
            
            s = r.readLine();
        }
        
        System.out.println(part1);
        System.out.println(part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.02 n/a ***");
        System.out.println();

        new Puzzle().solve(new BufferedReader(new FileReader(args[0])));
    }
    
}
