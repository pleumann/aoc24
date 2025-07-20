package day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Day 08 "Resonant Collinearity"
 */
public class Puzzle {
    
    /**
     * Represents an antenna.
     */
    record Antenna(int x, int y, char c) { };

    /**
     * The original file, line by line.
     */
    List<String> lines;
    
    /**
     * Height of our map.
     */
    int height;
    
    /**
     * Width of our map.
     */
    int width;
    
    /**
     * Contains all antennas found in our map, by their frequency.
     */
    HashMap<Character, ArrayList<Antenna>> antennas = new HashMap();

    /**
     * Marks where we found antinodes (mainly to avoid duplicates).
     */
    boolean[][] antinodes;

    /**
     * Part 1 result.
     */
    int part1;
    
    /**
     * Part 2 result.
     */
    int part2;

    /**
     * Dumps the map (including antinodes) to the screen.
     */
    void dump() {
        for (int i = 0; i < height; i++) {
            String s = lines.get(i);
            for (int j = 0; j < width; j++) {
                char c = s.charAt(j);
                if (c == '.' && antinodes[i][j]) {
                    System.out.print('#');
                } else {
                    System.out.print(c);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Extracts all antennas from the map, so we have something more efficient
     * to deal with.
     */
    void findAntennas() {
        for (int i = 0; i < height; i++) {
            String s = lines.get(i);
            for (int j = 0; j < width; j++) {
                char c = s.charAt(j);
                if (Character.isLetterOrDigit(c)) {
                    Antenna n = new Antenna(i, j, c);
                    ArrayList<Antenna> l = antennas.get(c);
                    if (l == null) {
                        l = new ArrayList<>();
                        antennas.put(c, l);
                    }
                    l.add(n);
                    System.out.println("New antenna: " + n);
                }
            }
        }
        
        System.out.println();
    }

    /**
     * Checks if the given position is a new antinode (that is, we already know
     * it's an antinode, but it might still be a duplicate.
     */
    boolean checkAntinode(int x, int y) {
        if (x >= 0 && y >= 0 && x < height && y < width) {
            if (!antinodes[x][y]) {
                antinodes[x][y] = true;
                return true;
            }
        }
        
        return false;
    }

    /**
     * Finds antinodes according to the rules of part 1.
     */
    void findAntinodes1() {
        antinodes = new boolean[height][width];
        
        for (ArrayList<Antenna> list: antennas.values()) {
            for (int i = 0; i < list.size(); i++) {
                Antenna first = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    Antenna second = list.get(j);
                    int dx = second.x - first.x;
                    int dy = second.y - first.y;
                    
                    if (checkAntinode(first.x - dx, first.y - dy)) {
                        part1++;
                    }
                    
                    if (checkAntinode(second.x + dx, second.y + dy)) {
                        part1++;
                    }
                }
            }
        }
    }

    /**
     * Finds antinodes according to the rules of part 2.
     */
    void findAntinodes2() {
        antinodes = new boolean[height][width];
        
        for (ArrayList<Antenna> list: antennas.values()) {
            for (int i = 0; i < list.size(); i++) {
                Antenna first = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    Antenna second = list.get(j);
                    int dx = second.x - first.x;
                    int dy = second.y - first.y;
                    
                    for (int k = -100; k < 100; k++) { // Me lazy. No math. :)
                        float x = first.x + k * dx;
                        float y = first.y + k * dy;
                        
                        if (x == Math.floor(x) && y == Math.floor(y)) {
                            if (checkAntinode((int)x, (int)y)) {
                                part2++;
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Solves the puzzle for the input coming from the given file.
     */
    void solve(String name) throws IOException {
        lines = Files.readAllLines(Path.of(name));
        
        height = lines.size();
        width = lines.get(0).length();
        
        findAntennas();
        
        findAntinodes1();
        dump();

        findAntinodes2();
        dump();
        
        System.out.printf("Part 1: %10d\nPart 2: %10d\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.08 Resonant Collinearity ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
