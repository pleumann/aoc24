package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Day 10 "Hoof it"
 */
public class Puzzle {
    
    char[][] map;

    boolean[][] seen;
    
    int width;
    int height;
    
    int part1 = 0;
    int part2 = 0;
    
    /**
     * Recursively explores the map, counting trailheads in global variables.
     */
    void explore(int x, int y, char d) {  
        if (d == '9') {
            System.out.print('*');
            
            if (!seen[x][y]) {
                seen[x][y] = true;
                part1++;
            }
            
            part2++;
            
            return;
        }
        
        System.out.print('.');
        
        d++;
        
        if (x > 0 && map[x - 1][y] == d) {
            explore(x - 1, y, d);
        }
        
        if (y > 0 && map[x][y - 1] == d) {
            explore(x, y - 1, d);
        }

        if (x < height - 1 && map[x + 1][y] == d) {
            explore(x + 1, y, d);
        }

        if (y < width - 1 && map[x][y + 1] == d) {
            explore(x, y + 1, d);
        }
    }
    
    /**
     * Solves the puzzle for the given input file.
     */
    void solve(String name) throws IOException { 
        List<String> list = Files.readAllLines(Path.of(name));
        
        height = list.size();
        width = list.get(0).length();
        
        map = new char[height][];
        for (int i = 0; i < height; i++) {
            System.out.println(list.get(i));
            map[i] = list.get(i).toCharArray();
        }

        System.out.println();
        
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] == '0') {
                    seen = new boolean[height][width];
                    System.out.printf("Possible trailhead at %2d/%2d: ", i, j);
                    explore(i, j, '0');
                    System.out.println();
                }
            }
        }
        
        System.out.printf("\nPart 1: %10d\nPart 2: %10d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.10 Hoof it ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
