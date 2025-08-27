package chungus;

import java.io.IOException;

public class Chungus {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    public Chungus(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);
        try {
            this.tasks = new TaskList(storage.load());
            ui.showTasksLoaded();
        } catch (IOException e) {
            ui.showLoadingError();
            this.tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        String input = ui.readCommand();
        while (!input.equals(CommandType.BYE.getCommand())) {
            try {
                CommandType command = parser.parseCommandType(input);
                if (command == null) {
                    if (input.trim().isEmpty()) {
                        // ignore silently
                    } else {
                        throw new ChungusException("I'm sorry, but I don't know what that means :-(");
                    }
                } else {
                    processCommand(command, input);
                }
            } catch (ChungusException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("Something went wrong: " + e.getMessage());
            }
            input = ui.readCommand();
        }
        saveTasksToStorage();
        ui.showBye();
        ui.close();
    }

    private void saveTasksToStorage() {
        try {
            storage.save(tasks.asArrayList());
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to storage: " + e.getMessage());
        }
    }

    private void processCommand(CommandType command, String input) throws ChungusException {
        switch (command) {
            case LIST:
                ui.showTaskList(tasks);
                break;
            case MARK:
                if (!input.startsWith(CommandType.MARK.getCommand() + " ")) {
                    throw new ChungusException("Please provide a task number.");
                }
                markTask(input, true);
                break;
            case UNMARK:
                if (!input.startsWith(CommandType.UNMARK.getCommand() + " ")) {
                    throw new ChungusException("Please provide a task number.");
                }
                markTask(input, false);
                break;
            case DELETE:
                if (!input.startsWith(CommandType.DELETE.getCommand() + " ")) {
                    throw new ChungusException("Please provide a task number.");
                }
                deleteTask(input);
                break;
            case TODO:
                if (!input.startsWith(CommandType.TODO.getCommand() + " ")) {
                    throw new ChungusException("The description of a todo cannot be empty.");
                }
                addTodo(input);
                break;
            case DEADLINE:
                if (!input.startsWith(CommandType.DEADLINE.getCommand() + " ")) {
                    throw new ChungusException("Deadline command must include '/by' followed by the due date.");
                }
                addDeadline(input);
                break;
            case EVENT:
                if (!input.startsWith(CommandType.EVENT.getCommand() + " ")) {
                    throw new ChungusException("Event command must include both '/from' and '/to' followed by start and end times.");
                }
                addEvent(input);
                break;
            default:
                throw new ChungusException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private void markTask(String input, boolean markAsDone) throws ChungusException {
        int idx = parser.parseTaskIndex(input, markAsDone ? CommandType.MARK.getCommand() : CommandType.UNMARK.getCommand());
        if (idx < 0 || idx >= tasks.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        Task task = tasks.get(idx);
        if (markAsDone) {
            task.markAsDone();
            ui.showMarked(task);
        } else {
            task.markAsNotDone();
            ui.showUnmarked(task);
        }
    }

    private void deleteTask(String input) throws ChungusException {
        int idx = parser.parseTaskIndex(input, CommandType.DELETE.getCommand());
        if (idx < 0 || idx >= tasks.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        Task deleted = tasks.remove(idx);
        ui.showTaskDeleted(deleted, tasks.size());
    }

    private void addTodo(String input) throws ChungusException {
        String description = parser.parseDescription(input, CommandType.TODO.getCommand());
        if (description.trim().isEmpty()) {
            throw new ChungusException("The description of a todo cannot be empty.");
        }
        Task t = new Todo(description);
        tasks.add(t);
        ui.showTaskAdded(t, tasks.size());
    }

    private void addDeadline(String input) throws ChungusException {
        if (!input.contains("/by")) {
            throw new ChungusException("Deadline command must include '/by' followed by the due date.");
        }
        String[] parts = parser.parseDeadline(input);
        if (parts[0].trim().isEmpty()) {
            throw new ChungusException("The description of a deadline cannot be empty.");
        }
        if (parts[1].trim().isEmpty()) {
            throw new ChungusException("The due date cannot be empty.");
        }
        Task t = new Deadline(parts[0], parts[1]);
        tasks.add(t);
        ui.showTaskAdded(t, tasks.size());
    }

    private void addEvent(String input) throws ChungusException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new ChungusException("Event command must include both '/from' and '/to' followed by start and end times.");
        }
        String[] parts = parser.parseEvent(input);
        if (parts[0].trim().isEmpty()) {
            throw new ChungusException("The description of an event cannot be empty.");
        }
        if (parts[1].trim().isEmpty()) {
            throw new ChungusException("The start time cannot be empty.");
        }
        if (parts[2].trim().isEmpty()) {
            throw new ChungusException("The end time cannot be empty.");
        }
        Task t = new Event(parts[0], parts[1], parts[2]);
        tasks.add(t);
        ui.showTaskAdded(t, tasks.size());
    }

    public static void main(String[] args) {
        new Chungus("data/chungus.txt").run();
    }
}


