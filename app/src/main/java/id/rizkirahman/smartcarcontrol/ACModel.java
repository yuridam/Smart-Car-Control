package id.rizkirahman.smartcarcontrol;

/**
 * Created by Rizki Rahman on 12/06/2017.
 */

public class ACModel {

    private static final ACModel ourInstance = new ACModel();

    static ACModel getInstance() {
        return ourInstance;
    }

    private int temperature;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
