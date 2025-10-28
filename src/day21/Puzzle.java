package day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author joerg
 */
public class Puzzle {

    String translate(String s, String keys, int x, int y) {
        StringBuilder sb = new StringBuilder();
        
        for (char c: s.toCharArray()) {
            int p = keys.indexOf(c);
            int newX = p / 3;
            int newY = p % 3;

            while (x != newX || y != newY) {
                if (y > newY && keys.charAt(3 * x + y - 1) != ' ') {
                    sb.append('<');
                    y--;
                } else if (y < newY && keys.charAt(3 * x + y + 1) != ' ') {
                    sb.append('>');
                    y++;
                } else if (x < newX && keys.charAt(3 * (x + 1) + y) != ' ') {
                    sb.append('v');
                    x++;
                } else if (x > newX && keys.charAt(3 * (x - 1) + y) != ' ') {
                    sb.append('^');
                    x--;
                }
                
            }

            sb.append('A');
        }
        
        return sb.toString();
    }

    String num(String s) {
        return translate(s, "789456123 0A", 3, 2);
    }

    String dir(String s) {
        return translate(s, " ^A<v>", 0, 2);
    }
    
    private void solve(BufferedReader bufferedReader) throws IOException {
        int part1 = 0;
        
        String s = bufferedReader.readLine();
        while (s != null) {
            System.out.println("--- " + s + " ---");
            String t = num(s);
            System.out.println(t);
            String u = dir(t);
            System.out.println(u);
            String v = dir(u);
            System.out.println(v);

            System.out.println(v.length());
            System.out.println();

            part1 += Integer.parseInt(s.substring(0, 3)) * v.length();
            
            s = bufferedReader.readLine();
        }
        
        System.out.println("Part 1: " + part1);
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle().solve(new BufferedReader(new FileReader(args[0])));
    }

}
