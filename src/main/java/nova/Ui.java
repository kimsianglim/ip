package nova;

import java.util.Scanner;

/**
 * Handles user interaction via the command line.
 * <p>
 * {@code Ui} is responsible for reading user input and displaying messages
 * such as prompts, errors, and task-related feedback.
 */
public class Ui {
    private static final String LINE = "----------------------------------------";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the welcome message when the application starts.
     * <p>
     * Prints a greeting, a short prompt, and a separator line to the console.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm nova.Nova");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Reads a command from standard input.
     *
     * @return The trimmed user input line.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays an error message to the user.
     * <p>
     * Prints the given message prefixed with an error indicator and
     * surrounded by separator lines.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println("OOPS!!! " + message);
        System.out.println(LINE);
    }

    public void showLoadingError() {
        showError("I couldn't load your tasks file.");
    }

    /**
     * Displays the farewell message when the application exits.
     * <p>
     * Prints a goodbye message surrounded by separator lines.
     */
    public void showBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays the header for the task list.
     * <p>
     * Prints a separator line followed by a message indicating
     * that the list of tasks will be shown.
     */
    public void showListHeader() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
    }

    /**
     * Displays a single task item in the task list.
     *
     * @param index The one-based index of the task.
     * @param taskString The string representation of the task.
     */
    public void showListItem(int index, String taskString) {
        System.out.println(index + ". " + taskString);
    }

    /**
     * Displays the footer for the task list.
     * <p>
     * Prints a separator line to indicate the end of the list.
     */
    public void showListFooter() {
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message that a task has been marked as done.
     *
     * @param taskString The string representation of the marked task.
     */
    public void showTaskMarked(String taskString) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + taskString);
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message that a task has been marked as not done.
     *
     * @param taskString The string representation of the unmarked task.
     */
    public void showTaskUnmarked(String taskString) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + taskString);
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message that a task has been deleted.
     *
     * @param removedTaskString The string representation of the deleted task.
     * @param remaining The number of tasks remaining in the list.
     */
    public void showTaskDeleted(String removedTaskString, int remaining) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTaskString);
        System.out.println("Now you have " + remaining + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message that a task has been added.
     *
     * @param taskString The string representation of the added task.
     * @param size The updated number of tasks in the list.
     */
    public void showTaskAdded(String taskString, int size) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + taskString);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays the header for the find results.
     * <p>
     * Prints a separator line followed by a message indicating
     * that matching tasks will be shown.
     */
    public void showFindHeader() {
        System.out.println(LINE);
        System.out.println("Here are the matching tasks in your list:");
    }
}
