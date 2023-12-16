public class Airfield {
    public String AirfieldName;
    public long time;
    public int weatherCode;
    public boolean wind, rain, snow, hail, bolt;

    public Airfield(String airfieldName, long time, int weatherCode, boolean wind, boolean rain, boolean snow, boolean hail, boolean bolt) {
        AirfieldName = airfieldName;
        this.time = time;
        this.weatherCode = weatherCode;
        this.wind = wind;
        this.rain = rain;
        this.snow = snow;
        this.hail = hail;
        this.bolt = bolt;
    }
}
