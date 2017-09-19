package Game;

import java.io.Serializable;

public class Settings implements Serializable {
    private boolean tipsOn;

    public Settings() {
        this.tipsOn = true;
    }

    public boolean isTipsOn() {
        return tipsOn;
    }

    public void setTipsOn(boolean tipsOn) {
        this.tipsOn = tipsOn;
    }

}
