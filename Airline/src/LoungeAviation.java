import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Set;
import java.lang.Math;

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

    public LoungeAviation() {}


    public void task1(String from, String to, long departureTime, long arrivalTime) {
        unixTime = departureTime;
        findPath(from, to);
    }
    
    public void findPath(String from, String to) {
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
        System.out.println(String.format(Locale.US, " %.5f", distances.get(to)));
    }

    private void printPath(HashMap<String, String> parents, String from, String to) {
        if (parents.get(to) == null) {
            System.out.print(to);
            return;
        }
        printPath(parents, from, parents.get(to));
        System.out.print(" " + to);
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
        /*if (from.equals("TR-0044") && to.equals("TR-0035")) {
            System.out.println(dist);
            System.out.println(lat1 + " " + lon1 + " " + lat2 + " " + lon2);
        }
        if (from.equals("TR-0035") && to.equals("LTFC")) {
            System.out.println(dist);
            System.out.println(lat1 + " " + lon1 + " " + lat2 + " " + lon2);
        }
        if (from.equals("LTFC") && to.equals("LTAB")) {
            System.out.println(dist);
            System.out.println(lat1 + " " + lon1 + " " + lat2 + " " + lon2);
        }*/
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

    private double evaluateWeatherMultiplier(String airportCode) {
        if (weatherOfAirfields.containsKey(airports.get(airportCode).airfieldName)) {
            return weatherOfAirfields.get(airports.get(airportCode).airfieldName);
        }
        else {
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
    }

    

    /*
    public void evaluateCostT2(String from, String to) {
        evaluateCostT1(from, to);
    }
    */
    
}