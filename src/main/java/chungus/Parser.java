package chungus;

public class Parser {
    public CommandType parseCommandType(String input) {
        return CommandType.fromInput(input);
    }

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

    public String parseDescription(String input, String command) {
        return input.substring(command.length()).trim();
    }

    public String[] parseDeadline(String input) throws ChungusException {
        try {
            String description = input.substring(9, input.indexOf("/by")).trim();
            String by = input.substring(input.indexOf("/by") + 4).trim();
            return new String[]{description, by};
        } catch (StringIndexOutOfBoundsException e) {
            throw new ChungusException("Invalid deadline format. Use: deadline <description> /by <date>");
        }
    }

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


