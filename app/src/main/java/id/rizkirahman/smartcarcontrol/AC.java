package id.rizkirahman.smartcarcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import id.rizkirahman.smartcarcontrol.R;

public class AC extends AppCompatActivity {

    private ACModel ac = ACModel.getInstance();
    private SharedPref sp = SharedPref.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);

        final SeekBar acBar = (SeekBar) findViewById(R.id.acBar);
        final TextView tempLevel = (TextView) findViewById(R.id.temperatureLevel);

        if(ac.getTemperature() == 0)
            tempLevel.setText("18°C");
        else
            tempLevel.setText(Integer.toString(ac.getTemperature() + 18) + "°C");

        acBar.setProgress(ac.getTemperature());
        acBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempLevel.setText(Integer.toString(acBar.getProgress() + 18) + "°C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tempLevel.setText(Integer.toString(acBar.getProgress()+18) + "°C");
                ac.setTemperature(acBar.getProgress());
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
        sp.writeValues(AC.this);
    }
}
