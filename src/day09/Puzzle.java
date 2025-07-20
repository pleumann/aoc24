package day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Day 09 "Disk Fragmenter"
 */
public class Puzzle {

    /**
     * Represents a range of blocks on the disk.
     */
    class Range {
        
        int offset;

        int length;

        int fileID;
        
        public Range(int offset, int length, int fileID) {
            this.offset = offset;
            this.length = length;
            this.fileID = fileID;
        }
    }
       
    /**
     * Represents the whole disk.
     */
    ArrayList<Range> disk = new ArrayList();
        
    /**
     * Copies a a range of blocks (complete or partial) from a source to a
     * target.
     */
    void copy(int from, int to) {
        Range source = disk.get(from);
        Range target = disk.get(to);

        if (source.length < target.length) {
            /*
             * Case 1: Source range smaller than target range. Copy and create a
             * new range of free blocks.
             */
            Range temp = new Range(target.offset + source.length, target.length - source.length, -1);
            disk.add(to + 1, temp);

            target.fileID = source.fileID;
            target.length = source.length;
            
            source.length = 0;
        } else {
            /*
             * Case 2: Source range larger than or equal to target range. Copy
             * what fits in and leave the rest in place.
             * new range of free blocks.
             */
            target.fileID = source.fileID;
            source.length = source.length - target.length;
        }
    }

    /**
     * Finds a range of free blocks larger than or equal to the given number.
     */
    int getFree(int length) {
        for (int i = 0; i < disk.size(); i++) {
            Range target = disk.get(i);
            if (target.fileID == -1 && target.length >= length) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Calculates the checksum accroding to the given rule (sum of file ID *
     * block offset).
     */
    long checksum() {
        long result = 0;
        
        for (int i = 0; i < disk.size(); i++) {
            Range block = disk.get(i);
            if (block.fileID != -1) {
                int offset = block.offset;
                int length = block.length;
                while (length != 0) {
                    result += block.fileID * offset++;
                    length--;
                }
            }
        }
        
        return result;
    }

    /**
     * Dumps everything to the screen.
     */
    void dump(String title) {
        System.out.printf("\033[1;37;40m%-48s\033[0m\n", title);
        
        for (Range block: disk) {
            if (block.fileID != -1) {
                System.out.print("\033[1;37;41m");
            } else {
                System.out.print("\033[1;37;42m");
            }
            System.out.printf("offset=%10d length=%10d fileID=%5d", block.offset, block.length, block.fileID);
            System.out.println("\033[0m");
        }
        
        System.out.println();
    }

    /**
     * Loads the initial state (aka the puzzle input) from the given file.
     */
    void load(String s) {
        int offset = 0;
        int fileID = 0;
        
        disk.clear();
        
        for (char d: s.toCharArray()) {
            int length = d - '0';
            
            if (disk.size() % 2 == 0) {
                disk.add(new Range(offset, length, fileID++));
            } else {
                disk.add(new Range(offset, length, -1));
            }
            offset = offset + length;
        }
    }

    /**
     * Solves part 1. File ranges are copied on block level.
     */
    long solvePart1(String backup) {
        load(backup);
        dump("File system before part 1");

        int to = getFree(1);
        while (to != -1) {
            int from = disk.size() - 1;
            Range source = disk.get(from);
            if (source.fileID != -1 && source.length != 0) {
                copy(from, to);
            }
            if (source.fileID == -1 || source.length == 0) {
                disk.remove(from);
            }

            to = getFree(1);
        }

        dump("File system after part 1");
        
        return checksum();
    }

    /**
     * Solves part 2. File ranges are copied in their entirety and need to fit
     * in the target range.
     */
    long solvePart2(String backup) {
        load(backup);
        dump("File system before part 2");

        int from = disk.size() - 1;
        while (from >= 0) {
            Range source = disk.get(from);
            if (source.fileID != -1 && source.length != 0) {
                int to = getFree(source.length);
                if (to == -1 || to >= from) {
                    from--;
                    continue;
                }
                copy(from, to);
                source.fileID = -1;
                from--;
            } else {
                from--;
            }
        }

        dump("File system after part 2");
        
        return checksum();
    }
    
    /**
     * Solves everything.
     */
    void solve(String name) throws IOException {
        String backup = Files.readString(Path.of(name));
        
        long part1 = solvePart1(backup);
        long part2 = solvePart2(backup);
        
        System.out.printf("Part 1: %10d\nPart 2: %10d\n\n", part1, part2);
    }
    
    /**
     * Provides the canonical entry point.
     */
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("*** AoC 2024.09 Disk Fragmenter ***");
        System.out.println();

        new Puzzle().solve(args[0]);
    }
    
}
