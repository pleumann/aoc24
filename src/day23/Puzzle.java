/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author joerg
 */
public class Puzzle {

    Map<String, Set<String>> graph;

    ArrayList<String> nodes = new ArrayList();
    
    /**
     * Rekursiver Bron-Kerbosch Algorithmus
     */
    private static void bronKerbosch(Set<String> R, Set<String> P, Set<String> X,
                                     Map<String, Set<String>> graph, 
                                     List<Set<String>> cliques) {
        if (P.isEmpty() && X.isEmpty()) {
            // Maximale Clique gefunden
            cliques.add(new HashSet<>(R));
            return;
        }
        
        // Kopie erstellen, da wir während Iteration modifizieren
        Set<String> pCopy = new HashSet<>(P);
        
        for (String v : pCopy) {
            Set<String> neighbors = graph.get(v);
            
            // R ∪ {v}
            Set<String> newR = new HashSet<>(R);
            newR.add(v);
            
            // P ∩ N(v)
            Set<String> newP = new HashSet<>(P);
            newP.retainAll(neighbors);
            
            // X ∩ N(v)
            Set<String> newX = new HashSet<>(X);
            newX.retainAll(neighbors);
            
            bronKerbosch(newR, newP, newX, graph, cliques);
            
            P.remove(v);
            X.add(v);
        }
    }    

    /**
     * Findet alle maximalen Cliquen in einem Graphen
     * @param graph Adjazenzliste als Map
     * @return Liste aller maximalen Cliquen
     */
    public static List<Set<String>> findAllCliques(Map<String, Set<String>> graph) {
        List<Set<String>> cliques = new ArrayList<>();
        Set<String> R = new HashSet<>();  // aktuelle Clique
        Set<String> P = new HashSet<>(graph.keySet());  // Kandidaten
        Set<String> X = new HashSet<>();  // bereits bearbeitet
        
        bronKerbosch(R, P, X, graph, cliques);
        return cliques;
    }
    
    private void solve(BufferedReader br) throws IOException {
        graph = new HashMap();
        
        String s = br.readLine();
        while (s != null) {
            String left = s.substring(0, 2);
            String right = s.substring(3);

            if (!graph.containsKey(left)) {
                graph.put(left, new HashSet());
                nodes.add(left);
            }

            if (!graph.containsKey(right)) {
                graph.put(right, new HashSet());
                nodes.add(right);
            }

            graph.get(left).add(right);
            graph.get(right).add(left);
            
            s = br.readLine();
        }
        
        System.out.printf("%d nodes\n", nodes.size());
        
        int part1 = 0;
        
        for (int i = 0; i < nodes.size(); i++) {
            String x = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                String y = nodes.get(j);
                for (int k = j + 1; k < nodes.size(); k++) {
                    String z = nodes.get(k);
                    if (graph.get(x).contains(y) && graph.get(y).contains(z) && graph.get(z).contains(x)) {
                        if (x.startsWith("t") || y.startsWith("t") || z.startsWith("t")) {
                            System.out.println(x + "-" + y + "-" + z);
                            part1++;
                        }
                    }
                }
            }
        }
       
        
        System.out.println(part1);
        
        List<Set<String>> list = findAllCliques(graph);
        
        Set<String> max = new HashSet();
        
        for (Set<String> clique: list) {
            if (clique.size() > max.size()) {
                max = clique;
            }
        }
        
        System.out.println("Maximale Clique: " + max);
       
        String[] sa = max.toArray(new String[max.size()]);
        Arrays.sort(sa);
        System.out.print(sa[0]);
        for (int i = 1; i < sa.length; i++) {
            System.out.print("," + sa[i]);
        }
        
    }

    public static void main(String [] args) throws IOException {
        new Puzzle().solve(new BufferedReader(new FileReader(args[0])));
    }
    
}
