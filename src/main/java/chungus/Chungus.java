package chungus;

import java.io.IOException;

/**
 * Main application entry point for the Chungus task manager.
 */

public class Chungus {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Creates a new Chungus application instance and loads tasks from storage.
     *
     * @param filePath Path to the data file used for persistence.
     */
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

    /**
     * Starts the interactive loop: reads user commands, executes them,
     * and displays results until the user exits.
     */
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

    /**
     * Persists the current task list to storage, showing an error via UI on
     * failure.
     */
    private void saveTasksToStorage() {
        try {
            storage.save(tasks.asArrayList());
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to storage: " + e.getMessage());
        }
    }

    /**
     * Dispatches a parsed command to the appropriate handler.
     *
     * @param command Parsed command type.
     * @param input   Raw user input.
     * @throws ChungusException if validation fails or the command is invalid.
     */
    private void processCommand(CommandType command, String input) throws ChungusException {
        switch (command) {
        case LIST:
            ui.showTaskList(tasks);
            break;
        case FIND:
            if (!input.startsWith(CommandType.FIND.getCommand() + " ")) {
                throw new ChungusException("Please provide a keyword to find.");
            }
            findTasks(input);
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
                throw new ChungusException(
                        "Event command must include both '/from' and '/to' followed by start and end times.");
            }
            addEvent(input);
            break;
        default:
            throw new ChungusException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Marks or unmarks a task based on the provided command input.
     *
     * @param input      Raw user input containing the task index.
     * @param markAsDone True to mark as done; false to mark as not done.
     * @throws ChungusException if the task index is invalid.
     */
    private void markTask(String input, boolean markAsDone) throws ChungusException {
        int idx = parser.parseTaskIndex(input,
                markAsDone ? CommandType.MARK.getCommand() : CommandType.UNMARK.getCommand());
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

    private void findTasks(String input) throws ChungusException {
        String keyword = parser.parseDescription(input, CommandType.FIND.getCommand());
        if (keyword.trim().isEmpty()) {
            throw new ChungusException("Please provide a keyword to find.");
        }
        java.util.ArrayList<Task> matches = tasks.findByKeyword(keyword);
        ui.showFindResults(matches);
    }

    /**
     * Deletes a task at the index specified in the input.
     *
     * @param input Raw user input containing the task index.
     * @throws ChungusException if the task index is invalid.
     */
    private void deleteTask(String input) throws ChungusException {
        int idx = parser.parseTaskIndex(input, CommandType.DELETE.getCommand());
        if (idx < 0 || idx >= tasks.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        Task deleted = tasks.remove(idx);
        ui.showTaskDeleted(deleted, tasks.size());
    }

    /**
     * Adds a Todo task from the provided input.
     *
     * @param input Raw user input containing the description.
     * @throws ChungusException if the description is empty.
     */
    private void addTodo(String input) throws ChungusException {
        String description = parser.parseDescription(input, CommandType.TODO.getCommand());
        if (description.trim().isEmpty()) {
            throw new ChungusException("The description of a todo cannot be empty.");
        }
        Task t = new Todo(description);
        tasks.add(t);
        ui.showTaskAdded(t, tasks.size());
    }

    /**
     * Adds a Deadline task from the provided input.
     *
     * @param input Raw user input containing description and /by date.
     * @throws ChungusException if parts are missing.
     */
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

    /**
     * Adds an Event task from the provided input.
     *
     * @param input Raw user input containing description, /from and /to.
     * @throws ChungusException if parts are missing.
     */
    private void addEvent(String input) throws ChungusException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new ChungusException(
                    "Event command must include both '/from' and '/to' followed by start and end times.");
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

    /**
     * Processes a user input and returns a response string for GUI display.
     * This method replaces the console-based run() method for GUI usage.
     *
     * @param input User input command
     * @return Formatted response string
     */
    public String getResponse(String input) {
        if (input.trim().equals(CommandType.BYE.getCommand())) {
            saveTasksToStorage();
            return ui.getByeMessage();
        }
        try {
            CommandType command = parser.parseCommandType(input);
            if (command == null) {
                if (input.trim().isEmpty()) {
                    return ""; // ignore silently
                } else {
                    throw new ChungusException("I'm sorry, but I don't know what that means :-(");
                }
            } else {
                return processCommandForGui(command, input);
            }
        } catch (ChungusException e) {
            return ui.getErrorMessage(e.getMessage());
        } catch (Exception e) {
            return ui.getErrorMessage("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Dispatches a parsed command to the appropriate handler for GUI.
     * Returns formatted response strings instead of printing to console.
     *
     * @param command Parsed command type.
     * @param input   Raw user input.
     * @return Formatted response string
     * @throws ChungusException if validation fails or the command is invalid.
     */
    private String processCommandForGui(CommandType command, String input) throws ChungusException {
        switch (command) {
        case LIST:
            return ui.getTaskListMessage(tasks);
        case FIND:
            if (!input.startsWith(CommandType.FIND.getCommand() + " ")) {
                throw new ChungusException("Please provide a keyword to find.");
            }
            return findTasksForGui(input);
        case MARK:
            if (!input.startsWith(CommandType.MARK.getCommand() + " ")) {
                throw new ChungusException("Please provide a task number.");
            }
            return markTaskForGui(input, true);
        case UNMARK:
            if (!input.startsWith(CommandType.UNMARK.getCommand() + " ")) {
                throw new ChungusException("Please provide a task number.");
            }
            return markTaskForGui(input, false);
        case DELETE:
            if (!input.startsWith(CommandType.DELETE.getCommand() + " ")) {
                throw new ChungusException("Please provide a task number.");
            }
            return deleteTaskForGui(input);
        case TODO:
            if (!input.startsWith(CommandType.TODO.getCommand() + " ")) {
                throw new ChungusException("The description of a todo cannot be empty.");
            }
            return addTodoForGui(input);
        case DEADLINE:
            if (!input.startsWith(CommandType.DEADLINE.getCommand() + " ")) {
                throw new ChungusException("Deadline command must include '/by' followed by the due date.");
            }
            return addDeadlineForGui(input);
        case EVENT:
            if (!input.startsWith(CommandType.EVENT.getCommand() + " ")) {
                throw new ChungusException(
                        "Event command must include both '/from' and '/to' followed by start and end times.");
            }
            return addEventForGui(input);
        default:
            throw new ChungusException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Marks or unmarks a task based on the provided command input for GUI.
     *
     * @param input      Raw user input containing the task index.
     * @param markAsDone True to mark as done; false to mark as not done.
     * @return Formatted response string
     * @throws ChungusException if the task index is invalid.
     */
    private String markTaskForGui(String input, boolean markAsDone) throws ChungusException {
        int idx = parser.parseTaskIndex(input,
                markAsDone ? CommandType.MARK.getCommand() : CommandType.UNMARK.getCommand());
        if (idx < 0 || idx >= tasks.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        Task task = tasks.get(idx);
        if (markAsDone) {
            task.markAsDone();
            return ui.getMarkedMessage(task);
        } else {
            task.markAsNotDone();
            return ui.getUnmarkedMessage(task);
        }
    }

    private String findTasksForGui(String input) throws ChungusException {
        String keyword = parser.parseDescription(input, CommandType.FIND.getCommand());
        if (keyword.trim().isEmpty()) {
            throw new ChungusException("Please provide a keyword to find.");
        }
        java.util.ArrayList<Task> matches = tasks.findByKeyword(keyword);
        return ui.getFindResultsMessage(matches);
    }

    /**
     * Deletes a task at the index specified in the input for GUI.
     *
     * @param input Raw user input containing the task index.
     * @return Formatted response string
     * @throws ChungusException if the task index is invalid.
     */
    private String deleteTaskForGui(String input) throws ChungusException {
        int idx = parser.parseTaskIndex(input, CommandType.DELETE.getCommand());
        if (idx < 0 || idx >= tasks.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
        Task deleted = tasks.remove(idx);
        return ui.getTaskDeletedMessage(deleted, tasks.size());
    }

    /**
     * Adds a Todo task from the provided input for GUI.
     *
     * @param input Raw user input containing the description.
     * @return Formatted response string
     * @throws ChungusException if the description is empty.
     */
    private String addTodoForGui(String input) throws ChungusException {
        String description = parser.parseDescription(input, CommandType.TODO.getCommand());
        if (description.trim().isEmpty()) {
            throw new ChungusException("The description of a todo cannot be empty.");
        }
        Task t = new Todo(description);
        tasks.add(t);
        return ui.getTaskAddedMessage(t, tasks.size());
    }

    /**
     * Adds a Deadline task from the provided input for GUI.
     *
     * @param input Raw user input containing description and /by date.
     * @return Formatted response string
     * @throws ChungusException if parts are missing.
     */
    private String addDeadlineForGui(String input) throws ChungusException {
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
        return ui.getTaskAddedMessage(t, tasks.size());
    }

    /**
     * Adds an Event task from the provided input for GUI.
     *
     * @param input Raw user input containing description, /from and /to.
     * @return Formatted response string
     * @throws ChungusException if parts are missing.
     */
    private String addEventForGui(String input) throws ChungusException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new ChungusException(
                    "Event command must include both '/from' and '/to' followed by start and end times.");
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
        return ui.getTaskAddedMessage(t, tasks.size());
    }

    /**
     * Checks if the input command is a bye command.
     *
     * @param input User input command
     * @return true if the command is "bye", false otherwise
     */
    public boolean isExitCommand(String input) {
        return input.trim().equals(CommandType.BYE.getCommand());
    }

    /**
     * App entry point.
     *
     */
    public static void main(String[] args) {
        new Chungus("data/chungus.txt").run();
    }
}
