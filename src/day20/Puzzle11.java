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
public class Puzzle11 {
    
    char[][] map;
    
    int size;
    
    int startX, startY;
    
    int endX, endY;
    
    int numPaths;
    
    int bestPath;
    
    int inX, inY, outX, outY, length;
    
    Puzzle11(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        size = lines.size();
        map = new char[size][];
        for (int i = 0; i < size; i++) {
            map[i] = lines.get(i).toCharArray();
            
            for (int j = 0; j < map[i].length; j++) {
                char c = map[i][j];
                if (c == 'S') {
                    startX = i;
                    startY = j;
                    map[i][j] = '.';
                    System.out.println("Start is " + startX + "," + startY);
                } else if (c == 'E') {
                    endX = i;
                    endY = j;
                    map[i][j] = '.';
                    System.out.println("End is " + endX + "," + endY);
                }
            }
        }
    }
    
    void dump() {
        for (char[] c: map) {
            System.out.println(new String(c));
        }
        System.out.println();
    }
    
    void explore(int x, int y, int cost, int limit) {
        if (x < 1 || y < 1 || x > size - 2 || y > size - 2) {
            return;
        }

        if (limit != 0 && (cost > limit || bestPath < limit)) {
            return;
        }
        
        if (map[x][y] == '*') {
            return;
        }

        if (x == endX && y == endY) {
            if (cost < bestPath) {
                bestPath = cost;
            }
            return;
        }

        if (map[x][y] == '#') {
//            if (x == inX && y == inY) {
//                map[x][y] = '*';
//                explore(outX, outY, cost + length, limit);
//                map[x][y] = '#';
//            }
            return;
        }
        
        map[x][y] = '*';

        explore(x - 1, y, cost + 1, limit);
        explore(x + 1, y, cost + 1, limit);
        explore(x, y - 1, cost + 1, limit);
        explore(x, y + 1, cost + 1, limit);

        if (x == inX && y == inY) {
            explore(outX, outY, cost + length, limit);
        }

        map[x][y] = '.';
    }
    
    void solve() {
        numPaths = 0;
        bestPath = Integer.MAX_VALUE;
        explore(startX, startY, 0, Integer.MAX_VALUE);
        System.out.printf("Part 0: %d paths, best is %d.\n", numPaths, bestPath);
        
        int normalPath = bestPath;
        int part1 = 0;
        
        for (inX = 1; inX < size - 1; inX++) {
            System.out.println(inX);
            for (inY = 1; inY < size - 1; inY++) {
                if (map[inX][inY] == '.') {
                    for (outX = 1; outX < size - 1; outX++) {
                        for (outY = 1; outY < size - 1; outY++) {
                            if (map[outX][outY] == '.') {
                                length = Math.abs(outX - inX) + Math.abs(outY - inY);
                                if (length > 0 && length <= 2) {
                                    numPaths = 0;
                                    explore(startX, startY, 0, normalPath - 100);
                                    if (numPaths != 0) {
                                        part1++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.printf("Part 1: %d paths, best is %d.\n", part1, bestPath);
        
        int part2 = 0;
        
        for (inX = 1; inX < size - 1; inX++) {
            System.out.println(inX);
            for (inY = 1; inY < size - 1; inY++) {
                if (map[inX][inY] == '.') {
                    for (outX = 1; outX < size - 1; outX++) {
                        for (outY = 1; outY < size - 1; outY++) {
                            if (map[outX][outY] == '.') {
                                length = Math.abs(outX - inX) + Math.abs(outY - inY);
                                if (length > 0 && length <= 20) {
                                    numPaths = 0;
                                    explore(startX, startY, 0, normalPath - 100);
                                    if (numPaths != 0) {
                                        part2++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.printf("Part 2: %d paths, best is %d.\n", part1, bestPath);
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.20 ***");
        System.out.println();
        
        new Puzzle11(args[0]).solve();
    }
}
