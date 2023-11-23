/**
 * This project is a simulation of a company's branches and employees.
 * Main function reads the input files and writes the output to a file.
 * 
 * @author Mehmet Ali Özdemir
 * @since 23.11.2023
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class project2 {
    public static void main(String[] args) throws Exception {
        HashTable<String, Branch> branches = new HashTable<>();
        FileWriter writer = new FileWriter(args[2]);

        try {
            File inputFile = new File(args[0]);
            Scanner reader = new Scanner(inputFile);

            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(", ");
                String city = data[0];
                String district = data[1];
                String name = data[2];
                String position = data[3];
                String branch = String.format("%s %s", city, district);

                if (!branches.contains(branch))
                    branches.insert(branch, new Branch(city, district, writer));

                branches.get(branch).add(name, position);;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    
        try {
            File inputFile = new File(args[1]);
            Scanner reader = new Scanner(inputFile);

            while (reader.hasNextLine()) {
                String[] command = reader.nextLine().split(": ");

                if (command[0].equals(""))
                    continue;

                else if (command[0].endsWith(":")) {
                    for (int i = 0; i < branches.getLists().length; i++) {
                        for (Items<String, Branch> item: branches.getLists()[i])
                            item.getValue().setMonthlyBonus();
                    }
                }

                else {
                    String operation = command[0];
                    chooseOperation(branches, operation, command[1].split(", "), writer);
                }

            }
            reader.close();
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This function chooses the operation to be done according to the given operation.
     * 
     * @param branches HashTable of branches
     * @param operation operation to be done
     * @param data data to be used in the operation
     * @param writer FileWriter to write the output
     * @throws IOException
     */
    public static void chooseOperation(HashTable<String, Branch> branches, String operation, String[] data, FileWriter writer) throws IOException {
        String city = data[0];
        String district = data[1];
        String name;
        String position;
        int monthlyScore;
        String branch = String.format("%s %s", city, district);
        
        if (operation.equals("ADD")) {
            name = data[2];
            position = data[3];
            branches.get(branch).add(name, position);
        }

        if (operation.equals("LEAVE")) {
            name = data[2];
            branches.get(branch).leave(name);   
        }

        if (operation.equals("PERFORMANCE_UPDATE")) {
            name = data[2];
            monthlyScore = Integer.parseInt(data[3]);
            branches.get(branch).performanceUpdate(name, monthlyScore);
        }

        if (operation.equals("PRINT_MANAGER")) {
            branches.get(branch).printManager();
        }

        if (operation.equals("PRINT_MONTHLY_BONUSES")) {
            branches.get(branch).printMonthlyBonuses();
        }

        if (operation.equals("PRINT_OVERALL_BONUSES")) {
            branches.get(branch).printOverallBonuses();
        }
    }
}