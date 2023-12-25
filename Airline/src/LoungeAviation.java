import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Set;
import java.lang.Math;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

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


    public void task1(String from, String to, long departureTime, long arrivalTime) throws IOException {
        unixTime = departureTime;
        findPath(from, to);
    }

    public void task2(String plane, String from, String to, long departureTime, long arrivalTime) throws IOException {
        unixTime = departureTime;
        dfs(plane, from, to, arrivalTime);
    }

    // Task 2
    public void dfs(String plane, String from, String to, long arrivalTime) throws IOException {
        Stack<String> stack = new Stack<>();
        stack.push(from);
        while (stack.size() > 0) {
            if (stack.peek().equals(to)) {
                break;
            }
            String current = stack.pop();
            if (current.equals(to)) {
                return;
            }
            for (String neighbor: directions.get(current)) {
                double dist = evaluateDistance(plane, current, neighbor);
                long passedTime = evaluatePassedTime(plane, dist);
                double cost = evaluateCostT2(plane, current, neighbor, dist, passedTime);
                if (unixTime + passedTime > arrivalTime) {
                    continue;
                }
                stack.push(neighbor);
            }
        }
        printPath2(parents, from, to);
        System.out.println(String.format(Locale.US, " %.5f", distances.get(to)));
    }
    
    private void printPath2(HashMap<String, String> parents, String from, String to) throws IOException {
        if (parents.get(to) == null) {
            System.out.print(to);
            return;
        }
        printPath2(parents, from, parents.get(to));
        System.out.print(" " + to);
    }

    private double evaluateDistance(String plane, String from, String to) {
        double lat1 = airports.get(from).lat;
        double lon1 = airports.get(from).lon;
        double lat2 = airports.get(to).lat;
        double lon2 = airports.get(to).lon;
        
        // evaluating distance (Haversine formula)
        double dist = haversine(lat1, lon1, lat2, lon2);
        return dist;
    }

    private double evaluateCostT2(String plane, String from, String to, double dist, long passedTime) {
        double W_d = evaluateWeatherMultiplier2(from, 0); // weather multiplier for the departure airport
        
        double W_l = evaluateWeatherMultiplier2(to, passedTime); // weather multiplier for the landing airport
        
        double cost = 300.0 * W_d * W_l + dist;
        return cost;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double inRoot = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
        double dist = 2 * R * Math.asin(Math.sqrt(inRoot));
        return dist;
    }

    private double evaluateWeatherMultiplier2(String airportCode, long passedTime) {
        String airfieldName = airports.get(airportCode).airfieldName;
        int weatherCode = weathers.get(airfieldName).get(unixTime + passedTime);
        int wind = (weatherCode & 16) >> 4;
        int rain = (weatherCode & 8) >> 3;
        int snow = (weatherCode & 4) >> 2;
        int hail = (weatherCode & 2) >> 1;
        int bolt = (weatherCode & 1);
        double weatherMultiplier = (1 + 0.05 * wind) * (1 + 0.05 * rain) * (1 + 0.10 * snow) * (1 + 0.15 * hail) * (1 + 0.20 * bolt);
        weatherOfAirfields.put(airfieldName, weatherMultiplier);
        return weatherMultiplier;
    }







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
    
    public long evaluatePassedTime(String plane, double distance) {
        long passedTime = 0;
        if (plane.equals("Carreidas 160")) {
            if (distance <= 175) {
                passedTime = 21600;
            }
            if (distance > 175 && distance <= 350) {
                passedTime = 43200;
            }
            if (distance > 350) {
                passedTime = 64800;
            }
        }
        else if (plane.equals("Orion III")) {
            if (distance <= 1500) {
                passedTime = 21600;
            }
            if (distance > 1500 && distance <= 3000) {
                passedTime = 43200;
            }
            if (distance > 3000) {
                passedTime = 64800;
            }
        }
        else if (plane.equals("Skyfleet S570")) {
            if (distance <= 500) {
                passedTime = 21600;
            }
            if (distance > 500 && distance <= 1000) {
                passedTime = 43200;
            }
            if (distance > 1000) {
                passedTime = 64800;
            }
        }
        else if (plane.equals("T-16 Skyhopper")) {
            if (distance <= 2500) {
                passedTime = 21600;
            }
            if (distance > 2500 && distance <= 5000) {
                passedTime = 43200;
            }
            if (distance > 5000) {
                passedTime = 64800;
            }
        }
        return passedTime;
    }




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
                double cost = evaluateCostT1(current, neighbor);

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

    private double evaluateCostT1(String from, String to) {
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

    private void printPath(HashMap<String, String> parents, String from, String to) throws IOException {
        if (parents.get(to) == null) {
            writer.write(to);
            return;
        }
        printPath(parents, from, parents.get(to));
        writer.write(" " + to);
    } 
}
