/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day09;

import day10.*;
import day09.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author joerg
 */
public class Puzzle2 {
    
    char[] disk;

    int targetSector;
    int sourceSector;

    int sector;
    long checksum;
    
    boolean odd(int i) {
        return i % 2 == 1;
    }
    
    void setup(String s) {
        disk = s.toCharArray();

        targetSector = 0;
        sourceSector = disk.length - 1;
        
        sector = 0;
        checksum = 0;
    }
    
    void writeSector(int i) {
        System.out.print(i + " ");
        checksum += sector * i;
        sector++;
    }
    
    int readBlock() {
        while(targetSector < sourceSector) {
            if (odd(sourceSector) || disk[sourceSector] == '0') {
                sourceSector--;
            } else {
                disk[sourceSector]--;
                return sourceSector / 2;
            }
        }

        return -1;
    }

    int readFile() {
        while(targetSector < sourceSector) {
            if (odd(sourceSector)) {
                sourceSector--;
            }
            
            return sourceSector / 2 * 10 +0 ;
        }

        return -1;
    }

    void writeBlock(int i) {
        while(targetSector < disk.length) {
            if (!odd(targetSector)) {
                if (disk[targetSector] != '0') {
                    disk[targetSector]--;
                    writeSector(targetSector / 2);
                } else {
                    targetSector++;
                }
            } else{
                if (disk[targetSector] == '0') {
                    targetSector++;
                } else {
                    disk[targetSector]--;
                    writeSector(i);
                    return;
                }
            }
        }
    }
    
    void solve(String name) throws IOException {
        long part1 = 0;
        int part2 = 0;

        setup(Files.readString(Path.of(name)));
        
        int i = readBlock();
        while (i != -1) {
            //System.out.print(i + ",");
            writeBlock(i);
            i = readBlock();
        }
        //System.out.print(" : ");
        writeBlock(0);
        
        part1 = checksum;
        
        System.out.printf("\nPart 1: %10d\nPart 2: %10d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.02 n/a ***");
        System.out.println();

        new Puzzle2().solve(args[0]);
    }
    
}
