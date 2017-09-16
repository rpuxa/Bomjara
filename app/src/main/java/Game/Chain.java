package Game;

public class Chain {
    private String name;
    private int transportNeeded;
    private int houseNeeded;
    private int friendNeeded;
    private int locationNeeded;
    private int moneyAdd;
    private int currency;

    public Chain(String name, int transportNeeded, int houseNeeded, int friendNeeded, int locationNeeded, int moneyAdd, int currency) {
        this.name = name;
        this.transportNeeded = transportNeeded;
        this.houseNeeded = houseNeeded;
        this.friendNeeded = friendNeeded;
        this.locationNeeded = locationNeeded;
        this.moneyAdd = moneyAdd;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getMoney() {
        return Action.moneyToStr(moneyAdd,currency);
    }

    public void open(int id) {
        Player player = Player.currentPlayer;
        if (transportNeeded > player.transport)
            Action.listener.showMassage("Требуется транспорт: " + Create.transport[transportNeeded].getName());
        else if (houseNeeded > player.house)
            Action.listener.showMassage("Требуется дом: " + Create.houses[houseNeeded].getName());
        else if (friendNeeded > player.friend)
            Action.listener.showMassage("Требуется кореш: " + Create.friends[friendNeeded].getName());
        else if (locationNeeded > player.location)
            Action.listener.showMassage("Требуется локация: " + Create.locationChain[locationNeeded].getName());
        else if (!player.addMoney(moneyAdd, currency))
            Action.listener.showMassage("Не хватает средств!");
        else {
            if (id == 0)
                player.location++;
            else if (id == 1)
                player.friend++;
            else if (id == 2)
                player.house++;
            else if (id == 3)
                player.transport++;
            Action.listener.updateChains(Player.currentPlayer);
            Action.listener.updateActions(Player.currentPlayer);
            Action.listener.updateInfo(Player.currentPlayer);
        }
    }
}
