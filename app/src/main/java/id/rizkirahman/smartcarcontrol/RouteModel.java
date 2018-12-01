package id.rizkirahman.smartcarcontrol;

public class RouteModel {

    private static final RouteModel ourInstance = new RouteModel();

    static RouteModel getInstance() {
        return ourInstance;
    }

    private String departureName;
    private String destinationName;

    private float departureLat;
    private float departureLon;
    private float destinationLat;
    private float destinationLon;
    private float distance;

    private int time;

    public String getDepartureName() {
        return departureName;
    }

    public void setDepartureName(String departureName) {
        this.departureName = departureName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public float getDepartureLat() {
        return departureLat;
    }

    public void setDepartureLat(float departureLat) {
        this.departureLat = departureLat;
    }

    public float getDepartureLon() {
        return departureLon;
    }

    public void setDepartureLon(float departureLon) {
        this.departureLon = departureLon;
    }

    public float getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(float destinationLat) {
        this.destinationLat = destinationLat;
    }

    public float getDestinationLon() {
        return destinationLon;
    }

    public void setDestinationLon(float destinationLon) {
        this.destinationLon = destinationLon;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void calculateDistance() {
        if(destinationLat != 0 && departureLat != 0) {
            float diffLat = destinationLat - departureLat;
            float diffLon = destinationLon - departureLon;
            float squareLat = (float) Math.pow(diffLat, 2.0);
            float squareLon = (float) Math.pow(diffLon, 2.0);
            distance = (float) Math.sqrt(squareLat + squareLon) * 111;
        }
        else {
            distance = 0;
        }
    }

    public void calculateTime() {
        time = (int) distance*15;
    }
}
