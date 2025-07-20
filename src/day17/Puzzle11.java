package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public class Puzzle11 {

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

        long[] seeds = { 23948989,
            23949245,
            91057853,
            91058109,
            158166717,
            158166973,
            225275581,
            225275837,
            292384445,
            292384701,
            359493309,
            359493565,
            426602173,
            426602429,
            493711037,
            493711293,
            560819901,
            560820157,
            627928765,
            627929021,
            695037629,
            695037885,
            762146493,
            762146749,
            820866749,
            820867005,
            829255357,
            829255613,
            837643965,
            837644221,
            854421181,
            854421437,
            871198397,
            871198653,
            887975613,
            887975869,
            896364221,
            896364477,
            904752829,
            904753085,
            921530045,
            921530301,
            938307261,
            938307517,
            963473085,
            963473341,
            1030581949,
            1030582205,
            1097690813,
            1097691069,
            1164799677,
            1164799933,
            1231908541,
            1231908797,
            1299017405,
            1299017661,
            1366126269,
            1366126525,
            1433235133,
            1433235389,
            1500343997,
            1500344253,
            1567452861,
            1567453117,
            1634561725,
            1634561981,
            1701670589,
            1701670845,
            1768779453,
            1768779709,
            1835888317,
            1835888573,
            1892841909,
            1894608573,
            1894608829,
            1902997181,
            1902997437,
            1911385789,
            1911386045,
            1928163005,
            1928163261,
            1944940221,
            1944940477,
            1959950773,
            1961717437,
            1961717693,
            1970106045,
            1970106301,
            1978494653,
            1978494909,
            1995271869,
            1995272125,
            2012049085,
            2012049341,
            2027059637,
            2037214909,
            2037215165,
            2094168501,
            2104323773,
            2104324029,
        };
                
        //long start = 0b011011010110111110111101;      
        
        //HashSet<String> keys = new HashSet();
        
        //long start = 0;
        //for (long start: seeds) {
            for (long i = 0; i < (1 << 32); i++) {
                long j = i; // << 24 | start;
    //            if (i % 1000000 == 0) {
    //                System.out.println(j);
    //            }
                if (run(j, 8)) {
                    System.out.printf("%48s %16d %s\n", Long.toBinaryString(j), j, output);
                    //System.out.printf("a=%d b=%d c=%d\n", regA, regB, regC);
    //                System.out.println(i + ",");
                }
            }
        //
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle11().load(args[0]);
    }
    
}
