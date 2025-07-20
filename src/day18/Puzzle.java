/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    record Point(int x, int y) { };
    
    ArrayList<Point> bytes = new ArrayList();
    
    int nextByte = 0;
    
    void solve(String path, int size) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));

        for (String s: lines) {
            String[] a = s.split(",");
            bytes.add(new Point(Integer.parseInt(a[0]), Integer.parseInt(a[1])));
        }
        
        System.out.printf("%d incoming bytes in list.\n", bytes.size());
        
        boolean[][] obstacle = new boolean[size][size];
        
        Point start = new Point(0, 0);
        Point target = new Point(size - 1, size - 1);

        for (int i = 0; i < bytes.size(); i++) {
            Point b = bytes.get(i);
            obstacle[b.x][b.y] = true;

            System.out.println("i=" + i + " obstacle=" + b);

            int timeout = 10000;
            int step = 1;
            
            HashSet<Point> positions = new HashSet();
            positions.add(start);

            while (!positions.contains(target) && step < timeout) {
                //System.out.println("Step " + step++);

                HashSet<Point> newPositions = new HashSet();

                for (Point p: positions) {
                    if (p.x > 0 && !obstacle[p.x - 1][p.y]) {
                        newPositions.add(new Point(p.x - 1, p.y));
                    }

                    if (p.y > 0 && !obstacle[p.x][p.y - 1]) {
                        newPositions.add(new Point(p.x, p.y - 1));
                    }

                    if (p.x < size - 1 && !obstacle[p.x + 1][p.y]) {
                        newPositions.add(new Point(p.x + 1, p.y));
                    }

                    if (p.y < size - 1 && !obstacle[p.x][p.y + 1]) {
                        newPositions.add(new Point(p.x, p.y + 1));
                    }
                }

                positions = newPositions;
            }
            
            System.out.println("Positions contains target? " + positions.contains(target));
            
            //System.out.println(bytes.get(nextByte - 1));
        }
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle().solve(args[0], 71);
    }
    
}
