package day23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Day 23 "LAN Party", find various cliques in a graph.
 */
public class Puzzle {

    /**
     * Holds our graph as a mapping of nodes names to names of neighbor nodes.
     */
    Map<String, Set<String>> graph;

    /**
     * The largest maximal clique.
     */
    Set<String> topClique = new HashSet();

    /**
     * Finds all maximal cliques via Bron-Kerbosch, which is a recursive
     * backtracking algorithm that is straightforward to implement.
     * 
     * R is the current clique we're looking at
     * P is the list of candidates to add to the clique
     * X is the list of nodes we've already investigated
     * 
     * See https://en.wikipedia.org/wiki/Bron–Kerbosch_algorithm for details.
     */
    private void bronKerbosch(Set<String> R, Set<String> P, Set<String> X) {
        
        // Recursion anchor
        if (P.isEmpty() && X.isEmpty()) {
            System.out.print('.');
            
            if (R.size() > topClique.size()) {
                // Found new largest maximal clique
                topClique = new HashSet(R);
            }
            
            return;
        }
        
        // Create a copy a P because we modify the original
        Set<String> pCopy = new HashSet(P);
        
        for (String v: pCopy) {
            Set<String> neighbors = graph.get(v);
            
            // Calculate R ∪ {v}
            Set<String> newR = new HashSet(R);
            newR.add(v);
            
            // Calculate P ∩ N(v)
            Set<String> newP = new HashSet(P);
            newP.retainAll(neighbors);
            
            // Calculate X ∩ N(v)
            Set<String> newX = new HashSet(X);
            newX.retainAll(neighbors);
            
            bronKerbosch(newR, newP, newX);
            
            P.remove(v);
            X.add(v);
        }
    }    
    
    private void solve(BufferedReader br) throws IOException {
        /*
         * First we construct the graph from the input file.
         */
        graph = new HashMap();
        
        String s = br.readLine();
        while (s != null) {
            String left = s.substring(0, 2);
            String right = s.substring(3);

            if (!graph.containsKey(left)) {
                graph.put(left, new HashSet());
            }

            if (!graph.containsKey(right)) {
                graph.put(right, new HashSet());
            }

            graph.get(left).add(right);
            graph.get(right).add(left);
            
            s = br.readLine();
        }
        
        System.out.printf("%d nodes.\n\n", graph.size());
        
        /*
         * Solve part 1 using three nested loops.
         */
        ArrayList<String> nodes = new ArrayList(graph.keySet());
        int part1 = 0;
        
        for (int i = 0; i < nodes.size(); i++) {
            String x = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                String y = nodes.get(j);
                for (int k = j + 1; k < nodes.size(); k++) {
                    String z = nodes.get(k);
                    if (graph.get(x).contains(y) && graph.get(y).contains(z) && graph.get(z).contains(x)) {
                        if (x.startsWith("t") || y.startsWith("t") || z.startsWith("t")) {
                            System.out.print('.');
                            part1++;
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println();
        System.out.println("Part 1: " + part1);
        System.out.println();

        /*
         * Solve part 2 using Bron-Kerbosch.
         */
        bronKerbosch(new HashSet(), new HashSet(graph.keySet()), new HashSet());
        
        String[] names = topClique.toArray(new String[topClique.size()]);
        Arrays.sort(names);
        String part2 = String.join(",", names);
                
        System.out.println();
        System.out.println();
        System.out.println("Part 2: " + part2);
        System.out.println();
    }

    /**
     * Canonical entry point.
     */
    public static void main(String [] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.23 LAN Party ***");
        System.out.println();

        new Puzzle().solve(new BufferedReader(new FileReader(args[0])));
    }
    
}
