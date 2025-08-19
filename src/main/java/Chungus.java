import java.util.Scanner;

public class Chungus {
    public static void main(String[] args) {
        String[] list = new String[100];
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
                for (int i = 0; i < listIndex; i++) {
                    System.out.println(String.format("%d. %s", i + 1, list[i]));
                }
                System.out.println("____________________________________________________________\n");
            } else {
                list[listIndex] = input;
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
