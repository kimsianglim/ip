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
            String input = scanner.nextLine().trim();

            try {
                count = processLogic(input, tasks, count);
                if (input.equals("bye")) {
                    break;
                }
            } catch (NovaException e) {
                System.out.println(LINE);
                System.out.println("OOPS!!! " + e.getMessage());
                System.out.println(LINE);
            }
        }
    }

    private static int processLogic(String input, Task[] tasks, int count) throws NovaException {
        if (input.equals("bye")) {
            System.out.println(LINE);
            System.out.println("Bye. Hope to see you again soon!");
            System.out.println(LINE);
            return count;
        }

        if (input.equals("list")) {
            System.out.println(LINE);
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < count; i++) {
                System.out.println((i + 1) + ". " + tasks[i]);
            }
            System.out.println(LINE);
            return count;

        }

        if (input.startsWith("mark ")) {
            int idx = Integer.parseInt(input.substring(5).trim()) - 1;

            if (idx < 0 || idx >= count) {
                throw new NovaException("Invalid task number");
            }

            tasks[idx].markAsDone();

            System.out.println(LINE);
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks[idx]);
            System.out.println(LINE);
            return count;

        }

        if (input.startsWith("unmark ")) {
            int idx = Integer.parseInt(input.substring(7).trim()) - 1;

            if (idx < 0 || idx >= count) {
                throw new NovaException("Invalid task number");
            }

            tasks[idx].markAsUndone();

            System.out.println(LINE);
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks[idx]);
            System.out.println(LINE);
            return count;

        }

        if (input.startsWith("todo ")) {
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new NovaException("The description of a todo cannot be empty.");
            }
            tasks[count] = new ToDo(desc);
            count++;
            System.out.println(LINE);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks[count - 1]);
            System.out.println("Now you have " + count + " tasks in the list.");
            System.out.println(LINE);
            return count;

        }

        if (input.startsWith("deadline ")) {
            // format: deadline <desc> /by <by>
            int byIndex = input.indexOf(" /by ");
            if (byIndex == -1) {
                throw new NovaException("follow this format: deadline <description> /by <time>");
            }

            String desc = input.substring(9, byIndex).trim();
            String by = input.substring(byIndex + 5).trim();

            if (desc.isEmpty() || by.isEmpty()) {
                throw new NovaException("description and by time cannot be empty.");
            }

            tasks[count] = new Deadline(desc, by);
            count++;
            System.out.println(LINE);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks[count - 1]);
            System.out.println("Now you have " + count + " tasks in the list.");
            System.out.println(LINE);
            return count;
        }

        if (input.startsWith("event ")) {
            // format: event <desc> /from <from> /to <to>
            int fromIndex = input.indexOf(" /from ");
            int toIndex = input.indexOf(" /to ");
            if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                throw new NovaException("follow this format: event <description> /from <start> /to <end>");
            }

            String desc = input.substring(6, fromIndex).trim();
            String from = input.substring(fromIndex + 7, toIndex).trim();
            String to = input.substring(toIndex + 5).trim();

            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new NovaException("description and time cannot be empty.");
            }

            tasks[count] = new Event(desc, from, to);
            count++;
            System.out.println(LINE);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks[count - 1]);
            System.out.println("Now you have " + count + " tasks in the list.");
            System.out.println(LINE);

            return count;
        }

        throw new NovaException("So sorry, I don't understand what that means.");

    }


}

