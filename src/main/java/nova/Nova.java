package nova;

import java.io.IOException;
import java.util.ArrayList;

import exception.NovaException;
import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.ToDo;

/**
 * The main entry point for the Nova application.
 * <p>
 * It works with {@link Ui}, {@link Parser}, {@link TaskList},
 * and {@link Storage}, loads existing tasks on startup and runs a command loop
 * until the user exits.
 */
public class Nova {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a {@code Nova} application instance.
     * <p>
     * Initializes the user interface and storage components, and attempts
     * to load existing tasks from the specified file path. If loading fails
     * due to an I/O error, an error message is displayed and the application
     * starts with an empty task list.
     *
     * @param filePath The file path used for loading and saving tasks.
     */
    public Nova(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loaded;
        try {
            ArrayList<Task> loadedTasks = storage.loadTasks();
            loaded = new TaskList(loadedTasks);
        } catch (IOException e) {
            ui.showLoadingError();
            loaded = new TaskList();
        }
        tasks = loaded;
    }

    /**
     * Runs the main command loop of the application.
     * <p>
     * Displays a welcome message, then repeatedly reads user input,
     * parses it into a {@link Command}, and executes it until an exit
     * command is issued.
     * <p>
     * Any {@link NovaException} thrown during parsing or execution is
     * caught and shown to the user as an error message. Any {@link IOException}
     * related to loading or saving tasks is also caught and reported.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                Command cmd = Parser.parse(input);
                boolean shouldExit = execute(cmd);
                if (shouldExit) {
                    return;
                }
            } catch (NovaException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("I couldn't save/load your tasks file.");
            }
        }
    }

    private boolean execute(Command cmd) throws NovaException, IOException {
        switch (cmd.getType()) {
        case EXIT:
            ui.showBye();
            return true;

        case LIST:
            ui.showListHeader();
            for (int i = 0; i < tasks.size(); i++) {
                ui.showListItem(i + 1, tasks.get(i).toString());
            }
            ui.showListFooter();
            storage.saveTasks(tasks.getTasks());
            return false;

        case MARK:
            tasks.mark(cmd.getIndex());
            storage.saveTasks(tasks.getTasks());
            ui.showTaskMarked(tasks.get(cmd.getIndex()).toString());
            return false;

        case UNMARK:
            tasks.unmark(cmd.getIndex());
            storage.saveTasks(tasks.getTasks());
            ui.showTaskUnmarked(tasks.get(cmd.getIndex()).toString());
            return false;

        case DELETE:
            Task removed = tasks.remove(cmd.getIndex());
            storage.saveTasks(tasks.getTasks());
            ui.showTaskDeleted(removed.toString(), tasks.size());
            return false;

        case TODO:
            tasks.add(new ToDo(cmd.getDescription()));
            storage.saveTasks(tasks.getTasks());
            ui.showTaskAdded(tasks.get(tasks.size() - 1).toString(), tasks.size());
            return false;

        case DEADLINE:
            tasks.add(new Deadline(cmd.getDescription(), cmd.getBy()));
            storage.saveTasks(tasks.getTasks());
            ui.showTaskAdded(tasks.get(tasks.size() - 1).toString(), tasks.size());
            return false;

        case EVENT:
            tasks.add(new Event(cmd.getDescription(), cmd.getFrom(), cmd.getTo()));
            storage.saveTasks(tasks.getTasks());
            ui.showTaskAdded(tasks.get(tasks.size() - 1).toString(), tasks.size());
            return false;

        case FIND:
            ArrayList<Integer> matches = tasks.findIndexes(cmd.getDescription());
            ui.showFindHeader();
            for (int idx : matches) {
                ui.showListItem(idx + 1, tasks.get(idx).toString());
            }
            ui.showListFooter();
            return false;

        default:
            throw new NovaException("So sorry, I don't understand what that means.");
        }
    }

    private String executeForGui(Command cmd) throws NovaException, IOException {
        switch (cmd.getType()) {
        case EXIT:
            return "Bye. Hope to see you again soon!";

        case LIST: {
            StringBuilder sb = new StringBuilder();
            sb.append("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
            }
            return sb.toString().trim();
        }

        case MARK: {
            tasks.mark(cmd.getIndex());
            storage.saveTasks(tasks.getTasks());
            return "Nice! I've marked this task as done:\n  " + tasks.get(cmd.getIndex());
        }

        case UNMARK: {
            tasks.unmark(cmd.getIndex());
            storage.saveTasks(tasks.getTasks());
            return "OK, I've marked this task as not done yet:\n  " + tasks.get(cmd.getIndex());
        }

        case DELETE: {
            Task removed = tasks.remove(cmd.getIndex());
            storage.saveTasks(tasks.getTasks());
            return "Noted. I've removed this task:\n  " + removed
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        }

        case TODO: {
            Task t = new ToDo(cmd.getDescription());
            tasks.add(t);
            storage.saveTasks(tasks.getTasks());
            return "Got it. I've added this task:\n  " + t
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        }

        case DEADLINE: {
            Task t = new Deadline(cmd.getDescription(), cmd.getBy());
            tasks.add(t);
            storage.saveTasks(tasks.getTasks());
            return "Got it. I've added this task:\n  " + t
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        }

        case EVENT: {
            Task t = new Event(cmd.getDescription(), cmd.getFrom(), cmd.getTo());
            tasks.add(t);
            storage.saveTasks(tasks.getTasks());
            return "Got it. I've added this task:\n  " + t
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        }

        default:
            throw new NovaException("So sorry, I don't understand what that means.");
        }
    }

    public String getResponse(String input) {
        try {
            Command cmd = Parser.parse(input);
            return executeForGui(cmd);
        } catch (NovaException e) {
            return "OOPS!!! " + e.getMessage();
        } catch (IOException e) {
            return "I couldn't save/load your tasks file.";
        }
    }

    public static void main(String[] args) {
        new Nova("data/nova.txt").run();
    }
}
