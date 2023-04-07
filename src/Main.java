import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("input.txt");
        Scanner scanner = new Scanner(file);
        int row, col = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.trim();
            line = line.split("~")[0];
            int parenthesesCount = 0;
            String[] tokensWithComments= line.split(" ");
            for (int i = 0; i < tokensWithComments.length ; i++) {
                    System.out.println(tokensWithComments[i]);
            }

        }
    }
}