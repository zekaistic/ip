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
                int idx = Integer.parseInt(input.substring(5).trim()) - 1;
                if (idx >= 0 && idx < listIndex) {
                    list[idx].markAsDone();
                    System.out.println("____________________________________________________________\n");
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(String.format("  %s", list[idx].toString()));
                    System.out.println("____________________________________________________________\n");
                }
            } else if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7).trim()) - 1;
                if (idx >= 0 && idx < listIndex) {
                    list[idx].markAsNotDone();
                    System.out.println("____________________________________________________________\n");
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(String.format("  %s", list[idx].toString()));
                    System.out.println("____________________________________________________________\n");
                }
            } else {
                list[listIndex] = new Task(input);
                listIndex++;
                System.out.println("____________________________________________________________\n");
                System.out.println(String.format("added: %s", input));
                System.out.println("____________________________________________________________\n");
            }
            input = sc.nextLine();
        }
        System.out.println("____________________________________________________________\n");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________\n");
        sc.close();
    }
}
