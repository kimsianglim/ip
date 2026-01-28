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
                System.out.println(tasks[idx]);
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
                System.out.println(tasks[idx]);
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                tasks[count] = new ToDo(desc);
                count++;
                System.out.println(LINE);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[count - 1]);
                System.out.println("Now you have " + count + " tasks in the list.");
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("deadline ")) {
                // format: deadline <desc> /by <by>
                int byIndex = input.indexOf(" /by ");
                if (byIndex == -1) {
                    System.out.println(LINE);
                    System.out.println("follow this format: deadline <description> /by <time>");
                    System.out.println(LINE);
                    continue;
                }
                String desc = input.substring(9, byIndex).trim();
                String by = input.substring(byIndex + 5).trim();

                tasks[count] = new Deadline(desc, by);
                count++;
                System.out.println(LINE);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[count - 1]);
                System.out.println("Now you have " + count + " tasks in the list.");
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("event ")) {
                // format: event <desc> /from <from> /to <to>
                int fromIndex = input.indexOf(" /from ");
                int toIndex = input.indexOf(" /to ");
                if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                    System.out.println(LINE);
                    System.out.println("follow this format: event <description> /from <start> /to <end>");
                    System.out.println(LINE);
                    continue;
                }

                String desc = input.substring(6, fromIndex).trim();
                String from = input.substring(fromIndex + 7, toIndex).trim();
                String to = input.substring(toIndex + 5).trim();

                tasks[count] = new Event(desc, from, to);
                count++;
                System.out.println(LINE);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[count - 1]);
                System.out.println("Now you have " + count + " tasks in the list.");
                System.out.println(LINE);
            }

        }
    }
}
