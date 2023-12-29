/**
 * This class represents the Graph.
 * It creates a graph from the given data and finds the shortest path between two airports.
 * The program calculates flight routes and writes the results to an output file.
 * 
 * @author Mehmet Ali Özdemir
 * @since 29.12.2023
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Set;
import java.lang.Math;
import java.io.FileWriter;
import java.io.IOException;

public class LoungeAviation {
    // Key: airfieldName, Value: HashMap<unixTime, weatherCode>
    public HashMap<String, HashMap<Long, Integer>> weathers = new HashMap<String, HashMap<Long, Integer>>();

    // key: airportCode, value: Airport
    public HashMap<String, Airport> airports = new HashMap<String, Airport>();
    
    // key: airportCode, value: LinkedList<airportCode>
    public HashMap<String, LinkedList<String>> directions = new HashMap<String, LinkedList<String>>();

    // key: airportCode, value: weatherMultiplier
    public HashMap<String, Double> weatherOfAirfields = new HashMap<String, Double>();

    public final double R = 6371; // radius of the earth in km
    public long unixTime;
    public FileWriter writer;

    public LoungeAviation(FileWriter writer) {
        this.writer = writer;
    }

    /**
     * This method calls findPath method to find the shortest path.
     * @param from is the departure airport.
     * @param to is the landing airport.
     * @param departureTime is the departure time of the flight.
     * @param arrivalTime is the arrival time of the flight.
     * @throws IOException
     */
    public void task1(String from, String to, long departureTime, long arrivalTime) throws IOException {
        unixTime = departureTime;
        findPath(from, to);
    }

    /**
     * This method finds shortest path between departure and arrival airports. It uses dijkstra's algorithm.
     * @param from is the departure airport.
     * @param to is the landing airport.
     */
    public void findPath(String from, String to) throws IOException {
        // key: airportCode, value: airportCode
        HashMap<String, String> parents = new HashMap<>();
        
        // key: airportCode, value: Double.MAX_VALUE
        HashMap<String, Double> distances = new HashMap<>();
        for (String airportCode: airports.keySet()) {
            distances.put(airportCode, Double.MAX_VALUE);
        }

        PriorityQueue<String> PQueue = new PriorityQueue<>(airports.size(), (a, b) -> distances.get(a).compareTo(distances.get(b))); // Comparator.comparingDouble(distances::get)
        
        distances.put(from, 0.0);
        PQueue.add(from);
        Set<String> visited = new HashSet<>();
        while(!PQueue.isEmpty()) {
            if (visited.contains(to))
                break;
            String current = PQueue.poll();
            if (visited.contains(current))
                continue;
            else 
                visited.add(current);

            for (String neighbor: directions.get(current)) {
                double cost = evaluateCost(current, neighbor);

                if (distances.get(current) + cost < distances.get(neighbor)) {
                    distances.put(neighbor, distances.get(current) + cost);
                    parents.put(neighbor, current);
                    PQueue.add(neighbor);
                }
            }
        }
        printPath(parents, from, to);
        writer.write(String.format(Locale.US, " %.5f", distances.get(to)) + "\n");
    }


    /**
     * This method evaluates the cost of the flight between two airports.
     * @param from is the departure airport.
     * @param to is the landing airport.
     */
    private double evaluateCost(String from, String to) {
        double W_d = evaluateWeatherMultiplier(from); // weather multiplier for the departure airport
        double W_l = evaluateWeatherMultiplier(to); // weather multiplier for the landing airport
        
        // evaluating distance (Haversine formula)
        double lat1 = airports.get(from).lat;
        double lon1 = airports.get(from).lon;
        double lat2 = airports.get(to).lat;
        double lon2 = airports.get(to).lon;

        double dist = haversine(lat1, lon1, lat2, lon2);
        
        double cost = 300.0 * W_d * W_l + dist;
        return cost;
    }

    /**
     * This method evaluates the weather multiplier for the given airport.
     * @param airportCode is the airport code of the airport.
     */
    private double evaluateWeatherMultiplier(String airportCode) {
        String airfieldName = airports.get(airportCode).airfieldName;
        int weatherCode = weathers.get(airfieldName).get(unixTime);
        int wind = (weatherCode & 16) >> 4;
        int rain = (weatherCode & 8) >> 3;
        int snow = (weatherCode & 4) >> 2;
        int hail = (weatherCode & 2) >> 1;
        int bolt = (weatherCode & 1);
        double weatherMultiplier = (1 + 0.05 * wind) * (1 + 0.05 * rain) * (1 + 0.10 * snow) * (1 + 0.15 * hail) * (1 + 0.20 * bolt);
        weatherOfAirfields.put(airfieldName, weatherMultiplier);
        return weatherMultiplier;
    }

    /**
     * This method calculates the distance between two airports using Haversine formula.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return distance between two airports.
     */
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double inRoot = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
        double dist = 2 * R * Math.asin(Math.sqrt(inRoot));
        return dist;
    }

    /**
     * This method prints the shortest path between two airports.
     * It uses recursion to print the path.
     * @param parents
     * @param from
     * @param to
     * @throws IOException
     */
    private void printPath(HashMap<String, String> parents, String from, String to) throws IOException {
        if (parents.get(to) == null) {
            writer.write(to);
            return;
        }
        printPath(parents, from, parents.get(to));
        writer.write(" " + to);
    } 
}
