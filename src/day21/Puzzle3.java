package day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author joerg
 */
public class Puzzle3 {
   
    class Pad {
        
        String keys;
        
        Pad next;
        
        public Pad(String keys, Pad next) {
            this.keys = keys;
            this.next = next;
        }
        
        boolean ok(int x, int y) {
            return keys.charAt(x * 3 + y) != ' ';
        }
        
        long cost(int x1, int y1, int x2, int y2) {

            if (next == null) {
                return 1;
            }
            
            long vert = Integer.MAX_VALUE;
            if (x1 < x2 && ok(x1 + 1, y1)) {
                vert = next.cost(0, 2, 1, 1) + cost(x1 + 1, y1, x2, y2);
            } else if (x1 > x2 && ok(x1 - 1, y1)) {
                vert = next.cost(0, 2, 0, 1) + cost(x1 - 1, y1, x2, y2);
            }

            long horz = Integer.MAX_VALUE;
            if (y1 < y2 && ok(x1, y1 + 1)) {
                horz = next.cost(0, 2, 1, 2) + cost(x1, y1 + 1, x2, y2);
            } else if (y1 > y2 && ok(x1, y1 - 1)) {
                horz = next.cost(0, 2, 1, 0) + cost(x1, y1 - 1, x2, y2);
            }
            
            return Math.min(vert, horz);
        }

    }
    
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
        
        
        Pad p1 = new Pad(" ^A<v>", null);
        Pad p2 = new Pad(" ^A<v>", p1);
        Pad p3 = new Pad(" ^A<v>", p2);
        Pad p4 = new Pad("789456123 0A", p1);
        
        System.out.println(p4.cost(3, 2, 3, 1));
        System.out.println(p4.cost(3, 1, 2, 1));
        System.out.println(p4.cost(2, 1, 0, 2));
        System.out.println(p4.cost(0, 2, 3, 2));
        
        /*
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
        */
        
        System.out.println("Part 1: " + part1);
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle3().solve(new BufferedReader(new FileReader(args[0])));
    }

}
