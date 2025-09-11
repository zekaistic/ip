package chungus.ui;

import java.util.ArrayList;
import java.util.Scanner;

import chungus.model.Task;
import chungus.model.TaskList;

/**
 * Console-based user interface for input and output.
 * Responsible for all user-facing messages and reading commands.
 */

public class Ui {
    private final Scanner scanner;

    /**
     * Creates a new UI instance that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the welcome banner.
     */
    public void showWelcome() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Hello! I'm Chungus!");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Prints a horizontal rule.
     */
    public void showLine() {
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows an error message.
     *
     * @param message error text to display
     */
    public void showError(String message) {
        System.out.println("____________________________________________________________\n");
        System.out.println("OOPS!!! " + message);
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows a warning that tasks could not be loaded.
     */
    public void showLoadingError() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Warning: Could not load tasks from storage. Starting with empty list.");
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows a message that tasks were loaded.
     */
    public void showTasksLoaded() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Tasks loaded successfully from storage.");
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Prints the goodbye message and banner.
     */
    public void showBye() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Reads the next user command from standard input.
     *
     * @return the raw command line
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Releases any resources associated with input.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Shows all tasks in the list.
     *
     * @param tasks task list to render
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows that a task was added.
     *
     * @param task    the task added
     * @param newSize the new list size
     */
    public void showTaskAdded(Task task, int newSize) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Got it. I've added this task:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", newSize));
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows that a task was removed.
     *
     * @param task    the task removed
     * @param newSize the new list size
     */
    public void showTaskDeleted(Task task, int newSize) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Noted. I've removed this task:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", newSize));
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows that a task was marked as done.
     *
     * @param task the updated task
     */
    public void showMarked(Task task) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows that a task was unmarked.
     *
     * @param task the updated task
     */
    public void showUnmarked(Task task) {
        System.out.println("____________________________________________________________\n");
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println("____________________________________________________________\n");
    }

    /**
     * Shows the results of a find operation.
     *
     * @param matches matching tasks
     */
    public void showFindResults(ArrayList<Task> matches) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, matches.get(i).toString()));
        }
        System.out.println("____________________________________________________________\n");
    }

    // GUI-compatible methods that return formatted strings instead of printing to console

    /**
     * Returns the welcome message as a string for GUI display.
     *
     * @return Formatted welcome message
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Chungus!\nWhat can I do for you?";
    }

    /**
     * Returns the goodbye message as a string for GUI display.
     *
     * @return Formatted goodbye message
     */
    public String getByeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns an error message as a string for GUI display.
     *
     * @param message error text to display
     * @return Formatted error message
     */
    public String getErrorMessage(String message) {
        return "OOPS!!! " + message;
    }

    /**
     * Returns the task list as a formatted string for GUI display.
     *
     * @param tasks task list to render
     * @return Formatted task list string
     */
    public String getTaskListMessage(TaskList tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks.get(i).toString()));
        }
        return sb.toString();
    }

    /**
     * Returns a task added message as a string for GUI display.
     *
     * @param task    the task added
     * @param newSize the new list size
     * @return Formatted task added message
     */
    public String getTaskAddedMessage(Task task, int newSize) {
        return String.format("Got it. I've added this task:\n  %s\nNow you have %d tasks in the list.",
                           task.toString(), newSize);
    }

    /**
     * Returns a task deleted message as a string for GUI display.
     *
     * @param task    the task removed
     * @param newSize the new list size
     * @return Formatted task deleted message
     */
    public String getTaskDeletedMessage(Task task, int newSize) {
        return String.format("Noted. I've removed this task:\n  %s\nNow you have %d tasks in the list.",
                           task.toString(), newSize);
    }

    /**
     * Returns a task marked message as a string for GUI display.
     *
     * @param task the updated task
     * @return Formatted task marked message
     */
    public String getMarkedMessage(Task task) {
        return String.format("Nice! I've marked this task as done:\n  %s", task.toString());
    }

    /**
     * Returns a task unmarked message as a string for GUI display.
     *
     * @param task the updated task
     * @return Formatted task unmarked message
     */
    public String getUnmarkedMessage(Task task) {
        return String.format("OK, I've marked this task as not done yet:\n  %s", task.toString());
    }

    /**
     * Returns the results of a find operation as a string for GUI display.
     *
     * @param matches matching tasks
     * @return Formatted find results message
     */
    public String getFindResultsMessage(ArrayList<Task> matches) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, matches.get(i).toString()));
        }
        return sb.toString();
    }
}


