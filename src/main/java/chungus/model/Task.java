package chungus.model;

/**
 * Base class for all tasks.
 */
public class Task {
    protected String description;
    protected TaskStatus status;

    /**
     * Creates a task with the given description and NOT_DONE status.
     *
     * @param description task description
     */
    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    /**
     * Returns the status icon used in string representations.
     *
     * @return "X" for done or space for not done
     */
    public String getStatusIcon() {
        return this.status.getIcon();
    }

    /** Marks the task as done. */
    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    /** Marks the task as not done. */
    public void markAsNotDone() {
        this.status = TaskStatus.NOT_DONE;
    }

    /** Toggles the task status. */
    public void toggleStatus() {
        this.status = this.status.toggle();
    }

    /**
     * Returns a user-friendly representation including status icon.
     */
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), this.description);
    }

    /**
     * Returns the task description.
     *
     * @return description
     */
    public String getDescription() {
        return this.description;
    }
}


