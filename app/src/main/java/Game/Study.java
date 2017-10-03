package Game;

import android.view.View;

public class Study implements View.OnClickListener {
    private int id;
    private String name;
    private int cost;
    private int currency;
    private int length;

    Study(int id, String name, int cost, int currency, int length) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.currency = currency;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public int getCurrency() {
        return currency;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static StudyListener listener;

    @Override
    public void onClick(View view) {
        Player player = Player.currentPlayer;
        if (player.learning[id] == 0 && !player.addMoney(-cost, currency, true))
            Action.listener.showMassage("Не хватает средств!");
        else {
            player.learning[id]++;
            new Action("",0,Constants.none,-5,-5,-5,Constants.none,true).onClick(view);
            listener.showStudy();
        }
    }

    public interface StudyListener{
        void showStudy();
    }
}
