/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author joerg
 */
public class Puzzle1 {
   
    record Point(int x, int y) {
    
        Point up() {
            return new Point(x - 1, y);
        }

        Point left() {
            return new Point(x, y - 1);
        }

        Point down() {
            return new Point(x + 1, y);
        }

        Point right() {
            return new Point(x, y + 1);
        }

    };
    
    char[][] map;
    
    int size;
    
    Point start;
    
    Point end;
    
    Puzzle1(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        size = lines.size();
        map = new char[size][];
        for (int i = 0; i < size; i++) {
            map[i] = lines.get(i).toCharArray();
            
            for (int j = 0; j < map[i].length; j++) {
                char c = map[i][j];
                if (c == 'S') {
                    start = new Point(i, j);
                    map[i][j] = '.';
                    System.out.println("Start is " + start);
                } else if (c == 'E') {
                    end = new Point(i, j);
                    map[i][j] = '.';
                    System.out.println("End is " + end);
                }
            }
        }
    }
    
    boolean canGo(Point p) {
        return map[p.x][p.y] != '#';
    }
    
    /**
     * Finds a path from the upper left corner to the lower right corner and
     * based on the given obstacles. Returns the number of steps needed, or -1
     * if not path can be found.
     */
    int findPath() {
        HashSet<Point> positions = new HashSet();
        HashSet<Point> seen = new HashSet();
        positions.add(start);
        int steps = 0;

        while (!positions.contains(end)) {
            HashSet<Point> newPositions = new HashSet();

            for (Point p: positions) {
                Point up = p.up();
                if (canGo(up) && !seen.contains(up)) {
                    newPositions.add(up);
                }

                Point left = p.left();
                if (canGo(left) && !seen.contains(left)) {
                    newPositions.add(left);
                }

                Point down = p.down();
                if (canGo(down) && !seen.contains(down)) {
                    newPositions.add(down);
                }

                Point right = p.right();
                if (canGo(right) && !seen.contains(right)) {
                    newPositions.add(right);
                }
                
                seen.add(p);
            }
            
            positions = newPositions;
            steps++;
            
            if (positions.isEmpty()) {
                throw new RuntimeException("Damn!");
            }
        }

        return steps;
    }
    
    void solve() {
        int standard = findPath();
        
        System.out.println(standard);
        
        int part1 = 0;

        for (int i = 1; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                if (map[i][j] == '#') {
                    map[i][j] = '.';

                    int better = findPath();
                    if (better <= standard - 1) {
                        System.out.println("Found a much better path with cost " + better);
                        part1++;
                    }

                    if (i < size - 2) {
                        if (map[i + 1][j] == '#') {
                            map[i + 1][j] = '.';

                            better = findPath();
                            if (better <= standard - 1) {
                                System.out.println("Found a much better path with cost " + better);
                                part1++;
                            }

                            map[i + 1][j] = '#';
                        }
                    }

                    if (j < size - 2) {
                        if (map[i][j + 1] == '#') {
                            map[i][j + 1] = '.';

                            better = findPath();
                            if (better <= standard - 1) {
                                System.out.println("Found a much better path with cost " + better);
                                part1++;
                            }

                            map[i][j + 1] = '#';
                        }
                    }

                    map[i][j] = '#';
                }
            }
        }
        
        System.out.println("Part 1: " + part1);
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.20 ***");
        System.out.println();
        
        new Puzzle1(args[0]).solve();
    }
}
