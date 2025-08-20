import java.util.Scanner;
import java.util.ArrayList;

public class Chungus {
    public static void main(String[] args) {
        ArrayList<Task> list = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        String message = "____________________________________________________________\n"
                + "Hello! I'm Chungus!\n"
                + "What can I do for you?\n"
                + "____________________________________________________________\n";        
        System.out.println(message);

        String input = sc.nextLine();
        while (!input.equals(CommandType.BYE.getCommand())) {
            try {
                CommandType command = CommandType.fromInput(input);
                
                if (command == null) {
                    if (input.trim().isEmpty()) {
                        // Handle empty input silently
                    } else {
                        throw new ChungusException("I'm sorry, but I don't know what that means :-(");
                    }
                } else {
                    processCommand(command, input, list);
                }
            } catch (ChungusException e) {
                System.out.println("____________________________________________________________\n");
                System.out.println("OOPS!!! " + e.getMessage());
                System.out.println("____________________________________________________________\n");
            } catch (Exception e) {
                System.out.println("____________________________________________________________\n");
                System.out.println("OOPS!!! Something went wrong: " + e.getMessage());
                System.out.println("____________________________________________________________\n");
            }
            input = sc.nextLine();
        }
        System.out.println("____________________________________________________________\n");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________\n");
        sc.close();
    }

    private static void processCommand(CommandType command, String input, ArrayList<Task> list) throws ChungusException {
        switch (command) {
            case LIST:
                displayTaskList(list);
                break;
            case MARK:
                if (!input.startsWith(CommandType.MARK.getCommand() + " ")) {
                    throw new ChungusException("Please provide a task number.");
                }
                markTask(input, list, true);
                break;
            case UNMARK:
                if (!input.startsWith(CommandType.UNMARK.getCommand() + " ")) {
                    throw new ChungusException("Please provide a task number.");
                }
                markTask(input, list, false);
                break;
            case DELETE:
                if (!input.startsWith(CommandType.DELETE.getCommand() + " ")) {
                    throw new ChungusException("Please provide a task number.");
                }
                deleteTask(input, list);
                break;
            case TODO:
                if (!input.startsWith(CommandType.TODO.getCommand() + " ")) {
                    throw new ChungusException("The description of a todo cannot be empty.");
                }
                addTodo(input, list);
                break;
            case DEADLINE:
                if (!input.startsWith(CommandType.DEADLINE.getCommand() + " ")) {
                    throw new ChungusException("Deadline command must include '/by' followed by the due date.");
                }
                addDeadline(input, list);
                break;
            case EVENT:
                if (!input.startsWith(CommandType.EVENT.getCommand() + " ")) {
                    throw new ChungusException("Event command must include both '/from' and '/to' followed by start and end times.");
                }
                addEvent(input, list);
                break;
            default:
                throw new ChungusException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private static void displayTaskList(ArrayList<Task> list) {
        System.out.println("____________________________________________________________\n");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, list.get(i).toString()));
        }
        System.out.println("____________________________________________________________\n");
    }

    private static void markTask(String input, ArrayList<Task> list, boolean markAsDone) throws ChungusException {
        int idx = parseTaskIndex(input, markAsDone ? CommandType.MARK.getCommand() : CommandType.UNMARK.getCommand());
        if (idx < 0 || idx >= list.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + list.size());
        }
        
        if (markAsDone) {
            list.get(idx).markAsDone();
            System.out.println("____________________________________________________________\n");
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(String.format("  %s", list.get(idx).toString()));
            System.out.println("____________________________________________________________\n");
        } else {
            list.get(idx).markAsNotDone();
            System.out.println("____________________________________________________________\n");
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println(String.format("  %s", list.get(idx).toString()));
            System.out.println("____________________________________________________________\n");
        }
    }

    private static void deleteTask(String input, ArrayList<Task> list) throws ChungusException {
        int idx = parseTaskIndex(input, CommandType.DELETE.getCommand());
        if (idx < 0 || idx >= list.size()) {
            throw new ChungusException("Invalid task number. Please enter a number between 1 and " + list.size());
        }
        Task deletedTask = list.remove(idx);
        System.out.println("____________________________________________________________\n");
        System.out.println("Noted. I've removed this task:");
        System.out.println(String.format("  %s", deletedTask.toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", list.size()));
        System.out.println("____________________________________________________________\n");
    }

    private static void addTodo(String input, ArrayList<Task> list) throws ChungusException {
        String description = parseDescription(input, CommandType.TODO.getCommand());
        if (description.trim().isEmpty()) {
            throw new ChungusException("The description of a todo cannot be empty.");
        }
        list.add(new Todo(description));
        System.out.println("____________________________________________________________\n");
        System.out.println("Got it. I've added this task:");
        System.out.println(String.format("  %s", list.get(list.size() - 1).toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", list.size()));
        System.out.println("____________________________________________________________\n");
    }

    private static void addDeadline(String input, ArrayList<Task> list) throws ChungusException {
        if (!input.contains("/by")) {
            throw new ChungusException("Deadline command must include '/by' followed by the due date.");
        }
        String[] parts = parseDeadline(input);
        if (parts[0].trim().isEmpty()) {
            throw new ChungusException("The description of a deadline cannot be empty.");
        }
        if (parts[1].trim().isEmpty()) {
            throw new ChungusException("The due date cannot be empty.");
        }
        list.add(new Deadline(parts[0], parts[1]));
        System.out.println("____________________________________________________________\n");
        System.out.println("Got it. I've added this task:");
        System.out.println(String.format("  %s", list.get(list.size() - 1).toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", list.size()));
        System.out.println("____________________________________________________________\n");
    }

    private static void addEvent(String input, ArrayList<Task> list) throws ChungusException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new ChungusException("Event command must include both '/from' and '/to' followed by start and end times.");
        }
        String[] parts = parseEvent(input);
        if (parts[0].trim().isEmpty()) {
            throw new ChungusException("The description of an event cannot be empty.");
        }
        if (parts[1].trim().isEmpty()) {
            throw new ChungusException("The start time cannot be empty.");
        }
        if (parts[2].trim().isEmpty()) {
            throw new ChungusException("The end time cannot be empty.");
        }
        list.add(new Event(parts[0], parts[1], parts[2]));
        System.out.println("____________________________________________________________\n");
        System.out.println("Got it. I've added this task:");
        System.out.println(String.format("  %s", list.get(list.size() - 1).toString()));
        System.out.println(String.format("Now you have %d tasks in the list.", list.size()));
        System.out.println("____________________________________________________________\n");
    }

    private static int parseTaskIndex(String input, String command) throws ChungusException {
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

    private static String parseDescription(String input, String command) {
        return input.substring(command.length()).trim();
    }

    private static String[] parseDeadline(String input) throws ChungusException {
        try {
            String description = input.substring(9, input.indexOf("/by")).trim();
            String by = input.substring(input.indexOf("/by") + 4).trim();
            return new String[]{description, by};
        } catch (StringIndexOutOfBoundsException e) {
            throw new ChungusException("Invalid deadline format. Use: deadline <description> /by <date>");
        }
    }

    private static String[] parseEvent(String input) throws ChungusException {
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
