package Game;

public class Location {

    private Action[] actions;
    private long fine;

    public Location(long fine ,Action... actions) {
        this.fine = fine;
        this.actions = actions;
    }

    public Action[] getActions() {
        return actions;
    }

    public long getFine() {
        return fine;
    }
}
