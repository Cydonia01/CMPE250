/**
 * This class represents the Vertex in a graph.
 * It creates a vertex with the given data.
 * 
 * @author Mehmet Ali Özdemir
 * @since 29.12.2023
 */
public class Airport {
    public String airportCode;
    public String airfieldName;
    public double lat;
    public double lon;
    public double parkingCost;

    public Airport(String airportCode, String airfieldName, double lat, double lon, double parkingCost) {
        this.airportCode = airportCode;
        this.airfieldName = airfieldName;
        this.lat = lat;
        this.lon = lon;
        this.parkingCost = parkingCost;
    }
}
