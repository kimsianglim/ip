import java.util.Scanner;

public class Nova {
    public static void main(String[] args) {
        System.out.println("Hello! I'm Nova");
        System.out.println("What can I do for you?");
        System.out.println("----------------------------------------");

        Scanner scanner = new Scanner(System.in);

        String[] items = new String[100];
        int count = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("----------------------------------------");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("----------------------------------------");
                break;
            }

            if (input.equals("list")) {
                System.out.println("----------------------------------------");
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + items[i]);
                }
                System.out.println("----------------------------------------");
                continue;
            }

            items[count] = input;
            count++;

            System.out.println("----------------------------------------");
            System.out.println("added: " + input);
            System.out.println("----------------------------------------");
        }
    }
}
