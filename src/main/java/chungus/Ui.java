package chungus;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Hello! I'm Chungus!");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________\n");
    }

    public void showLine() {
        System.out.println("____________________________________________________________\n");
    }

    public void showError(String message) {
        System.out.println("____________________________________________________________\n");
        System.out.println("OOPS!!! " + message);
        System.out.println("____________________________________________________________\n");
    }

    public void showLoadingError() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Warning: Could not load tasks from storage. Starting with empty list.");
        System.out.println("____________________________________________________________\n");
    }

    public void showTasksLoaded() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Tasks loaded successfully from storage.");
        System.out.println("____________________________________________________________\n");
    }

    public void showBye() {
        System.out.println("____________________________________________________________\n");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________\n");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }

    public void showTaskList(TaskList tasks) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }
        System.out.println("____________________________________________________________\n");
    }

    public void showTaskAdded(Task task, int newSize) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Got it. I've added this task:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", newSize));
        System.out.println("____________________________________________________________\n");
    }

    public void showTaskDeleted(Task task, int newSize) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Noted. I've removed this task:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", newSize));
        System.out.println("____________________________________________________________\n");
    }

    public void showMarked(Task task) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println("____________________________________________________________\n");
    }

    public void showUnmarked(Task task) {
        System.out.println("____________________________________________________________\n");
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(String.format("  %s", task.toString()));
        System.out.println("____________________________________________________________\n");
    }

    public void showFindResults(java.util.ArrayList<Task> matches) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, matches.get(i).toString()));
        }
        System.out.println("____________________________________________________________\n");
    }
}
