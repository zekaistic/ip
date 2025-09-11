package chungus.logic;

import chungus.common.ChungusException;

/**
 * Responsible for parsing raw user input into structured command data.
 */

public class Parser {
    /**
     * Parses the command type from a raw input line.
     *
     * @param input Raw user input line.
     * @return The {@link CommandType} or null if not recognized.
     */
    public CommandType parseCommandType(String input) {
        return CommandType.fromInput(input);
    }

    /**
     * Parses a priority command of the form: "priority index level".
     *
     * @param input raw user input
     * @return array [taskIndex, levelString]
     * @throws ChungusException if invalid
     */
    public Object[] parsePriorityCommand(String input) throws ChungusException {
        String rest = input.substring(CommandType.PRIORITY.getCommand().length()).trim();
        if (rest.isEmpty()) {
            throw new ChungusException("Please provide a task number and priority level.");
        }
        String[] tokens = rest.split(" ", 2);
        if (tokens.length < 2) {
            throw new ChungusException("Please provide a priority level: high, medium, or low.");
        }
        int index;
        try {
            index = Integer.parseInt(tokens[0]) - 1;
        } catch (NumberFormatException e) {
            throw new ChungusException("Please provide a valid number for the task.");
        }
        String level = tokens[1].trim();
        if (level.isEmpty()) {
            throw new ChungusException("Please provide a priority level: high, medium, or low.");
        }
        return new Object[]{index, level};
    }

    /**
     * Parses a 1-based task index following a command keyword.
     *
     * @param input   Raw user input.
     * @param command Command keyword used as prefix.
     * @return Zero-based index.
     * @throws ChungusException if index is missing or invalid.
     */
    public int parseTaskIndex(String input, String command) throws ChungusException {
        try {
            String indexStr = input.substring(command.length()).trim();
            if (indexStr.isEmpty()) {
                throw new ChungusException("Please provide a task number.");
            }
            return Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new ChungusException("Please provide a valid number for the task.");
        }
    }

    /**
     * Extracts the description following a command keyword.
     *
     * @param input   Raw user input.
     * @param command Command keyword used as prefix.
     * @return Trimmed description string (may be empty).
     */
    public String parseDescription(String input, String command) {
        return input.substring(command.length()).trim();
    }

    /**
     * Parses a deadline command line into description and due date (/by).
     *
     * @param input Raw user input.
     * @return Array [description, byDate].
     * @throws ChungusException for malformed input.
     */
    public String[] parseDeadline(String input) throws ChungusException {
        try {
            String description = input.substring(9, input.indexOf("/by")).trim();
            String by = input.substring(input.indexOf("/by") + 4).trim();
            return new String[]{description, by};
        } catch (StringIndexOutOfBoundsException e) {
            throw new ChungusException("Invalid deadline format. Use: deadline <description> /by <date>");
        }
    }

    /**
     * Parses an event command line into description, from (/from) and to (/to).
     *
     * @param input Raw user input.
     * @return Array [description, from, to].
     * @throws ChungusException for malformed input.
     */
    public String[] parseEvent(String input) throws ChungusException {
        try {
            String description = input.substring(6, input.indexOf("/from")).trim();
            String from = input.substring(input.indexOf("/from") + 6, input.indexOf("/to")).trim();
            String to = input.substring(input.indexOf("/to") + 4).trim();
            return new String[]{description, from, to};
        } catch (StringIndexOutOfBoundsException e) {
            throw new ChungusException("Invalid event format. Use: event <description> /from <start> /to <end>");
        }
    }
}


