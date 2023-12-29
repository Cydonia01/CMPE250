/**
 * This class represents the main application for the Airline program.
 * It reads data from various files and performs operations based on the input.
 * The program calculates flight routes and writes the results to an output file.
 *
 * @author Mehmet Ali Özdemir
 * @since 29.12.2023
 */
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner reader;
        FileWriter writer = new FileWriter(args[4]);
        FileWriter writer2 = new FileWriter(args[5]);
        LoungeAviation company = new LoungeAviation(writer);

        // reading the weather file
        File weatherFile = new File(args[2]);
        reader = new Scanner(weatherFile);
        reader.nextLine();
        while (reader.hasNextLine()) {
            String[] data = reader.nextLine().split(",");
            String airfieldName = data[0];
            long unixTime = Long.parseLong(data[1]);
            int weatherCode = Integer.parseInt(data[2]);
            if (company.weathers.containsKey(airfieldName)) {
                company.weathers.get(airfieldName).put(unixTime, weatherCode);
            } else {
                HashMap<Long, Integer> temp = new HashMap<Long, Integer>();
                temp.put(unixTime, weatherCode);
                company.weathers.put(airfieldName, temp);
            }
        }
        reader.close();

        // reading the airports file
        File airportsFile = new File(args[0]);
        reader = new Scanner(airportsFile);
        reader.nextLine();        
        while (reader.hasNextLine()) {
            String[] data = reader.nextLine().split(",");
            String airportCode = data[0];
            String airfieldName = data[1];
            double lat = Double.parseDouble(data[2]);
            double lon = Double.parseDouble(data[3]);
            double parkingCost = Double.parseDouble(data[4]);
            Airport airport = new Airport(airportCode, airfieldName, lat, lon, parkingCost);
            company.airports.put(airportCode, airport);
            company.directions.put(airportCode, new LinkedList<String>());
        }
        reader.close();

        // reading the directions file
        File directions = new File(args[1]);
        reader = new Scanner(directions);
        reader.nextLine();

        while(reader.hasNextLine()) {
            String[] data = reader.nextLine().split(",");
            String from = data[0];
            String to = data[1];
            company.directions.get(from).add(to);
        }
        reader.close();

        // reading the mission file
        File missionFile = new File(args[3]);
        reader = new Scanner(missionFile);
        String plane = reader.nextLine();
        while(reader.hasNextLine()) {
            String[] data = reader.nextLine().split(" ");
            String from = data[0];
            String to = data[1];
            long departureTime = Long.parseLong(data[2]);
            long arrivalTime = Long.parseLong(data[3]);
            company.task1(from, to, departureTime, arrivalTime);
        }
        reader.close();
        writer.close();
        writer2.close();
    }
}
