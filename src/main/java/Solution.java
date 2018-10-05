import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {

    public static void main(String... args) {
        System.out.println(args[1]);
        Scanner input = null;
        if (0 < args.length) {
            try {
                input = new Scanner(new File(args[1]));
            } catch (FileNotFoundException ex) {
                System.out.println("No File Found!");
                return;
            }

        } else {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(0);
        }

        while(input.hasNext()){
            System.out.println(input.nextLine());
        }


        Scanner in = new Scanner(System.in);
        String line = null;
        while (line == null || !line.equals("tally")) {

            line = in.nextLine();
            System.out.println(line);
        }
    }
}
