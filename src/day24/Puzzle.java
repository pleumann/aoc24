/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author joerg
 */
public class Puzzle {
    
    abstract class Node {
        
        String id;
        
        abstract boolean evaluate();
        
        public Node(String id) {
            this.id = id;
        }
        
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
            System.out.println(k);
            if (!nodes.containsKey(k)) {
                break;
            }
            j = j | (nodes.get(k).evaluate() ? 1l << i : 0);
            i++;
        }

        return j;        
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
        
        System.out.println(j);
    }
    
    
    public static void main(String[] args) throws IOException {
        new Puzzle().solve(args[0]);
    }
    
}
