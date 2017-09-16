package Game;

import java.io.Serializable;

public class Settings implements Serializable {
    boolean tipsOn;

    public Settings(boolean tipsOn) {
        this.tipsOn = true;
    }

    public boolean isTipsOn() {
        return tipsOn;
    }

    public void setTipsOn(boolean tipsOn) {
        this.tipsOn = tipsOn;
    }
}
