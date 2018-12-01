package id.rizkirahman.smartcarcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;


public class Door extends AppCompatActivity {

    private DoorModel d = DoorModel.getInstance();
    private SharedPref sp = SharedPref.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

        final Switch frontLeft = (Switch) findViewById(R.id.frontLeft);
        final Switch frontRight = (Switch) findViewById(R.id.frontRight);
        final Switch rearLeft = (Switch) findViewById(R.id.rearLeft);
        final Switch rearRight = (Switch) findViewById(R.id.rearRight);
        final Switch boot = (Switch) findViewById(R.id.boot);
        final Button lockAll = (Button) findViewById(R.id.lockAll);

        if (d.isFrontLeft() == true)
            frontLeft.setChecked(true);
        else frontLeft.setChecked(false);

        if (d.isFrontRight() == true)
            frontRight.setChecked(true);
        else frontRight.setChecked(false);

        if (d.isRearLeft() == true)
            rearLeft.setChecked(true);
        else rearLeft.setChecked(false);

        if (d.isRearRight() == true)
            rearRight.setChecked(true);
        else rearRight.setChecked(false);

        if (d.isBoot() == true)
            boot.setChecked(true);
        else boot.setChecked(false);

        lockAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frontLeft.setChecked(true);
                rearLeft.setChecked(true);
                frontRight.setChecked(true);
                rearRight.setChecked(true);
                boot.setChecked(true);
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
    protected void onDestroy() {
        super.onDestroy();

        final Switch frontLeft = (Switch) findViewById(R.id.frontLeft);
        final Switch frontRight = (Switch) findViewById(R.id.frontRight);
        final Switch rearLeft = (Switch) findViewById(R.id.rearLeft);
        final Switch rearRight = (Switch) findViewById(R.id.rearRight);
        final Switch boot = (Switch) findViewById(R.id.boot);

        if (frontLeft.isChecked())
            d.setFrontLeft(true);
        else d.setFrontLeft(false);

        if (frontRight.isChecked())
            d.setFrontRight(true);
        else d.setFrontRight(false);

        if (rearLeft.isChecked())
            d.setRearLeft(true);
        else d.setRearLeft(false);

        if (rearRight.isChecked())
            d.setRearRight(true);
        else d.setRearRight(false);

        if (boot.isChecked())
            d.setBoot(true);
        else d.setBoot(false);

        if (frontLeft.isChecked()
                && frontRight.isChecked()
                && rearLeft.isChecked()
                && rearRight.isChecked()
                && boot.isChecked())
            d.setAllDoorsLocked(true);
        else
            d.setAllDoorsLocked(false);

        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();
        sp.writeValues(Door.this);
    }
}