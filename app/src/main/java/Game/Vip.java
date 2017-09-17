package Game;

import android.view.View;

public abstract class Vip implements View.OnClickListener {

    private String name;
    private boolean sold;
    private boolean notInfinity;
    private long cost;

    Vip(String name, boolean notInfinity, long cost) {
        this.name = name;
        this.notInfinity = notInfinity;
        this.cost = cost;
    }

    @Override
    public void onClick(View view) {
        if (!sold) {
            Player player = Player.currentPlayer;
            if (player.getVipMoney() < cost)
                Action.listener.showMassage("Не хватает средств!");
            else {
                player.addVipMoney(-cost);
                if (notInfinity)
                    sold = true;
                reward(player);
                listener.updateVipStore(player);
                Action.listener.updateInfo(player);
            }
        } else
            Action.listener.showMassage("Уже продано!");
    }

    abstract void reward(Player player);

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public boolean isNotInfinity() {
        return notInfinity;
    }

    public long getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public static VipListener listener;

    public interface VipListener{
        void updateVipStore(Player player);
    }
}
