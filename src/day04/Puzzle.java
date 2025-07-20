package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Day 4 "Ceres Search".
 */
public class Puzzle {
    
    /**
     * Extracts a string from a "rectangular" list of strings using a given
     * starting position, direction and maximum length.
     */
    String extract(List<String> lines, int x, int y, int dx, int dy, int length) {
        int height = lines.size();
        int width = lines.get(0).length();
        
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            if (x < 0 || y < 0 || x >= height || y >= width) {
                break;
            }
            
            sb.append(lines.get(x).charAt(y));
            
            x = x + dx;
            y = y + dy;
        }
        
        return sb.toString();
    }
    
    /**
     * Solves the puzzle for the input coming from the given file name.
     */
    void solve(String name) throws IOException {
        int part1 = 0;
        int part2 = 0;
        
        List<String> lines = Files.readAllLines(Path.of(name));
        
        int height = lines.size();
        int width = lines.get(0).length();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                
                // Part 1: Find XMAS in all directions.
                for (int dx = -1; dx < 2; dx++) {
                    for (int dy = -1; dy < 2; dy++) {
                        if (dx != 0 || dy != 0) {
                            if (extract(lines, x, y, dx, dy, 4).equals("XMAS")) {
                                part1++;
                            }
                        }
                    }
                }
                
                // Part 2: Find MAS arranged as an X.
                String s = extract(lines, x - 1, y - 1,  1, 1, 3);
                String t = extract(lines, x + 1, y - 1, -1, 1, 3);
                
                if ((s.equals("MAS") || s.equals("SAM")) &&
                    (t.equals("MAS") || t.equals("SAM"))) {
                    part2++;
                }
            }
        }
        
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.04 Ceres Search ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
