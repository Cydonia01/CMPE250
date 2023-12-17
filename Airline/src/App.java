import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;

public class App {
    public static void main(String[] args) throws Exception {
        LoungeAviation company = new LoungeAviation();
        Scanner reader;

        // reading the weather file
        File weatherFile = new File("cases/weather.csv");
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
        File airportsFile = new File("cases/airports/TR-0.csv");
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
        File directions = new File("cases/directions/TR-0.csv");
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
        File missionFile = new File("cases/missions/TR-0.in");
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
        /* for (String airportCode: company.directions.keySet()) {
            for (String neighbor: company.directions.get(airportCode)) {
                System.out.println(airportCode + " " + neighbor);
            }
        }*/
    }
}
