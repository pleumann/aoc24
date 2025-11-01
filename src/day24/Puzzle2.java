/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day24;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author joerg
 */
public class Puzzle2 {

    abstract class Node {
        
        String id;

        abstract boolean evaluate();
        
        public Node(String id) {
            this.id = id;
        }

        
        abstract String toString(int depth);
        
    }
    
    HashMap<String, Node> nodes = new HashMap();
    
    class Const extends Node {

        boolean value;
    
        public Const(String id, boolean value) {
            super(id);
            this.value = value;
        }
        
        @Override
        public boolean evaluate() {
            return value;
        }

        
        public String toString(int depth) {
            return id;
        }
        
    }

    class And extends Node {

        String left;
        
        String right;

        public And(String id, String left, String right) {
            super(id);
            this.left = left;
            this.right = right;
        }
        
        @Override
        public boolean evaluate() {
            return nodes.get(left).evaluate() && nodes.get(right).evaluate();
        }
        
        public String toString(int depth) {
            if (depth == 0) {
                return id;
            } else {
                return "(" + nodes.get(left).toString(depth - 1) + " & " + nodes.get(right).toString(depth - 1) + ")";
            }
        }
        
    }

    class Or extends Node {

        String left;
        
        String right;

        public Or(String id, String left, String right) {
            super(id);
            this.left = left;
            this.right = right;
        }
        
        @Override
        public boolean evaluate() {
            return nodes.get(left).evaluate() || nodes.get(right).evaluate();
        }

        public String toString(int depth) {
            if (depth == 0) {
                return id;
            } else {
                return "(" + nodes.get(left).toString(depth - 1) + " | " + nodes.get(right).toString(depth - 1) + ")";
            }
        }
        
    }

    class Xor extends Node {

        String left;
        
        String right;

        public Xor(String id, String left, String right) {
            super(id);
            this.left = left;
            this.right = right;
        }
        
        @Override
        public boolean evaluate() {
            return nodes.get(left).evaluate() ^ nodes.get(right).evaluate();
        }
        
        public String toString(int depth) {
            if (depth == 0) {
                return id;
            } else {
                return "(" + nodes.get(left).toString(depth - 1) + " ^ " + nodes.get(right).toString(depth - 1) + ")";
            }
        }
    }
    
    Node parse(String s) {
        String[] a = s.split(": | -> | ");
        
        for (String b: a) {
            System.out.println(b);
        }

        if (a.length == 2) {
            return new Const(a[0], a[1].equals("1"));
        } else {
            return switch (a[1]) {
                case "AND" -> new And(a[3], a[0], a[2]);
                case "OR"  -> new Or(a[3], a[0], a[2]);
                case "XOR" -> new Xor(a[3], a[0], a[2]);
                default -> throw new RuntimeException("Oops!");
            };
        }
    }
    
    long result() {
        long i = 0;
        long j = 0;
        
        while (true) {
            String k = String.format("z%02d", i);
            //System.out.println(k);
            if (!nodes.containsKey(k)) {
                break;
            }
            j = j | (nodes.get(k).evaluate() ? 1l << i : 0);
            i++;
        }

        return j;        
    }
    
    String key(int i) {
        return String.format("%02d", i);
    }
    
    long calc(long x, long y) {
        //System.out.printf("%d + %d = ", x, y);
        
        for (int i = 0; i < 45; i++) {
            ((Const)(nodes.get("x" + key(i)))).value = (x & 1) == 1;
            ((Const)(nodes.get("y" + key(i)))).value = (y & 1) == 1;
                    
            x >>= 1;
            y >>= 1;
        }
        
        long r = result();
        
        //System.out.println(r);
        
        return r;
    }
    
    void deps(String id, HashSet<String> result, int depth) {
        if (depth == 0) {
            return;
        }
        
        Node n = nodes.get(id);
        
        if (n instanceof And a) {
            deps(a.left, result, depth - 1);
            deps(a.right, result, depth - 1);
        } else if (n instanceof Or o) {
            deps(o.left, result, depth - 1);
            deps(o.right, result, depth - 1);
        } else if (n instanceof Xor x) {
            deps(x.left, result, depth - 1);
            deps(x.right, result, depth - 1);
        } else if (n instanceof Const c) {
            result.add(c.id);
        }
    }

    void ops(String id, HashSet<String> result, int depth) {
        if (depth == 0) {
            return;
        }

        result.add(id);
        
        Node n = nodes.get(id);
        
        if (n instanceof And a) {
            deps(a.left, result, depth - 1);
            deps(a.right, result, depth - 1);
        } else if (n instanceof Or o) {
            deps(o.left, result, depth - 1);
            deps(o.right, result, depth - 1);
        } else if (n instanceof Xor x) {
            deps(x.left, result, depth - 1);
            deps(x.right, result, depth - 1);
        } else if (n instanceof Const c) {
            result.add(c.id);
        }
    }
    
    void solve(String path) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(path));
        
        String s = r.readLine();
        while (s != null) {
            System.out.println(s);
            
            if (!s.isBlank()) {
                Node n = parse(s);
                nodes.put(n.id, n);
            }
            s = r.readLine();
        }
        
        System.out.printf("%d nodes.\n", nodes.size());
        
        long j = result();
        
        System.out.println("Part 1: " + j);
        
        HashSet<String> cands = new HashSet();
        
        for (int i = 1; i < 46; i++) {
            String t = String.format("z%02d", i);
            HashSet<String> h = new HashSet();
            deps(t, h, 4);
            //System.out.println(t + " depends on " + h);
            if (h.size() != 4) {
                System.out.println("*** Error: " + t + " = " + nodes.get(t).toString(2));
            }
        }
        /*
        for (int i = 0; i < 45; i++) {
            for (int k = 0; k < 4; k++) {
                for (int l = 0; l < 4; l++) {
                    long x = k << i;
                    long y = l << i;
                    
                    long z = (k + l) << i;
                    
                    long act = calc(x, y);
                    if (act != z) {
                        System.out.println("This is wrong (i=" + i + ")");
                        //System.exit(1);
                    }
                }
            }
        }
        */

        System.out.print('>');
        String ss = System.console().readLine();
        while (ss.length() != 0) {
            System.out.println(ss + " = " + nodes.get(ss).toString(2));
            System.out.print('>');
            ss = System.console().readLine();
        }
        
    }
    
    
    public static void main(String[] args) throws IOException {
        new Puzzle2().solve(args[0]);
    }
    
}
