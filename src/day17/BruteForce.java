package day17;

public class BruteForce {

   int[] numbers = { 2,4,1,1,7,5,1,5,4,3,5,5,0,3,3,0 };
          
   boolean go(long start) {
       
       long a = start;
       long b = 0;
       long c = 0;
       
       int digit = 0;
       
       while (a != 0) {
           b = b % a;
           b = b ^ 1;
           c = a / (1 << b);
           b = b ^ 5;
           b = b ^ c;
           
           if (numbers[digit] != b) {
               return false;
           } else {
               digit++;
           }
           
           
           a = a / (1 << 3);
       }
       
       return digit == numbers.length;
   }
   
   public static void main(String[] start) {
       
       BruteForce p = new BruteForce();
       
       long l = 1l << 45;
       
       while (true) {
           if (l % 1000000000 == 0) {
               System.out.println(l);
           }
           
           if (p.go(l)) {
               System.out.println("BINGO: " + l);
           }
           
           l++;
       }
   }
   
}
