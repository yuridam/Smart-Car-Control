package id.rizkirahman.smartcarcontrol;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Menu extends AppCompatActivity {

    private MenuModel m = MenuModel.getInstance();
    private FuelModel f = FuelModel.getInstance();
    private RouteModel r = RouteModel.getInstance();
    private DoorModel d = DoorModel.getInstance();
    private SharedPref sp = SharedPref.getInstance();

    private long timeRemaining = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sp.readValues(Menu.this);

        Button door = (Button) findViewById(R.id.door);
        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDoor();
            }
        });

        Button fuel = (Button) findViewById(R.id.fuel);
        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFuel();
            }
        });

        Button ac = (Button) findViewById(R.id.ac);
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAC();
            }
        });

        Button route = (Button) findViewById(R.id.route);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRoute();
            }
        });

        final ToggleButton startToggle = (ToggleButton) findViewById(R.id.startToggle);
        startToggle.setChecked(m.isEngine());
        startToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!d.isAllDoorsLocked() && f.getFuelLevel() != 0 && startToggle.isChecked()) {
                    Toast.makeText(Menu.this, R.string.doorNotice, Toast.LENGTH_SHORT).show();
                    startToggle.setChecked(false);
                    m.setEngine(false);
                } else if (d.isAllDoorsLocked() && f.getFuelLevel() == 0 && startToggle.isChecked()) {
                    Toast.makeText(Menu.this, R.string.fuelNotice, Toast.LENGTH_SHORT).show();
                    startToggle.setChecked(false);
                    m.setEngine(false);
                } else if (!d.isAllDoorsLocked() && f.getFuelLevel() == 0 && startToggle.isChecked()) {
                    Toast.makeText(Menu.this, R.string.doorFuelNotice, Toast.LENGTH_SHORT).show();
                    startToggle.setChecked(false);
                    m.setEngine(false);
                } else if (d.isAllDoorsLocked() && f.getFuelLevel() != 0) {
                    m.setEngine(true);
                }
            }
        });


        final TextView departure = (TextView) findViewById(R.id.departure);
        String departureName = r.getDepartureName();
        departure.setText(departureName);

        final TextView destination = (TextView) findViewById(R.id.destination);
        String destinationName = r.getDestinationName();
        destination.setText(destinationName);

        final TextView distance = (TextView) findViewById(R.id.distance);
        r.calculateDistance();
        if (r.getDistance() != 0) {
            distance.setText(String.format("%.2f", r.getDistance()) + " km");
        }
        else {
            distance.setText("");
        }

        final TextView time = (TextView) findViewById(R.id.estimatedTime);
        r.calculateTime();
        if (r.getDistance() != 0) {
            time.setText(String.valueOf(r.getTime()) + " s");
        }
        else {
            time.setText("");
        }

        final ProgressBar fuelBar = (ProgressBar) findViewById(R.id.fuelBar);
        fuelBar.setProgress(f.getFuelLevel());

        final ToggleButton pauseResume = (ToggleButton) findViewById(R.id.pauseResume);
        pauseResume.setEnabled(false);

        final Button startRoute = (Button) findViewById(R.id.startRoute);

        startRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int distanceInt = (int) r.getDistance();

                if (startToggle.isChecked() && r.getDistance() != 0) {

                    startToggle.setEnabled(false);
                    startRoute.setEnabled(false);
                    pauseResume.setEnabled(true);

                    long eta = distanceInt*15000;
                    new CountDownTimer(eta, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(m.isPaused()){
                                cancel();
                                startToggle.setEnabled(true);
                            }
                            else {
                                time.setText(millisUntilFinished / 1000 + " s");
                                timeRemaining = millisUntilFinished;
                                startToggle.setEnabled(false);
                            }
                        }

                        @Override
                        public void onFinish() {
                            time.setText(R.string.arrived);
                            startRoute.setEnabled(true);
                            pauseResume.setEnabled(false);
                            startToggle.setEnabled(true);
                        }
                    }.start();

                    new CountDownTimer(eta, 5000) {
                        public void onTick(long millisUntilFinished) {
                            if(m.isPaused()) {
                                cancel();
                            }
                            else {
                                f.setFuelLevel(f.getFuelLevel() - 1);
                                fuelBar.setProgress(f.getFuelLevel());
                                timeRemaining = millisUntilFinished;
                            }
                        }
                        public void onFinish() {
                            f.setFuelLevel(f.getFuelLevel());
                            fuelBar.setProgress(f.getFuelLevel());
                        }
                    }.start();
                }
                else if (!startToggle.isChecked()){
                    Toast.makeText(Menu.this, R.string.engineNotice, Toast.LENGTH_SHORT).show();
                }
                else if (r.getDistance() == 0) {
                    Toast.makeText(Menu.this, R.string.routeNotice, Toast.LENGTH_SHORT).show();
                }
            }
        });

        pauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pauseResume.isChecked()) {
                    m.setPaused(true);
                    startToggle.setEnabled(true);
                }
                else if (!startToggle.isChecked()) {
                    Toast.makeText(Menu.this, R.string.engineNotice, Toast.LENGTH_SHORT).show();
                    pauseResume.setChecked(true);
                }
                else {
                    m.setPaused(false);
                    startToggle.setEnabled(false);
                    long eta = timeRemaining;
                    new CountDownTimer(eta, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(m.isPaused()) {
                                cancel();
                            }
                            else {
                                time.setText(millisUntilFinished / 1000 + " s");
                                timeRemaining = millisUntilFinished;
                            }
                        }
                        @Override
                        public void onFinish() {
                            time.setText(R.string.arrived);
                            startRoute.setEnabled(true);
                            pauseResume.setEnabled(false);
                            startToggle.setEnabled(true);
                        }
                    }.start();

                    new CountDownTimer(eta, 5000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(m.isPaused()) {
                                cancel();
                            }
                            else {
                                f.setFuelLevel(f.getFuelLevel()-1);
                                fuelBar.setProgress(f.getFuelLevel());
                                timeRemaining = millisUntilFinished;
                            }
                        }
                        @Override
                        public void onFinish() {
                            f.setFuelLevel(f.getFuelLevel());
                            fuelBar.setProgress(f.getFuelLevel());
                        }
                    }.start();
                }
            }
        });

    }

    private void launchDoor() {
        Intent doorMenu = new Intent(this, Door.class);
        startActivity(doorMenu);
    }

    private void launchFuel() {
        Intent fuelMenu = new Intent(this, Fuel.class);
        startActivity(fuelMenu);
    }

    private void launchAC() {
        Intent acMenu = new Intent(this, AC.class);
        startActivity(acMenu);
    }

    private void launchRoute() {
        Intent routeMenu = new Intent(this, Route.class);
        startActivity(routeMenu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ToggleButton startToggle = (ToggleButton) findViewById(R.id.startToggle);

        if (!d.isAllDoorsLocked() && f.getFuelLevel() != 0 && startToggle.isChecked()) {
            startToggle.setChecked(false);
            m.setEngine(false);
        } else if (d.isAllDoorsLocked() && f.getFuelLevel() == 0 && startToggle.isChecked()) {
            startToggle.setChecked(false);
            m.setEngine(false);
        } else if (!d.isAllDoorsLocked() && f.getFuelLevel() == 0 && startToggle.isChecked()) {
            startToggle.setChecked(false);
            m.setEngine(false);
        } else if (d.isAllDoorsLocked() && f.getFuelLevel() != 0) {
            m.setEngine(true);
        }

        final TextView departure = (TextView) findViewById(R.id.departure);
        String departureName = r.getDepartureName();
        departure.setText(departureName);

        final TextView destination = (TextView) findViewById(R.id.destination);
        String destinationName = r.getDestinationName();
        destination.setText(destinationName);

        final TextView distance = (TextView) findViewById(R.id.distance);
        r.calculateDistance();
        if (r.getDistance() != 0) {
            distance.setText(String.format("%.2f", r.getDistance()) + " km");
        }
        else {
            distance.setText("");
        }

        final TextView time = (TextView) findViewById(R.id.estimatedTime);
        if(!m.isPaused()) {
            r.calculateTime();
            time.setText(String.valueOf(r.getTime()) + " s");
        }
        else {
            time.setText(String.valueOf((int) timeRemaining / 1000) + " s");
        }

        final ProgressBar fuelBar = (ProgressBar) findViewById(R.id.fuelBar);
        fuelBar.setProgress(f.getFuelLevel());
    }

    @Override
    protected void onStop(){
        super.onStop();
        sp.writeValues(Menu.this);
    }
}

