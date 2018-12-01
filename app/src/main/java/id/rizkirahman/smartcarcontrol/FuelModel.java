package id.rizkirahman.smartcarcontrol;

public class FuelModel {

    private static final FuelModel ourInstance = new FuelModel();

    static FuelModel getInstance() {
        return ourInstance;
    }

    private int fuelLevel;

    public int getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(int fuelLevel) {
        this.fuelLevel = fuelLevel;
    }
}
