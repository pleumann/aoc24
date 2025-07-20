package day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Day 01 "Historian Hysteria".
 */
public class Puzzle {
    
    /**
     * Solves the puzzle for the given input file.
     */
    void solve(String path) throws IOException {
        
        // Step 1: Load file, split lines, convert to integer.
        
        List<String> lines = Files.readAllLines(Path.of(path));
        
        int count = lines.size();
        
        int[] left = new int[count];
        int[] right = new int[count];
        
        for (int i = 0; i < count; i++) {
            String s = lines.get(i);
            String[] a = s.split("\\s+");
            
            left[i] = Integer.parseInt(a[0]);
            right[i] = Integer.parseInt(a[1]);
        }
        
        // Step 2: Sort
        
        Arrays.sort(left);
        Arrays.sort(right);

        // Step 3: Walk through both arrays in parallel.
        
        int part1 = 0;
        int part2 = 0;
        
        int j = 0;
        
        for (int i = 0; i < count; i++) {
            part1 += Math.abs(right[i] - left[i]);

            if (i == 0 || left[i] != left[i - 1]) {
                while (j < count && right[j] < left[i]) {
                    j++;
                }
                
                while (j < count && right[j] == left[i]) {
                    part2 += left[i];
                    j++;
                }
            }
        }
        
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.01 Historian Hysteria ***");
        System.out.println();
        
        new Puzzle().solve(args[0]);
    }
    
}