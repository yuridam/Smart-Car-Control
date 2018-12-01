package id.rizkirahman.smartcarcontrol;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private MenuModel m = MenuModel.getInstance();
    private FuelModel f = FuelModel.getInstance();
    private RouteModel r = RouteModel.getInstance();
    private DoorModel d = DoorModel.getInstance();
    private ACModel ac = ACModel.getInstance();

    private static final SharedPref ourInstance = new SharedPref();

    static SharedPref getInstance() {
        return ourInstance;
    }

    public void writeValues(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);

        //nSave all values from Model classes to the Shared Preferences
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();

        editor.putBoolean("FrontLeft", d.isFrontLeft());
        editor.putBoolean("FrontRight", d.isFrontRight());
        editor.putBoolean("RearLeft", d.isRearLeft());
        editor.putBoolean("RearRight", d.isRearRight());
        editor.putBoolean("Boot", d.isBoot());
        editor.putBoolean("IsAllLocked", d.isAllDoorsLocked());
        editor.putInt("Temperature", ac.getTemperature());
        editor.putInt("Fuel", f.getFuelLevel());
        editor.putString("Departure", r.getDepartureName());
        editor.putString("Destination", r.getDestinationName());
        editor.putFloat("Departure Lat", r.getDepartureLat());
        editor.putFloat("Departure Lon", r.getDepartureLon());
        editor.putFloat("Destination Lat", r.getDestinationLat());
        editor.putFloat("Destination Lon", r.getDestinationLon());
        editor.putFloat("Distance", r.getDistance());
        editor.putBoolean("Engine", m.isEngine());
        editor.putBoolean("PauseResume", m.isPaused());

        editor.apply();

    }

    public void readValues(Context context){

        // Put all saved values in SharedPref to the Model classes

        SharedPreferences sharedPref = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        d.setFrontLeft(sharedPref.getBoolean("FrontLeft", false));
        d.setFrontRight(sharedPref.getBoolean("FrontRight", false));
        d.setRearLeft(sharedPref.getBoolean("RearLeft", false));
        d.setRearRight(sharedPref.getBoolean("RearRight", false));
        d.setBoot(sharedPref.getBoolean("Boot", false));
        d.setAllDoorsLocked(sharedPref.getBoolean("IsAllLocked", false));
        ac.setTemperature(sharedPref.getInt("Temperature", 0));
        f.setFuelLevel(sharedPref.getInt("Fuel", 0));
        r.setDepartureName(sharedPref.getString("Departure", null));
        r.setDestinationName(sharedPref.getString("Destination", null));
        r.setDepartureLat(sharedPref.getFloat("Departure Lat", 0));
        r.setDepartureLon(sharedPref.getFloat("Departure Lon", 0));
        r.setDestinationLat(sharedPref.getFloat("Destination Lat", 0));
        r.setDestinationLon(sharedPref.getFloat("Destination Lon", 0));
        r.setDistance(sharedPref.getFloat("Distance", 0));
        m.setEngine(sharedPref.getBoolean("Engine", false));
        m.setPaused(sharedPref.getBoolean("PauseResume", false));

    }


}
