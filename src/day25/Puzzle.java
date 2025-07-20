/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author joerg
 */

class Schematic {
    int[] heights = new int[5];

    public Schematic() {
        Arrays.fill(heights, 0);
    }
}

public class Puzzle {

    private static List<Schematic> locks = new ArrayList<>();
    private static List<Schematic> keys = new ArrayList<>();

    public static void load(String path) throws IOException {
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
    }

    public static boolean fits(Schematic lock, Schematic key) {
        for (int i = 0; i < 5; i++) {
            if (lock.heights[i] + key.heights[i] > 5) {
                return false;
            }
        }
        return true;
    }

    public static void showLock(Schematic lock) {
        for (int i = 0; i < 5; i++) {
            // Position the cursor at (column 20, row 10 + i)
            System.out.printf("\u001B[%d;%dH", 3 + i, 17);

            // Print the lock representation
            for (int j = 0; j <= lock.heights[i]; j++) {
                System.out.print("#");
            }

            // Fill the rest with spaces for alignment
            for (int j = lock.heights[i] + 1; j < 8; j++) {
                System.out.print(" ");
            }
        }
    }

    public static void showKey(Schematic key) {
        for (int i = 0; i < 5; i++) {
            System.out.printf("\u001B[%d;%dH", 3 + i, 27);

            for (int j = key.heights[i] + 1; j < 8; j++) {
                System.out.print(" ");
            }

            for (int j = 0; j <= key.heights[i]; j++) {
                System.out.print("#");
            }
        }
    }

    public static int solve() {
        int result = 0;

        for (Schematic lock : locks) {
            showLock(lock);

            int countForLock = 0;
            for (Schematic key : keys) {
                showKey(key);

                if (fits(lock, key)) {
                    result++;
                    countForLock++;
                }
            }
        }

        return result;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.print("\u001B[2J");
        System.out.print("\u001B[H");
        
        System.out.println("*** AoC 2024.25 Code Chronicle ***");
        System.out.println("\nKeys:\nLocks:\n\n\nPart 1:");

        load(args[0]);
        int part1 = solve();

        System.out.printf("\u001B[7;1HPart 1: %4d\n", part1);
        System.out.println();
    }
    
}
