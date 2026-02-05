package nova;

import java.util.Scanner;

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

    public void showListHeader() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
    }

    public void showListItem(int index, String taskString) {
        System.out.println(index + ". " + taskString);
    }

    public void showListFooter() {
        System.out.println(LINE);
    }

    public void showTaskMarked(String taskString) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + taskString);
        System.out.println(LINE);
    }

    public void showTaskUnmarked(String taskString) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + taskString);
        System.out.println(LINE);
    }

    public void showTaskDeleted(String removedTaskString, int remaining) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTaskString);
        System.out.println("Now you have " + remaining + " tasks in the list.");
        System.out.println(LINE);
    }

    public void showTaskAdded(String taskString, int size) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + taskString);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }
}