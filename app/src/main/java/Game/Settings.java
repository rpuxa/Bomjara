package Game;

import java.io.Serializable;

public class Settings implements Serializable {

    private static final long serialVersionUID = 2001L;

    private boolean tipsOn;
    private boolean showAppRate;

    public Settings() {
        this.tipsOn = true;
        this.showAppRate = true;
    }

    public boolean isTipsOn() {
        return tipsOn;
    }

    public void setTipsOn(boolean tipsOn) {
        this.tipsOn = tipsOn;
    }

    public boolean isShowAppRate() {
        return showAppRate;
    }

    public void setShowAppRate(boolean showAppRate) {
        this.showAppRate = showAppRate;
    }
}
