import java.io.File;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;

public class App {
    public static void main(String[] args) throws Exception {
        File airports = new File("../cases/airports/TR-0.csv");
        Scanner reader = new Scanner(airports);
        reader.nextLine();
        
        // reading the airports file
        while (reader.hasNextLine()) {
            String[] data = reader.nextLine().split(",");
            String airportCode = data[0];
            String airfieldName = data[1];
            double lat = Double.parseDouble(data[2]);
            double lon = Double.parseDouble(data[3]);
            double parkingCost = Double.parseDouble(data[4]);
            Airport airport = new Airport(airportCode, airfieldName, lat, lon, parkingCost);

        }
        reader.close();

        // reading the directions file
        File directions = new File("../cases/directions/TR-0.csv");
        reader = new Scanner(directions);
        reader.nextLine();

        while(reader.hasNextLine()) {
            String[] data = reader.nextLine().split(",");
            String from = data[0];
            String to = data[1];
        }

        reader.close();

        // reading the weather file
    }
}
