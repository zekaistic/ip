package chungus.app;

import java.io.IOException;
import java.util.ArrayList;

import chungus.common.ChungusException;
import chungus.common.Constants;
import chungus.logic.CommandType;
import chungus.logic.Parser;
import chungus.model.Deadline;
import chungus.model.Event;
import chungus.model.Task;
import chungus.model.TaskList;
import chungus.model.Todo;
import chungus.storage.Storage;
import chungus.ui.Ui;

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
        while (!isExitCommand(input)) {
            if (isBlank(input)) {
                input = ui.readCommand();
                continue;
            }
            try {
                CommandType command = parser.parseCommandType(input);
                if (command == null) {
                    throw new ChungusException(Constants.MSG_UNKNOWN);
                }
                processCommand(command, input);
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
            requireStartsWith(input, CommandType.FIND.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_KEYWORD);
            findTasks(input);
            break;
        case MARK:
            requireStartsWith(input, CommandType.MARK.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_TASK_NUMBER);
            markTask(input, true);
            break;
        case UNMARK:
            requireStartsWith(input, CommandType.UNMARK.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_TASK_NUMBER);
            markTask(input, false);
            break;
        case DELETE:
            requireStartsWith(input, CommandType.DELETE.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_TASK_NUMBER);
            deleteTask(input);
            break;
        case TODO:
            requireStartsWith(input, CommandType.TODO.getCommand() + Constants.SPACE, Constants.MSG_TODO_EMPTY);
            addTodo(input);
            break;
        case DEADLINE:
            requireStartsWith(input, CommandType.DEADLINE.getCommand() + Constants.SPACE, Constants.MSG_DEADLINE_NEEDS_BY);
            addDeadline(input);
            break;
        case EVENT:
            requireStartsWith(input, CommandType.EVENT.getCommand() + Constants.SPACE, Constants.MSG_EVENT_NEEDS_FROM_TO);
            addEvent(input);
            break;
        default:
            throw new ChungusException(Constants.MSG_UNKNOWN);
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
        requireNotBlank(keyword, Constants.MSG_PROVIDE_KEYWORD);
        ArrayList<Task> matches = tasks.findByKeyword(keyword);
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
        validateIndex(idx);
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
        requireNotBlank(description, Constants.MSG_TODO_EMPTY);
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
        requireContains(input, Constants.TOKEN_BY, Constants.MSG_DEADLINE_NEEDS_BY);
        String[] parts = parser.parseDeadline(input);
        requireNotBlank(parts[0], Constants.MSG_DEADLINE_DESC_EMPTY);
        requireNotBlank(parts[1], Constants.MSG_DEADLINE_DATE_EMPTY);
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
        requireContains(input, Constants.TOKEN_FROM, Constants.MSG_EVENT_NEEDS_FROM_TO);
        requireContains(input, Constants.TOKEN_TO, Constants.MSG_EVENT_NEEDS_FROM_TO);
        String[] parts = parser.parseEvent(input);
        requireNotBlank(parts[0], Constants.MSG_EVENT_DESC_EMPTY);
        requireNotBlank(parts[1], Constants.MSG_EVENT_START_EMPTY);
        requireNotBlank(parts[2], Constants.MSG_EVENT_END_EMPTY);
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
        if (isExitCommand(input)) {
            saveTasksToStorage();
            return ui.getByeMessage();
        }
        try {
            CommandType command = parser.parseCommandType(input);
            if (isBlank(input)) {
                return "";
            }
            if (command == null) {
                throw new ChungusException(Constants.MSG_UNKNOWN);
            }
            return processCommandForGui(command, input);
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
            requireStartsWith(input, CommandType.FIND.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_KEYWORD);
            return findTasksForGui(input);
        case MARK:
            requireStartsWith(input, CommandType.MARK.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_TASK_NUMBER);
            return markTaskForGui(input, true);
        case UNMARK:
            requireStartsWith(input, CommandType.UNMARK.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_TASK_NUMBER);
            return markTaskForGui(input, false);
        case DELETE:
            requireStartsWith(input, CommandType.DELETE.getCommand() + Constants.SPACE, Constants.MSG_PROVIDE_TASK_NUMBER);
            return deleteTaskForGui(input);
        case TODO:
            requireStartsWith(input, CommandType.TODO.getCommand() + Constants.SPACE, Constants.MSG_TODO_EMPTY);
            return addTodoForGui(input);
        case DEADLINE:
            requireStartsWith(input, CommandType.DEADLINE.getCommand() + Constants.SPACE, Constants.MSG_DEADLINE_NEEDS_BY);
            return addDeadlineForGui(input);
        case EVENT:
            requireStartsWith(input, CommandType.EVENT.getCommand() + Constants.SPACE, Constants.MSG_EVENT_NEEDS_FROM_TO);
            return addEventForGui(input);
        default:
            throw new ChungusException(Constants.MSG_UNKNOWN);
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
        requireNotBlank(keyword, Constants.MSG_PROVIDE_KEYWORD);
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
        validateIndex(idx);
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
        requireNotBlank(description, Constants.MSG_TODO_EMPTY);
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
        requireContains(input, Constants.TOKEN_BY, Constants.MSG_DEADLINE_NEEDS_BY);
        String[] parts = parser.parseDeadline(input);
        requireNotBlank(parts[0], Constants.MSG_DEADLINE_DESC_EMPTY);
        requireNotBlank(parts[1], Constants.MSG_DEADLINE_DATE_EMPTY);
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
        requireContains(input, Constants.TOKEN_FROM, Constants.MSG_EVENT_NEEDS_FROM_TO);
        requireContains(input, Constants.TOKEN_TO, Constants.MSG_EVENT_NEEDS_FROM_TO);
        String[] parts = parser.parseEvent(input);
        requireNotBlank(parts[0], Constants.MSG_EVENT_DESC_EMPTY);
        requireNotBlank(parts[1], Constants.MSG_EVENT_START_EMPTY);
        requireNotBlank(parts[2], Constants.MSG_EVENT_END_EMPTY);
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
        new Chungus(Constants.DEFAULT_STORAGE_PATH).run();
    }

    // Small helpers to follow SLAP and reduce nesting/duplication
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static void requireNotBlank(String s, String message) throws ChungusException {
        if (isBlank(s)) {
            throw new ChungusException(message);
        }
    }

    private static void requireStartsWith(String input, String prefix, String message) throws ChungusException {
        if (isBlank(input) || !input.startsWith(prefix)) {
            throw new ChungusException(message);
        }
    }

    private static void requireContains(String input, String token, String message) throws ChungusException {
        if (isBlank(input) || !input.contains(token)) {
            throw new ChungusException(message);
        }
    }

    private void validateIndex(int idx) throws ChungusException {
        if (idx < 0 || idx >= tasks.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + tasks.size());
        }
    }
}


