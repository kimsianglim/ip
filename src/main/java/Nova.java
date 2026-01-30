import java.util.Scanner;
import java.util.ArrayList;

public class Nova {

    private static final String LINE = "----------------------------------------";

    public static void main(String[] args) {
        System.out.println("Hello! I'm Nova");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);

        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                processLogic(input, tasks);
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

    private static void processLogic(String input, ArrayList<Task> tasks) throws NovaException {
        if (input.equals("bye")) {
            System.out.println(LINE);
            System.out.println("Bye. Hope to see you again soon!");
            System.out.println(LINE);

            return;
        }

        if (input.equals("list")) {
            System.out.println(LINE);
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
            System.out.println(LINE);

            return;

        }

        if (input.startsWith("mark ")) {
            try {
                int markIndex = Integer.parseInt(input.substring(5).trim()) - 1;

                if (markIndex < 0 || markIndex >= tasks.size()) {
                    throw new NovaException("Invalid task number");
                }

                tasks.get(markIndex).markAsDone();

                System.out.println(LINE);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks.get(markIndex));
                System.out.println(LINE);

            } catch (NumberFormatException e) {
                throw new NovaException("mark needs a task number, e.g. mark 1");
            }

            return;

        }

        if (input.startsWith("unmark ")) {
            try {
                int unmarkIndex = Integer.parseInt(input.substring(7).trim()) - 1;
                if (unmarkIndex < 0 || unmarkIndex >= tasks.size()) {
                    throw new NovaException("Invalid task number");
                }
                tasks.get(unmarkIndex).markAsUndone();

                System.out.println(LINE);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks.get(unmarkIndex));
                System.out.println(LINE);

            } catch (NumberFormatException e) {
                throw new NovaException("unmark needs a task number, e.g. unmark 1");
            }

            return;

        }

        if (input.startsWith("delete ")) {
            try {
                int deleteIndex = Integer.parseInt(input.substring(7).trim()) - 1;

                if (deleteIndex < 0 || deleteIndex >= tasks.size()) {
                    throw new NovaException("Invalid task number");
                }

                Task removed = tasks.remove(deleteIndex);

                System.out.println(LINE);
                System.out.println("Noted. I've removed this task:");
                System.out.println("  " + removed);
                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                System.out.println(LINE);

            } catch (NumberFormatException e) {
                throw new NovaException("delete needs a task number, e.g. delete 1");
            }

            return;
        }


        if (input.equals("todo") || input.startsWith("todo ")) {
            String desc = input.length() > 4 ? input.substring(4).trim() : "";

            if (desc.isEmpty()) {
                throw new NovaException("The description of a todo cannot be empty.");
            }
            tasks.add(new ToDo(desc));
            System.out.println(LINE);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks.get(tasks.size() - 1));
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(LINE);

            return;

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

            tasks.add(new Deadline(desc, by));
            System.out.println(LINE);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks.get(tasks.size() - 1));
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(LINE);

            return;
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

            tasks.add(new Event(desc, from, to));
            System.out.println(LINE);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks.get(tasks.size() - 1));
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(LINE);

            return;
        }

        throw new NovaException("So sorry, I don't understand what that means.");

    }


}

