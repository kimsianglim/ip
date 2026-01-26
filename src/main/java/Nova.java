import java.util.Scanner;

public class Nova {
    public static void main(String[] args) {
        System.out.println("Hello! I'm Nova");
        System.out.println("What can I do for you?");
        System.out.println("----------------------------------------");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("----------------------------------------");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("----------------------------------------");
                break;
            }

            System.out.println("----------------------------------------");
            System.out.println(input);
            System.out.println("----------------------------------------");
        }
    }
}
