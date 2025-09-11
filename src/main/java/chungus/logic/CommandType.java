package chungus.logic;

/**
 * Supported command keywords accepted by the application.
 */
public enum CommandType {
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    FIND("find"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    BYE("bye");

    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    /**
     * Returns the raw command string.
     *
     * @return command keyword
     */
    public String getCommand() {
        return command;
    }

    /**
     * Attempts to infer the command from user input.
     *
     * @param input raw input line
     * @return matching {@link CommandType} or null
     */
    public static CommandType fromInput(String input) {
        String trimmedInput = input.trim();

        // Handle exact matches first
        for (CommandType cmd : values()) {
            if (trimmedInput.equals(cmd.command)) {
                return cmd;
            }
        }

        // Handle commands with arguments
        for (CommandType cmd : values()) {
            if (trimmedInput.startsWith(cmd.command + " ")) {
                return cmd;
            }
        }

        return null;
    }

    /**
     * Checks if the input matches this command, with or without arguments.
     *
     * @param input raw input
     * @return true if matches
     */
    public boolean matches(String input) {
        return input.startsWith(this.command + " ") || input.equals(this.command);
    }
}


