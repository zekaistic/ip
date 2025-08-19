import java.util.Scanner;

public class Chungus {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String message = "____________________________________________________________\n"
                + "Hello! I'm Chungus!\n"
                + "What can I do for you?\n"
                + "____________________________________________________________\n";
        System.out.println(message);
        String input = sc.nextLine();
        while (!input.equals("bye")) {
            System.out.println("____________________________________________________________\n");
            System.out.println(input);
            System.out.println("____________________________________________________________\n");
            input = sc.nextLine();
        }
        System.out.println("____________________________________________________________\n");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________\n");
        sc.close();
    }
}
