
public class Airport {
    public String AirportCode;
    public String AirfieldName;
    public double lat;
    public double lon;
    public double parkingCost;

    public Airport(String AirportCode, String AirfieldName, double lat, double lon, double parkingCost) {
        this.AirportCode = AirportCode;
        this.AirfieldName = AirfieldName;
        this.lat = lat;
        this.lon = lon;
        this.parkingCost = parkingCost;
    }
}
