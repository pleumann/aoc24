package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Day 18 "RAM Run". Basically path finding based on breadth-first flood fill.
 */
public class Puzzle {
    
    /**
     * Represents a point (oh, does it really?).
     */
    record Point(int x, int y) { };
    
    /**
     * Finds a path from the upper left corner to the lower right corner and
     * based on the given obstacles. Returns the number of steps needed, or -1
     * if not path can be found.
     */
    int findPath(int size, HashSet<Point> obstacle) {
        Point start = new Point(0, 0);
        Point target = new Point(size - 1, size - 1);

        HashSet<Point> newPos = new HashSet();
        HashSet<Point> seen = new HashSet();
        newPos.add(start);
        int steps = 0;

        while (!newPos.contains(target) && !newPos.isEmpty()) {
            HashSet<Point> oldPos = newPos;
            newPos = new HashSet();

            for (Point p: oldPos) {
                if (p.x > 0 && !obstacle.contains(p) && !seen.contains(p)) {
                    newPos.add(new Point(p.x - 1, p.y));
                }

                if (p.y > 0 && !obstacle.contains(p) && !seen.contains(p)) {
                    newPos.add(new Point(p.x, p.y - 1));
                }

                if (p.x < size - 1 && !obstacle.contains(p) && !seen.contains(p)) {
                    newPos.add(new Point(p.x + 1, p.y));
                }

                if (p.y < size - 1 && !obstacle.contains(p) && !seen.contains(p)) {
                    newPos.add(new Point(p.x, p.y + 1));
                }
                
                seen.add(p);
            }
            
            steps++;
        }

        if (newPos.isEmpty()) {
            steps = -1;
        }

        return steps;
    }
    
    /**
     * Solves both parts of the puzzle for the given input.
     */
    void solve(String path, int size) throws IOException {
        // Read input into array if points
        List<String> lines = Files.readAllLines(Path.of(path));
        List<Point> bytes = new ArrayList();

        for (String s: lines) {
            String[] a = s.split(",");
            bytes.add(new Point(Integer.parseInt(a[0]), Integer.parseInt(a[1])));
        }
        
        System.out.printf("Total obstacles : %d\n\n", bytes.size());

        // Build empty array of obstacles.
        HashSet<Point> obstacle = new HashSet();

        // Part 1: Place 1024 obstacles and find a path.
        for (int i = 0; i < 1024; i++) {
            System.out.printf("\033[1AObstacles placed: %4d\n", i);
            obstacle.add(bytes.get(i));
        }
        
        int part1 = findPath(size, obstacle);
        
        // Part 2: Place remaining obstacles, one at a time, and find a path.
        Point part2 = null;
        
        for (int i = 1024; i < bytes.size(); i++) {
            System.out.printf("\033[1AObstacles placed: %4d\n", i);
            Point b = bytes.get(i);
            obstacle.add(b);
            
            if (findPath(size, obstacle) == -1) {
                part2 = b;
                break;
            }
        }
        
        // Report our findings.
        System.out.println();
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.18 RAM Run ***");
        System.out.println();
        
        new Puzzle().solve(args[0], 71);
    }
    
}
