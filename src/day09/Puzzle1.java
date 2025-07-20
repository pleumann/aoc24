/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author joerg
 */
public class Puzzle1 {
    
    static class Block {
        
        int offset;

        int length;

        int fileID;

        Block prev;

        Block next;
        
    }
        
    Block first;

    Block last;
        
    void init() {
        first = new Block();

        first.length = 1000000;
        first.fileID = -1;

        last = first;
    }
        
    void copy(Block source, Block target) {
        assert(source.fileID != -1);
        assert(target.fileID == -1);

        if (source.length < target.length) {
            Block temp = new Block();
            temp.next = target.next;
            temp.prev = target;
            target.next = temp;

            target.fileID = source.fileID;
            temp.fileID = -1;

            temp.offset = target.offset + source.length;
            
            temp.length = target.length - source.length;
            target.length = source.length;
            source.length = 0;
            
            if (last == target) {
                last = temp;
            }
        } else {
            target.fileID = source.fileID;
            source.length = source.length - target.length;
        }
    }

    Block getSource() {
        Block source = last;
        
        while (source != null) {
            if (source.fileID != -1) {
                break;
            }
            source = source.prev;
        }
        
        return source;
    }

    Block getTarget(int length) {
        Block target = first;
        
        while (target != null) {
            if (target.fileID == -1 && target.length >= length) {
                break;
            }
            target = target.next;
        }
        
        return target;
    }
    
    long checksum() {
        long result = 0;
        
        Block b = first;
        while (b != null) {
            if (b.fileID != -1) {
                result += b.fileID * b.offset;
            }
        }
        
        return result;
    }

    void dump() {
        Block b = first;
        while (b != null) {
            System.out.printf("offset=%10d length=%10d fileID=%5d\n", b.offset, b.length, b.fileID);
            b = b.next;
        }
    }
        
    void load(String s) {
        boolean used = true;
        int files = 0;
        Block oldFirst = first;
        
        for (char d: s.toCharArray()) {
            Block temp = new Block();
            temp.length = d - '0';
            if (used) {
                temp.fileID = files++;
            } else {
                temp.fileID = -1;
            }
            copy(temp, oldFirst);
            used = !used;
        }
    }
    
    void solve(String name) throws IOException {
        init();
        load(Files.readString(Path.of(name)));
        
        dump();
        
        Block target = getTarget(1);
        while (target != null) {
            if (last.fileID != -1 && last.length > 0) {
                System.out.printf("Copying %d bytes of file %d to offset %d.\n", last.length, last.fileID, target.offset);
                copy(last, target);
            } else {
                last = last.prev;
                last.next = null;
            }
        }
        
        long part1 = checksum();
        long part2 = 0;
        
        System.out.printf("\nPart 1: %10d\nPart 2: %10d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.02 n/a ***");
        System.out.println();

        new Puzzle1().solve(args[0]);
    }
    
}
