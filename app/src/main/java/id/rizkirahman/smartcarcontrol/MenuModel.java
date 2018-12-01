package id.rizkirahman.smartcarcontrol;

public class MenuModel {

    private static final MenuModel ourInstance = new MenuModel();

    static MenuModel getInstance() {
        return ourInstance;
    }

    private boolean engine;
    private boolean paused;

    public boolean isEngine() {
        return engine;
    }

    public void setEngine(boolean engine) {
        this.engine = engine;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
