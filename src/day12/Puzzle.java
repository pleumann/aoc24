package day12;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    class Plot {
        int area;
        
        int perimeter;
    }
    
    char[][] map;
    
    int size;
    
    void recurse(char c, int x, int y, Plot a, HashSet<String> seen) {
        if (seen.contains("" + x + "/" + y)) {
            return;
        }
        
        if (map[x][y] != c) {
            a.perimeter++;
            return;
        }
        
        a.area++;
        seen.add("" + x + "/" + y);
        map[x][y] = '.';
        
        recurse(c, x - 1, y, a, seen);
        recurse(c, x, y + 1, a, seen);
        recurse(c, x + 1, y, a, seen);
        recurse(c, x, y - 1, a, seen);
    }
    
    int sides(HashSet<String> s) {
        int k = 0;
        
        boolean bu = false;
        boolean bd = false;
        
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                boolean bu2 = !s.contains("" + (i - 1) + "/" + j) && s.contains("" + i + "/" + j);
                boolean bd2 = !s.contains("" + (i + 1) + "/" + j) && s.contains("" + i + "/" + j);
                
                if (bu2 && !bu) {
                    k++;
                }
                if (bd2 && !bd) {
                    k++;
                }
                
                bu = bu2;
                bd = bd2;
            }        
        }        
        
        boolean bl = false;
        boolean br = false;

        for (int j = 1; j <= size; j++) {
            for (int i = 1; i <= size; i++) {
                boolean bl2 = !s.contains("" + i + "/" + (j - 1)) && s.contains("" + i + "/" + j);
                boolean br2 = s.contains("" + i + "/" + j) && !s.contains("" + i + "/" + (j + 1));
                
                if (bl2 && !bl) {
                    k++;
                }
                if (br2 && !br) {
                    k++;
                }
                
                bl = bl2;
                br = br2;
            }        
        }  
        
        return k;
        
    }
    
    private void solve(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        
        size = lines.size();
        
        map = new char[size + 2][];
        
        map[0] = ".".repeat(size + 2).toCharArray();
        
        for (int i = 0; i < size; i++) {
            map[i + 1] = ("." + lines.get(i) + ".").toCharArray();
        }

        map[size + 1] = ".".repeat(size + 2).toCharArray();
        
        int part1 = 0;
        int part2 = 0;
        
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                char c = map[i][j];
                if (c != '.') {
                    Plot p = new Plot();
                    HashSet<String> h = new HashSet();
                    recurse(c, i, j, p, h);
                    int q = p.area * p.perimeter;
                    int r = sides(h);
                    System.out.println(c + ": " + p.area + " * " + p.perimeter + " = " + q + " [" + h.size() + "]" + "sides=" + r);
                    part1 += q;
                    part2 += p.area * r;
                }                   
            }
        }
        
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.12 ***");
        System.out.println();
        
        new Puzzle().solve(args[0]);
    }

}
