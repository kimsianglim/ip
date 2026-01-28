import java.util.Scanner;

public class Nova {

    private static final String LINE = "----------------------------------------";

    public static void main(String[] args) {
        System.out.println("Hello! I'm Nova");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int count = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println(LINE);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            if (input.equals("list")) {
                System.out.println(LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5).trim()) - 1;

                if (idx < 0 || idx >= count) {
                    System.out.println(LINE);
                    System.out.println("Invalid task number.");
                    System.out.println(LINE);
                    continue;
                }

                tasks[idx].markAsDone();

                System.out.println(LINE);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[idx]); // prints [X] description
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7).trim()) - 1;

                if (idx < 0 || idx >= count) {
                    System.out.println(LINE);
                    System.out.println("Invalid task number.");
                    System.out.println(LINE);
                    continue;
                }

                tasks[idx].markAsUndone();

                System.out.println(LINE);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[idx]); // prints [ ] description
                System.out.println(LINE);
                continue;
            }

            tasks[count] = new Task(input);
            System.out.println(LINE);
            System.out.println("added: " + tasks[count]); // shows [ ] description
            System.out.println(LINE);
            count++;
        }
    }
}
