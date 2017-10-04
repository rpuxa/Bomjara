package Game;


public class Jobs {
    private Action[] actions;

    Jobs(Action... actions) {
        this.actions = actions;
    }

    public Action[] getActions() {
        return actions;
    }
}
