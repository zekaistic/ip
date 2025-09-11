package chungus.model;

/**
 * Completion status of a task.
 */
public enum TaskStatus {
    DONE("X"),
    NOT_DONE(" ");

    private final String icon;

    TaskStatus(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the display icon for this status.
     *
     * @return icon string
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Toggles between DONE and NOT_DONE.
     *
     * @return toggled status
     */
    public TaskStatus toggle() {
        return this == DONE ? NOT_DONE : DONE;
    }
}


