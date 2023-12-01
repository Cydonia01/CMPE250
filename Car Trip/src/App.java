import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        //FileWriter writer = new FileWriter[args[1]];

        try {
            File inputFile = new File(args[0]);
            Scanner reader = new Scanner(inputFile);

            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(" ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
