package Game;

import android.view.View;

import java.util.Random;

public class Action implements Constants, View.OnClickListener{
    private String name;
    private long moneyAdd;
    private int currency;
    private double healthAdd;
    private double energyAdd;
    private double foodAdd;
    private int menu;
    private boolean legal;

    public Action(String name, long moneyAdd, int currency, double healthAdd, double energyAdd, double foodAdd, int menu, boolean legal) {
        this.name = name;
        this.moneyAdd = moneyAdd;
        this.currency = currency;
        this.healthAdd = healthAdd;
        this.energyAdd = energyAdd;
        this.foodAdd = foodAdd;
        this.menu = menu;
        this.legal = legal;
    }

    public String getName() {
        return name + " " + ((moneyAdd > 0) ? "+" : "") + moneyToStr(moneyAdd,currency);
    }

    public static String moneyToStr(long money, int currency){
        String moneySt = String.valueOf(money);
        if (currency == rub)
            moneySt += "р.";
        else if (currency == euro)
            moneySt += "€";
        else if (currency == bitcoin)
            moneySt += "bit.";
        else if (currency == bottle)
            moneySt += "бут.";
        else
            moneySt = "";
        return moneySt;
    }

    @Override
    public void onClick(View view) {
        Player player = Player.currentPlayer;
        if (!player.addMoney(moneyAdd,currency,false)) {
            listener.showMassage("Не хватает средств!");
        } else if (legal || illegalAction()) {
            long energy = player.addEnergy(energyAdd);
            long food = player.addFood(foodAdd);
            long health = player.addHealth(healthAdd);
            listener.showMassage(addToStr(food,"Еда",true) + addToStr(health,"Здоровье",false) + addToStr(energy,"Бодрость",false),true);
            player.addAge();
            player.checkHealth();
            if (energy != 0 || food != 0 || health !=0)
                player.addRandVipMoney();
        }
        listener.updateInfo(player);
    }

    private static String addToStr(double num, String string, boolean ln){
        return (num == 0) ? "" : (string + ": " + ((num < 0) ? "" : "+") + num + ((ln) ? "\n" : " "));
    }

    public int getMenu() {
        return menu;
    }

    public boolean isLegal() {
        return legal;
    }

    private boolean illegalAction(){
        if (new Random().nextInt(10) == 5){
            listener.caughtByPolice();
            return false;
        } else {
            return true;
        }
    }

    public static ActionListener listener;

    public interface ActionListener{
        void updateActions(Player player);

        void updateInfo(Player player);

        void showMassage(String massage);

        void showMassage(String massage, boolean isShort);

        void updateChains(Player player);

        void caughtByPolice();
    }
}
