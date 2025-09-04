package chungus;

/**
 * Simple task without dates.
 */
public class Todo extends Task {
    /**
     * Creates a todo task.
     *
     * @param description description text
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", TaskType.TODO.getSymbol(), super.toString());
    }
}
