package day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author joerg
 */
public class Puzzle {
   
    class Pad {
        
        String keys;
        
        Pad next;
        
        HashMap<String, Long> cache = new HashMap();
        
        public Pad(String keys, Pad next) {
            this.keys = keys;
            this.next = next;
        }
        
        void ways(int x1, int y1, int x2, int y2, String s, HashSet<String> h) {
            if (keys.charAt(x1 * 3 + y1) == ' ') {
                return;
            }
            
            if (x1 == x2 && y1 == y2) {
                System.out.printf("%d,%d -> %d,%d = %s\n", x1, y1, x2, y2, s);
                h.add(s + 'A');
                return;
            }
            
            if (x1 < x2) {
                ways(x1 + 1, y1, x2, y2, s + 'v', h);
            } else if (x1 > x2) {
                ways(x1 - 1, y1, x2, y2, s + '^', h);
            }

            if (y1 < y2) {
                ways(x1, y1 + 1, x2, y2, s + '>', h);
            } else if (y1 > y2) {
                ways(x1, y1 - 1, x2, y2, s + '<', h);
            }
            
        }
        
        long cost(String s) {

            if (next == null) {
                return s.length();
            }

            if (cache.containsKey(s)) {
                return cache.get(s);
            }
            
            int x1 = keys.indexOf('A') / 3;
            int y1 = keys.indexOf('A') % 3;

            long cost = 0;
            
            for (char c: s.toCharArray()) {
                int x2 = keys.indexOf(c) / 3;
                int y2 = keys.indexOf(c) % 3;
                
                HashSet<String> h = new HashSet();
                ways(x1, y1, x2, y2, "", h);
           
                long delta = Long.MAX_VALUE;
                for (String t: h) {
                    long cc = next.cost(t);
                    System.out.println("Cost of " + t + " is " + cc);
                    delta = Math.min(delta, cc);
                }
                cost += delta;
                
                x1 = x2;
                y1 = y2;
            }
            
            cache.put(s, cost);
            
            return cost;
        }

    }
    
    private void solve(BufferedReader bufferedReader) throws IOException {
        long part1 = 0;
        
        
        Pad p1 = new Pad(" ^A<v>", null);
        
        for (int i = 0; i < 25; i++) {
            p1 = new Pad(" ^A<v>", p1);
        }
        
        Pad p4 = new Pad("789456123 0A", p1);
        
//        System.out.println(p4.cost("029A"));
        
        String s = bufferedReader.readLine();
        while (s != null) {
            System.out.println("--- " + s + " ---");
            long c = p4.cost(s);
            System.out.println(c);
            part1 += Long.parseLong(s.substring(0, 3)) * c;
            s = bufferedReader.readLine();
        }
        
        System.out.println("Part 1: " + part1);
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle().solve(new BufferedReader(new FileReader(args[0])));
    }

}
