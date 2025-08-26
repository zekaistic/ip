public class Task {
    protected String description;
    protected TaskStatus status;

    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    public String getStatusIcon() {
        return this.status.getIcon();
    }

    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    public void markAsNotDone() {
        this.status = TaskStatus.NOT_DONE;
    }

    public void toggleStatus() {
        this.status = this.status.toggle();
    }

    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), this.description);
    }
    
    public String getDescription() {
        return this.description;
    }
}
