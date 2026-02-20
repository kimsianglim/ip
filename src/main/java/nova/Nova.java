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
            return handleExitCli();
        case LIST:
            return handleListCli();
        case MARK:
            return handleMarkCli(cmd);
        case UNMARK:
            return handleUnmarkCli(cmd);
        case DELETE:
            return handleDeleteCli(cmd);
        case TODO:
            return handleTodoCli(cmd);
        case DEADLINE:
            return handleDeadlineCli(cmd);
        case EVENT:
            return handleEventCli(cmd);
        case FIND:
            return handleFindCli(cmd);
        default:
            throw unknownCommand();
        }
    }

    private String executeForGui(Command cmd) throws NovaException, IOException {
        switch (cmd.getType()) {
        case EXIT:
            return "Bye. Hope to see you again soon!";
        case LIST:
            return formatListForGui();
        case MARK:
            return formatMarkedForGui(markTask(cmd.getIndex()));
        case UNMARK:
            return formatUnmarkedForGui(unmarkTask(cmd.getIndex()));
        case DELETE: {
            Task removed = deleteTask(cmd.getIndex());
            return formatDeletedForGui(removed);
        }
        case TODO: {
            Task t = addTodo(cmd.getDescription());
            return formatAddedForGui(t);
        }
        case DEADLINE: {
            Task t = addDeadline(cmd.getDescription(), cmd.getBy());
            return formatAddedForGui(t);
        }
        case EVENT: {
            Task t = addEvent(cmd.getDescription(), cmd.getFrom(), cmd.getTo());
            return formatAddedForGui(t);
        }
        case FIND:
            return formatFindForGui(cmd.getDescription());
        default:
            throw unknownCommand();
        }
    }

    private void save() throws IOException {
        storage.saveTasks(tasks.getTasks());
    }

    private Task markTask(int idx) throws IOException, NovaException {
        tasks.mark(idx);
        save();
        return tasks.get(idx);
    }

    private Task unmarkTask(int idx) throws IOException, NovaException {
        tasks.unmark(idx);
        save();
        return tasks.get(idx);
    }

    private Task deleteTask(int idx) throws IOException, NovaException {
        Task removed = tasks.remove(idx);
        save();
        return removed;
    }

    private Task addTodo(String desc) throws IOException, NovaException {
        Task t = new ToDo(desc);
        if (tasks.containsDuplicate(t)) {
            throw new NovaException("This task already exists in your list.");
        }
        tasks.add(t);
        save();
        return t;
    }

    private Task addDeadline(String desc, String by) throws IOException, NovaException {
        Task t = new Deadline(desc, by);
        if (tasks.containsDuplicate(t)) {
            throw new NovaException("This task already exists in your list.");
        }
        tasks.add(t);
        save();
        return t;
    }

    private Task addEvent(String desc, String from, String to) throws IOException, NovaException {
        Task t = new Event(desc, from, to);
        if (tasks.containsDuplicate(t)) {
            throw new NovaException("This task already exists in your list.");
        }
        tasks.add(t);
        save();
        return t;
    }

    private boolean handleExitCli() {
        ui.showBye();
        return true;
    }

    private boolean handleListCli() throws IOException, NovaException {
        ui.showListHeader();
        for (int i = 0; i < tasks.size(); i++) {
            ui.showListItem(i + 1, tasks.get(i).toString());
        }
        ui.showListFooter();
        save();
        return false;
    }

    private boolean handleMarkCli(Command cmd) throws IOException, NovaException {
        Task t = markTask(cmd.getIndex());
        ui.showTaskMarked(t.toString());
        return false;
    }

    private boolean handleUnmarkCli(Command cmd) throws IOException, NovaException {
        Task t = unmarkTask(cmd.getIndex());
        ui.showTaskUnmarked(t.toString());
        return false;
    }

    private boolean handleDeleteCli(Command cmd) throws IOException, NovaException {
        Task removed = deleteTask(cmd.getIndex());
        ui.showTaskDeleted(removed.toString(), tasks.size());
        return false;
    }

    private boolean handleTodoCli(Command cmd) throws IOException, NovaException {
        Task t = addTodo(cmd.getDescription());
        ui.showTaskAdded(t.toString(), tasks.size());
        return false;
    }

    private boolean handleDeadlineCli(Command cmd) throws IOException, NovaException {
        Task t = addDeadline(cmd.getDescription(), cmd.getBy());
        ui.showTaskAdded(t.toString(), tasks.size());
        return false;
    }

    private boolean handleEventCli(Command cmd) throws IOException, NovaException {
        Task t = addEvent(cmd.getDescription(), cmd.getFrom(), cmd.getTo());
        ui.showTaskAdded(t.toString(), tasks.size());
        return false;
    }

    private boolean handleFindCli(Command cmd) throws NovaException {
        ArrayList<Integer> matches = tasks.findIndexes(cmd.getDescription());
        ui.showFindHeader();
        for (int idx : matches) {
            ui.showListItem(idx + 1, tasks.get(idx).toString());
        }
        ui.showListFooter();
        return false;
    }

    private String formatListForGui() throws NovaException {
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private String formatAddedForGui(Task t) {
        return "Got it. I've added this task:\n  " + t
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String formatMarkedForGui(Task t) {
        return "Nice! I've marked this task as done:\n  " + t;
    }

    private String formatUnmarkedForGui(Task t) {
        return "OK, I've marked this task as not done yet:\n  " + t;
    }

    private String formatDeletedForGui(Task removed) {
        return "Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String formatFindForGui(String keyword) throws NovaException {
        ArrayList<Integer> matches = tasks.findIndexes(keyword);
        if (matches.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int idx : matches) {
            sb.append(idx + 1).append(". ").append(tasks.get(idx)).append("\n");
        }
        return sb.toString().trim();
    }

    private NovaException unknownCommand() {
        return new NovaException("So sorry, I don't understand what that means.");
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
