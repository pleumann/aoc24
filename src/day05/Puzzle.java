package day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    record Pair(int left, int right) { };
    
    boolean check(String[] a, ArrayList<Pair> rules) {
        HashMap<Integer, Integer> pages = new HashMap();
        for (int i = 0; i < a.length; i++) {
            pages.put(Integer.parseInt(a[i]), i);
        }

        for (Pair p: rules) {
            if (pages.containsKey(p.left) && pages.containsKey(p.right)) {
                if (pages.get(p.left) > pages.get(p.right)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    boolean repair(String[] a, ArrayList<Pair> rules) {
        HashMap<Integer, Integer> pages = new HashMap();
        for (int i = 0; i < a.length; i++) {
            pages.put(Integer.parseInt(a[i]), i);
        }

        for (Pair p: rules) {
            if (pages.containsKey(p.left) && pages.containsKey(p.right)) {
                int l = pages.get(p.left);
                int r = pages.get(p.right);
                if (l > r) {
                    String s = a[l];
                    a[l] = a[r];
                    a[r] = s;
                    return false;
                }
            }
        }
        
        return true;
    }
    
    void solve(BufferedReader r) throws IOException {
        int part1 = 0;
        int part2 = 0;

        ArrayList<Pair> rules = new ArrayList();
        
        String s = r.readLine();
        while (!"".equals(s)) {
            String[] a = s.split("\\|");
            rules.add(new Pair(Integer.parseInt(a[0]), Integer.parseInt(a[1])));
            s = r.readLine();
        }
        
        s = r.readLine();
        while (s != null) {
            System.out.println(s);
            
            String[] a = s.split(",");
            
            if (check(a, rules)) {
                part1 = part1 + Integer.parseInt(a[a.length / 2]);
            } else {
                while (!repair(a, rules)) {
                    System.out.print('.');
                }
                part2 = part2 + Integer.parseInt(a[a.length / 2]);
            }
            
            s = r.readLine();
        }
        
        System.out.printf("\nPart 1: %10d\nPart 2: %10d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.02 Print Queue ***");
        System.out.println();

        new Puzzle().solve(new BufferedReader(new FileReader(args[0])));
    }
    
}
