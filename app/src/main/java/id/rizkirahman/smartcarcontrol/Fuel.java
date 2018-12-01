package id.rizkirahman.smartcarcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import id.rizkirahman.smartcarcontrol.R;

public class Fuel extends AppCompatActivity {

    private FuelModel f = FuelModel.getInstance();
    private SharedPref sp = SharedPref.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);

        EditText fillTank = (EditText) findViewById(R.id.fillTank);
        final Editable e = fillTank.getText();

        final ProgressBar fuelProgress = (ProgressBar) findViewById(R.id.fuelProgressBar);
        final TextView fuelLevel = (TextView) findViewById(R.id.fuelLevel);

        fuelLevel.setText(String.valueOf(f.getFuelLevel()) + " %");
        fuelProgress.setProgress(f.getFuelLevel());

        // Fill Fuel
        final Button fillButton = (Button) findViewById(R.id.fillButton);
        fillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = fuelProgress.getProgress();
                int inputAmount;
                int totalAmount;

                inputAmount = Integer.valueOf(e.toString());
                totalAmount = currentAmount + inputAmount;

                if (totalAmount > 100) {
                    fuelProgress.setProgress(100);
                    f.setFuelLevel(100);
                    fuelLevel.setText(String.valueOf(f.getFuelLevel()) + " %");
                }
                else if (totalAmount <= 100){
                    fuelProgress.setProgress(totalAmount);
                    f.setFuelLevel(totalAmount);
                    fuelLevel.setText(String.valueOf(totalAmount) + " %");
                }
            }
        });

        final Button backToMenu = (Button) findViewById(R.id.backToMenu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        sp.writeValues(Fuel.this);
    }

}
