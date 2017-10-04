package Game;

public class Chain implements Constants {
    private String name;
    private int transportNeeded;
    private int houseNeeded;
    private int friendNeeded;
    private int locationNeeded;
    private int studyNeeded;
    private int moneyAdd;
    private int currency;

    Chain(String name, int transportNeeded, int houseNeeded, int friendNeeded, int locationNeeded, int studyNeeded, int moneyAdd, int currency) {
        this.name = name;
        this.transportNeeded = transportNeeded;
        this.houseNeeded = houseNeeded;
        this.friendNeeded = friendNeeded;
        this.locationNeeded = locationNeeded;
        this.studyNeeded = studyNeeded;
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
            Action.listener.showMassage(transportNeededSt + Create.transports[transportNeeded].getName());
        else if (houseNeeded > player.house)
            Action.listener.showMassage(houseNeededSt + Create.houses[houseNeeded].getName());
        else if (friendNeeded > player.friend)
            Action.listener.showMassage(friendNeededSt + Create.friends[friendNeeded].getName());
        else if (locationNeeded > player.location)
            Action.listener.showMassage(locationNeededSt + Create.locationChains[locationNeeded].getName());
        else if (studyNeeded != -1 && player.learning[studyNeeded] != Create.study[studyNeeded].getLength())
            Action.listener.showMassage(studyNeededSt + Create.study[studyNeeded].getName());
        else if (!player.addMoney(moneyAdd, currency,true))
            Action.listener.showMassage(noMoney);
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
