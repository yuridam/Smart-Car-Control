package id.rizkirahman.smartcarcontrol;

import android.content.Context;
import android.content.SharedPreferences;

public class DoorModel {

    private static final DoorModel ourInstance = new DoorModel();

    static DoorModel getInstance() {
        return ourInstance;
    }

    private boolean allDoorsLocked;
    private boolean frontLeft;
    private boolean frontRight;
    private boolean rearLeft;
    private boolean rearRight;
    private boolean boot;

    public boolean isAllDoorsLocked() {
        return allDoorsLocked;
    }

    public void setAllDoorsLocked(boolean allDoorsLocked) {
        this.allDoorsLocked = allDoorsLocked;
    }

    public boolean isFrontLeft() {
        return frontLeft;
    }

    public void setFrontLeft(boolean frontLeft) {
        this.frontLeft = frontLeft;
    }

    public boolean isFrontRight() {
        return frontRight;
    }

    public void setFrontRight(boolean frontRight) {
        this.frontRight = frontRight;
    }

    public boolean isRearLeft() {
        return rearLeft;
    }

    public void setRearLeft(boolean rearLeft) {
        this.rearLeft = rearLeft;
    }

    public boolean isRearRight() {
        return rearRight;
    }

    public void setRearRight(boolean rearRight) {
        this.rearRight = rearRight;
    }

    public boolean isBoot() {
        return boot;
    }

    public void setBoot(boolean boot) {
        this.boot = boot;
    }

}
