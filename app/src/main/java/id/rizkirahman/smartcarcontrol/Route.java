package id.rizkirahman.smartcarcontrol;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Route extends AppCompatActivity {

    private RouteModel r = RouteModel.getInstance();
    private SharedPref sp = SharedPref.getInstance();

    private String TAG = Route.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "https://www.fbi.h-da.de/fileadmin/personal/h.wiedling/daten/poi.txt";
    ArrayList<HashMap<String, String>> poiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        poiList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listRoute);

        new GetDestinations().execute();

        final Button backToMenu = (Button) findViewById(R.id.backToMenu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Async task to get JSON by making http call
    private class GetDestinations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Route.this);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to URL and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from URL: " + jsonStr);

            if (jsonStr != null) {
                try {
                    // Getting JSON array node
                    JSONArray pointsOfInterest = new JSONArray(jsonStr);

                    // Looping through all points of interest
                    for (int i = 0; i < pointsOfInterest.length(); i++) {
                        JSONObject p = pointsOfInterest.getJSONObject(i);

                        String type = p.getString("type");
                        String name = p.getString("name");
                        String address = p.getString("address");
                        String lat = p.getString("lat");
                        String lon = p.getString("lon");

                        HashMap<String, String> point = new HashMap<>();

                        point.put("type", type);
                        point.put("name", name);
                        point.put("address", address);
                        point.put("lat", lat);
                        point.put("lon", lon);

                        poiList.add(point);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "JSON parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get JSON from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get JSON from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            // Updating parsed JSON data into ListView
            final ListAdapter adapter = new SimpleAdapter(
                    Route.this, poiList,
                    R.layout.list_view, new String[]{"type", "name", "address", "lat", "lon"},
                    new int[]{R.id.type, R.id.name, R.id.address});

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
                    final String name = (String) obj.get("name");
                    final String lat = (String) obj.get("lat");
                    final String lon = (String) obj.get("lon");
                    final float latFloat = Float.parseFloat(lat);
                    final float lonFloat = Float.parseFloat(lon);

                    AlertDialog routeDialog = new AlertDialog.Builder(Route.this).create();
                    routeDialog.setMessage(name + " " + getString(R.string.routeDialogMessage));

                    routeDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.routeDestination), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            r.setDestinationName(name);
                            r.setDestinationLat(latFloat);
                            r.setDestinationLon(lonFloat);
                            Toast.makeText(Route.this, R.string.destinationSet, Toast.LENGTH_SHORT).show();
                        }
                    });

                    routeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.routeDeparture), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            r.setDepartureName(name);
                            r.setDepartureLat(latFloat);
                            r.setDepartureLon(lonFloat);
                            Toast.makeText(Route.this, R.string.departureSet, Toast.LENGTH_SHORT).show();
                        }
                    });

                    routeDialog.setCancelable(true);
                    routeDialog.show();
                }
            });

        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        sp.writeValues(Route.this);
    }
}
