package Game;

import java.io.Serializable;

public class Settings implements Serializable {
    private boolean tipsOn;
    private boolean adOn;

    public Settings() {
        this.tipsOn = true;
        this.adOn = true;
    }

    public boolean isTipsOn() {
        return tipsOn;
    }

    public void setTipsOn(boolean tipsOn) {
        this.tipsOn = tipsOn;
    }

    public boolean isAdOn() {
        return adOn;
    }

    public void setAdOn(boolean adOn) {
        this.adOn = adOn;
    }
}
