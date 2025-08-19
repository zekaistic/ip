import java.util.Scanner;

public class Chungus {
    public static void main(String[] args) {
        Task[] list = new Task[100];
        int listIndex = 0;

        Scanner sc = new Scanner(System.in);
        String message = "____________________________________________________________\n"
                + "Hello! I'm Chungus!\n"
                + "What can I do for you?\n"
                + "____________________________________________________________\n";        
        System.out.println(message);

        String input = sc.nextLine();
        while (!input.equals("bye")) {
            if (input.equals("list")) {
                System.out.println("____________________________________________________________\n");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < listIndex; i++) {
                    System.out.println(String.format("%d. %s", i + 1, list[i].toString()));
                }
                System.out.println("____________________________________________________________\n");
            } else if (input.startsWith("mark ")) {
                int idx = parseTaskIndex(input, "mark");
                if (idx >= 0 && idx < listIndex) {
                    list[idx].markAsDone();
                    System.out.println("____________________________________________________________\n");
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(String.format("  %s", list[idx].toString()));
                    System.out.println("____________________________________________________________\n");
                }
            } else if (input.startsWith("unmark ")) {
                int idx = parseTaskIndex(input, "unmark");
                if (idx >= 0 && idx < listIndex) {
                    list[idx].markAsNotDone();
                    System.out.println("____________________________________________________________\n");
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(String.format("  %s", list[idx].toString()));
                    System.out.println("____________________________________________________________\n");
                }
            } else if (input.startsWith("todo ")) {
                String description = parseDescription(input, "todo");
                list[listIndex] = new Todo(description);
                listIndex++;
                System.out.println("____________________________________________________________\n");
                System.out.println("Got it. I've added this task:");
                System.out.println(String.format("  %s", list[listIndex - 1].toString()));
                System.out.println(String.format("Now you have %d tasks in the list.", listIndex));
                System.out.println("____________________________________________________________\n");
            } else if (input.startsWith("deadline ")) {
                String[] parts = parseDeadline(input);
                list[listIndex] = new Deadline(parts[0], parts[1]);
                listIndex++;
                System.out.println("____________________________________________________________\n");
                System.out.println("Got it. I've added this task:");
                System.out.println(String.format("  %s", list[listIndex - 1].toString()));
                System.out.println(String.format("Now you have %d tasks in the list.", listIndex));
                System.out.println("____________________________________________________________\n");
            } else if (input.startsWith("event ")) {
                String[] parts = parseEvent(input);
                list[listIndex] = new Event(parts[0], parts[1], parts[2]);
                listIndex++;
                System.out.println("____________________________________________________________\n");
                System.out.println("Got it. I've added this task:");
                System.out.println(String.format("  %s", list[listIndex - 1].toString()));
                System.out.println(String.format("Now you have %d tasks in the list.", listIndex));
                System.out.println("____________________________________________________________\n");
            } else {
                System.out.println("____________________________________________________________\n");
                System.out.println("Sorry, start your commands with 'todo', 'deadline', or 'event'.");
                System.out.println("____________________________________________________________\n");
            }
            input = sc.nextLine();
        }
        System.out.println("____________________________________________________________\n");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________\n");
        sc.close();
    }

    private static int parseTaskIndex(String input, String command) {
        try {
            return Integer.parseInt(input.substring(command.length()).trim()) - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String parseDescription(String input, String command) {
        return input.substring(command.length()).trim();
    }

    private static String[] parseDeadline(String input) {
        String description = input.substring(9, input.indexOf("/by")).trim();
        String by = input.substring(input.indexOf("/by") + 4).trim();
        return new String[]{description, by};
    }

    private static String[] parseEvent(String input) {
        String description = input.substring(6, input.indexOf("/from")).trim();
        String from = input.substring(input.indexOf("/from") + 6, input.indexOf("/to")).trim();
        String to = input.substring(input.indexOf("/to") + 4).trim();
        return new String[]{description, from, to};
    }
}
