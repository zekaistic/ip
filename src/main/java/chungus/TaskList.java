package chungus;

import java.util.ArrayList;

/**
 * A thin wrapper around an ArrayList of Tasks providing
 * operations used by the application.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list backed by an existing collection.
     *
     * @param tasks Backing list (nullable); if null, an empty list is used.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public ArrayList<Task> asArrayList() {
        return tasks;
    }

    public ArrayList<Task> findByKeyword(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return matches;
        }
        String lower = keyword.toLowerCase();
        for (Task t : tasks) {
            if (t.getDescription() != null && t.getDescription().toLowerCase().contains(lower)) {
                matches.add(t);
            }
        }
        return matches;
    }
}
