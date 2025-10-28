package day21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author joerg
 */
public class Puzzle1 {

    class DirPad {
        
        DirPad parent;
        
        int x;
        
        int y;

        DirPad(DirPad parent, int x, int y) {
            this.parent = parent;
            this.x = x;
            this.y = y;
        }
        
        int push(int x, int y) {
            int len = 0;
            
            if (parent == null) {
                while (this.x < x) {
                    System.out.print('v');
                    this.x++;
                    len++;
                }
                
                while (this.x > x) {
                    System.out.print('^');
                    this.x--;
                    len++;
                }
                
                while (this.y < y) {
                    System.out.print('>');
                    this.y++;
                    len++;
                }

                while (this.y > y) {
                    System.out.print('<');
                    this.y--;
                    len++;
                }
                
                System.out.print('A');
                len++;
            } else {
                while (this.x < x) {
                    len += parent.push(1, 1);
                    this.x++;
                }
                
                while (this.x > x) {
                    len += parent.push(0, 1);
                    this.x--;
                }
                
                while (this.y < y) {
                    len += parent.push(1, 2);
                    this.y++;
                }

                while (this.y > y) {
                    len += parent.push(1, 0);
                    this.y--;
                }
                
                len += parent.push(0, 2);
            }
            
            return len;
        }

        int pushAlt(int x, int y) {
            int len = 0;
            
            if (parent == null) {
                while (this.y < y) {
                    System.out.print('>');
                    this.y++;
                    len++;
                }

                while (this.y > y) {
                    System.out.print('<');
                    this.y--;
                    len++;
                }
                
                while (this.x < x) {
                    System.out.print('v');
                    this.x++;
                    len++;
                }
                
                while (this.x > x) {
                    System.out.print('^');
                    this.x--;
                    len++;
                }
                
                System.out.print('A');
                len++;
            } else {
                while (this.y < y) {
                    len += Math.min(parent.push(1, 2), parent.pushAlt(1, 2));
                    this.y++;
                }

                while (this.y > y) {
                    len += Math.min(parent.push(1, 0), parent.pushAlt(1, 0));
                    this.y--;
                }
                
                while (this.x < x) {
                    len += Math.min(parent.push(1, 1), parent.pushAlt(1, 1));
                    this.x++;
                }
                
                while (this.x > x) {
                    len += Math.min(parent.push(0, 1), parent.pushAlt(0, 1));
                    this.x--;
                }
                
                len += Math.min(parent.push(0, 2), parent.pushAlt(0, 2));
            }
            
            return len;
        }

    }
    
    private void solve(BufferedReader bufferedReader) throws IOException {
        final String NUMPAD = "789456123 0A";
        
        DirPad human = new DirPad(null, 0, 2);
        DirPad robot1 = new DirPad(null, 0, 2);
        DirPad robot2 = new DirPad(robot1, 0, 2);
        DirPad door = new DirPad(robot2, 3, 2);
        
        int part1 = 0;
        
        String s = bufferedReader.readLine();
        while (s != null) {
            System.out.print(s + ": ");

            int len = 0;
            for (int i = 0; i < s.length(); i++) {
                int p = NUMPAD.indexOf(s.charAt(i));
                int x = p / 3;
                int y = p % 3;
                len += Math.min(door.push(x, y), door.pushAlt(x, y));
            }

            System.out.println(" -> " + len);
            System.out.println();

            part1 += Integer.parseInt(s.substring(0, 3)) * len;
            
            s = bufferedReader.readLine();
        }
        
        System.out.println("Part 1: " + part1);
    }
    
    public static void main(String[] args) throws IOException {
        new Puzzle1().solve(new BufferedReader(new FileReader(args[0])));
    }

}
