package chungus;

public enum CommandType {
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    BYE("bye");

    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

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

    public boolean matches(String input) {
        return input.startsWith(this.command + " ") || input.equals(this.command);
    }
}


