package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle1 {

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
                if (expected[next++] != i) {
                    return false;
                }
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
    
    boolean run(long a) throws IOException {
        regA = a;
        regB = 0;
        regC = 0;
        
        pc = 0;
        
        next = 0;
        
        output.setLength(0);
        
        while (pc < code.length) {
            if (!execute()) {
                return false;
            }
        }
        
        return next == code.length;
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
        
        for (long i = 1l<<47; i < Long.MAX_VALUE; i++) {
            if (i % 1000000 == 0) {
                System.out.println(i);
            }
            if (run(i)) {
                System.out.println(output.toString());
                System.out.println("BINGO!!! for a=" + i);
                break;
            } else {
                System.out.println(output.toString());
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle1().load(args[0]);
    }
    
}
