package balychev.oleh.blch.cardword.model;

public class State {
    private int id;
    private int interval;

    public State(int id, int interval) {
        this.id = id;
        this.interval = interval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
