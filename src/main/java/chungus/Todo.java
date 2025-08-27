package chungus;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", TaskType.TODO.getSymbol(), super.toString());
    }
}


