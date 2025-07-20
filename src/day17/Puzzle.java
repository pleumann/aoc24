package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Puzzle {

    long regA;
    long regB;
    long regC;
    
    int[] code;
    
    int pc;

    int[] expected;
    
    int next;
    
    StringBuilder output = new StringBuilder();
    
    long combo(int literal) {
        switch (literal) {
            case 4  -> { return regA;    }
            case 5  -> { return regB;    }
            case 6  -> { return regC;    }
            default -> { return literal; }
        }
    }
    
    boolean execute() {
        int opcode = code[pc++];

        int literal = 0;
        if (pc < code.length) {
            literal = code[pc++];
        }

        //System.out.printf("a=%8d b=%8d c=%8d pc=%4d opcode=%d literal=%8d\n", regA, regB, regC, pc, opcode, literal);
        
        switch (opcode) {
            case 0 /* adv */ -> {
                regA = regA / (1 << combo(literal));
            }
            case 1 /* bxl */ -> {
                regB = regB ^ literal;
            }
            case 2 /* bst */ -> {
                regB = combo(literal) % 8;
            }
            case 3 /* jnz */ -> {
                if (regA != 0) {
                    pc = literal;
                }
            }
            case 4 /* bxc */ -> {
                regB = regB ^ regC;
            }
            case 5 /* out */ -> {
                if (output.length() != 0) {
                    output.append(',');
                }
                int i = (int)(combo(literal) % 8);
                output.append(i);
                if (expected[next] != i) {
                    return false;
                }
                next++;
            }
            case 6 /* bdv */ -> {
                regB = regA / (1 << combo(literal));
            }
            case 7 /* cdv */ -> {
                regC = regA / (1 << combo(literal));
            }
        }
        
        return true;
    }
    
    boolean run(long a, int end) throws IOException {
        regA = a;
        regB = 0;
        regC = 0;
        
        pc = 0;
        
        next = 0;
        
        output.setLength(0);
        
        while (pc < code.length && next < end) {
            if (!execute()) {
                return false;
            }
        }
        
        return next == end;
    }
    
    
    void load(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        
        for (String s: lines) {
            System.out.println(s);
        }
        
        regA = Integer.parseInt(lines.get(0).substring(12));
        regB = Integer.parseInt(lines.get(1).substring(12));
        regC = Integer.parseInt(lines.get(2).substring(12));
        
        String[] arr = lines.get(4).split(" |,");
        
        code = new int[arr.length - 1];
        for (int i = 0; i < code.length; i++) {
            code[i] = Integer.parseInt(arr[i + 1]);
        }

        
        System.out.println();
        System.out.println("Number of instructions is " + code.length + ".");
        System.out.println();
        
        expected = code.clone();

        int place = 0;
        //long start = 0b011011010110111110111101;
        //long start = 0b010110111110111101;
        //long start = 0b01101101 0110111110111101;
        //long start = 0b011011010110111110111101;
        
        //long start = 0b0110111010111101;
        //long start = 0b0110111110111101;
        
//        long start = 0b0110111010111101;
//        long start = 0b0110111110111101;
        // long start = 0b0111100110110101;        
        
        //long start = 0b111001011011010110111010111101;
        //long start = 0b111001011011010110111110111101;      

        HashSet<Long> seeds = new HashSet();
        seeds.add(0l);
                
        //long start = 0b011011010110111110111101;      
        
        //HashSet<String> keys = new HashSet();

        long min = -1;
        
        for (int digits = 0; digits < 16; digits++) {
            System.out.println("Got " + seeds.size() + " seeds");
            HashSet<Long> newSeeds = new HashSet();
            min = Long.MAX_VALUE;
            for (long start: seeds) {
                //System.out.println("Trying seed " + start + " aka " + Long.toBinaryString(start));
                for (long i = 0; i < 4095; i++) {
                    long j = i << (3 * digits) | start;
                    //System.out.println(j);
                    if (run(j, digits + 1)) {
                        System.out.printf("%d -> %s\n", j, output);
                        long k = (i & 0x7) << (3 * digits) | start;
                        newSeeds.add(k);
                        min = Math.min(min, k);
                    }
                }
            }
            seeds = newSeeds;
        }
        
        System.out.println("-->" + min);
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle().load(args[0]);
    }
    
}
