package day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author joerg
 */
public class Puzzle {

    void solve(String name) throws IOException {
        int part1 = 0;
        int part2 = 0;
        
        String text = Files.readString(Path.of(name));

        Pattern p = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");
        Matcher m = p.matcher(text);

        int index = 0;
        boolean enabled = true;
        
        while (m.find(index)) {
            String g = m.group();
            System.out.println(g);
            
            if (g.startsWith("mul")) {
                int value = Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
                
                part1 += value;
                if (enabled) {
                    part2 += value;
                }
            } else {
                enabled = "do()".equals(g);
            }
            
            index = m.end();
        }

        System.out.printf("\nPart 1: %10d\nPart 2: %10d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.02 n/a ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
