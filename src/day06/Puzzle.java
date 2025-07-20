package day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author joerg
 */
public class Puzzle {

    char[][] map;
    
    int[][] seen;
    
    int height;
    int width;

    int startX = -1;
    int startY = -1;
    
    void load(String name) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(name));
        
        height = lines.size();
        width = lines.get(0).length();
        
        map = new char[height][];
        for (int i = 0; i < height; i++) {
            map[i] = lines.get(i).toCharArray();
            
            if (startX == -1 && startY == -1) {
                startY = lines.get(i).indexOf('^');
                if (startY != -1) {
                    startX = i;
                }
            }
        }
        
        map[startX][startY] = '.';
    }
    
    void dump() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (seen[i][j] != 0) {
                    System.out.print('*');
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    int patrol() {
        seen = new int[height][width];

        int count = 0;
        
        int x = startX;
        int y = startY;
        
        int dx = -1;
        int dy = 0;
        
        while (true) {
            // System.out.printf("x=%3d y=%3d dx=%2d dy=%2d\n", x, y, dx, dy);
            
            if (seen[x][y] == 0) {
                count++;
            }
                
            int k = dx == -1 ? 1 : dy == 1 ? 2 : dx == 1 ? 4 : 8;
            if ((seen[x][y] & k) != 0) {
                count = -1;
                break; // Been here, done that.
            }
            seen[x][y] |= k;
            
            int newX = x + dx;
            int newY = y + dy;
            
            if (newX < 0 || newY < 0 || newX >= height || newY >= width) {
                break;
            }
            
            if (map[newX][newY] != '.') {
                int tmp = dx;
                dx = dy;
                dy = -tmp;
            } else {
                x = newX;
                y = newY;
            }
        }
        
        return count;
    }
    
    void solve(String name) throws IOException {
        load(name);
        
        System.out.printf("startx=%d starty=%d\n\n", startX, startY);
        
        int part1 = patrol();
        dump();
        
        int part2 = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((i != startX || j != startY) && map[i][j] == '.') {
                    map[i][j] = 'O';
                    if (patrol() == -1) {
                        part2++;
                        dump();
                    }
                    map[i][j] = '.';
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
        System.out.println("*** AoC 2024.06 Guard Gallivant ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
