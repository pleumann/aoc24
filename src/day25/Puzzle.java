package day25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Day 25 "Code Chronicle"
 */
public class Puzzle {

    /**
     * Represents a lock or key.
     */
    class Schematic {
        int[] heights = new int[5];
    }

    /**
     * Our lists of all locks and keys.
     */
    private List<Schematic> locks = new ArrayList<>();
    private List<Schematic> keys = new ArrayList<>();

    /**
     * Checks whether a given combination of lock and key fits.
     */
    public boolean fits(Schematic lock, Schematic key) {
        for (int i = 0; i < 5; i++) {
            if (lock.heights[i] + key.heights[i] > 5) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Shows a lock on the screen.
     */
    public void showLock(Schematic lock) {
        for (int i = 0; i < 5; i++) {
            // Position the cursor at (column 20, row 10 + i)
            System.out.printf("\u001B[%d;%dH", 3 + i, 17);

            // Print the lock representation
            System.out.print("#".repeat(1 + lock.heights[i]));
            System.out.print(" ".repeat(7 - lock.heights[i]));
        }
    }

    /**
     * Shows a key on the screen.
     */
    public void showKey(Schematic key) {
        for (int i = 0; i < 5; i++) {
            System.out.printf("\u001B[%d;%dH", 3 + i, 27);

            // Print the key representation
            System.out.print(" ".repeat(7 - key.heights[i]));
            System.out.print("#".repeat(1 + key.heights[i]));
        }
    }

    /**
     * Loads the schematics from the  given input file and solves the puzzle.
     */
    public void solve(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        int lockCount = 0;
        int keyCount = 0;

        String line = line = reader.readLine();
        while (line != null) {
            boolean isLock = line.equals("#####");
            Schematic p = new Schematic();

            for (int i = 0; i < 5; i++) {
                line = reader.readLine();
                for (int j = 0; j < 5; j++) {
                    if (line.charAt(j) == '#') {
                        p.heights[j]++;
                    }
                }
            }

            // Skip empty lines
            reader.readLine();
            reader.readLine();

            if (isLock) {
                locks.add(p);
                lockCount++;
                System.out.printf("\u001B[3;1HLocks:   %3d\n", lockCount);
            } else {
                keys.add(p);
                keyCount++;
                System.out.printf("\u001B[4;1HKeys:    %3d\n", keyCount);
            }
            
            line = reader.readLine();
        }

        reader.close();

        int result = 0;

        for (Schematic lock : locks) {
            showLock(lock);

            int countForLock = 0;
            for (Schematic key : keys) {
                showKey(key);

                if (fits(lock, key)) {
                    result++;
                    countForLock++;
                    System.out.printf("\u001B[7;1HPart 1: %4d\n", result);
                }
            }
        }
    }

    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.print("\u001B[2J");
        System.out.print("\u001B[H");
        
        System.out.println("*** AoC 2024.25 Code Chronicle ***");
        System.out.println("\nKeys:\nLocks:\n\n\nPart 1:");

        new Puzzle().solve(args[0]);

        System.out.println();
    }
    
}
