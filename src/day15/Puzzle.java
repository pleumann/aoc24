package day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    char[][] map;
    
    int width;
    
    int height;
    
    int x;
    
    int y;

    boolean push(int x, int y, int dx, int dy, boolean dryRun) {
        int newX = x + dx;
        int newY = y + dy;
        
        char c = map[newY][newX];
        
        if (c == '#') {
            return false;
        }
        
        if (c == 'O') {
            if (!push(newX, newY, dx, dy, dryRun)) {
                return false;
            }
        }
        
        if (c == '[' || c == ']') {
            if (dx != 0) {
                if (!push(newX, newY, dx, dy, dryRun)) {
                    return false;
                }
            } else /* dy != 0 */ {
                if (!push(newX, newY, dx, dy, dryRun)) {
                    return false;
                }
                if (!push(newX + (c == '[' ? 1 : -1), newY, dx, dy, dryRun)) {
                    return false;
                }
            }
        }
        
        if (!dryRun) {
            map[newY][newX] = map[y][x];
            map[y][x] = '.';
        }
        
        return true;
    }
    
    void dump() {
        System.out.print("\033[H");

        for (char[] ca: map) {
            System.out.println(new String(ca));
        }
        
        System.out.println();
    }
    
    int gps() {
        int result = 0;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[j][i] == 'O' || map[j][i] == '[') {
                    result = result + 100 * j + i;
                }
            }
        }
        
        return result;
    }
    
    String scale(String s) {
        StringBuilder sb = new StringBuilder();
        
        for (char c: s.toCharArray()) {
            if (c == 'O') {
                sb.append("[]");
            } else if (c == '@') {
                sb.append("@ ");
            } else {
                sb.append(c);
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    int solve(String name, int part) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(name));
        String s = br.readLine();
        
        height = s.length();
        map = new char[height][];
 
        for (int i = 0; i < height; i++) {
            if (part == 2) {
                s = scale(s);
            }
            
            int p = s.indexOf('@');
            if (p != -1) {
                x = p;
                y = i;
            }
            map[i] = s.toCharArray();
            s = br.readLine();
        }
        
        width = map[0].length;
        
        assert("".equals(s));

        dump();

        String cmd = "";
        s = br.readLine();
        while (s != null) {
            cmd = cmd + s;
            s = br.readLine();
        }
        
        for (int i = 0; i < cmd.length(); i++) {
            char c = cmd.charAt(i);
            //System.out.println("Moving " + c + " ...");
            //System.out.println();
            
            if (c == '^') {
                if (push(x, y, 0, -1, true)) {
                    push(x, y, 0, -1, false);
                    y--;
                }
            } else if (c == '>') {
                if (push(x, y, 1, 0, true)) {
                    push(x, y, 1, 0, false);
                    x++;
                }
            } else if (c == 'v') {
                if (push(x, y, 0, 1, true)) {
                    push(x, y, 0, 1, false);
                    y++;
                }
            } else if (c == '<') {
                if (push(x, y, -1, 0, true)) {
                    push(x, y, -1, 0, false);
                    x--;
                }
            }
            
            dump();
            
            System.out.println("Move " + i + "/" + cmd.length() + " is: " + c);
            System.out.println();
            
            try {
                Thread.sleep(2);
            } catch(InterruptedException e) {

            }
        }
        
        return gps();
    }
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.print("\033[2J\033[H");
//        System.out.println();
//        System.out.println("*** AoC 2024.15 Warehouse Woes ***");
//        System.out.println();

        Puzzle p = new Puzzle();
        int part1 = p.solve(args[0], 1);
        int part2 = p.solve(args[0], 2);
        
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
    
}