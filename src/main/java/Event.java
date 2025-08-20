public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (from: %s to: %s)", TaskType.EVENT.getSymbol(), super.toString(), this.from, this.to);
    }
}
