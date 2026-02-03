import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;


public class Nova {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

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
        switch (cmd.type) {
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
                tasks.mark(cmd.index);
                storage.saveTasks(tasks.getTasks());
                ui.showTaskMarked(tasks.get(cmd.index).toString());
                return false;

            case UNMARK:
                tasks.unmark(cmd.index);
                storage.saveTasks(tasks.getTasks());
                ui.showTaskUnmarked(tasks.get(cmd.index).toString());
                return false;

            case DELETE:
                Task removed = tasks.remove(cmd.index);
                storage.saveTasks(tasks.getTasks());
                ui.showTaskDeleted(removed.toString(), tasks.size());
                return false;

            case TODO:
                tasks.add(new ToDo(cmd.desc));
                storage.saveTasks(tasks.getTasks());
                ui.showTaskAdded(tasks.get(tasks.size() - 1).toString(), tasks.size());
                return false;

            case DEADLINE:
                tasks.add(new Deadline(cmd.desc, cmd.by));
                storage.saveTasks(tasks.getTasks());
                ui.showTaskAdded(tasks.get(tasks.size() - 1).toString(), tasks.size());
                return false;

            case EVENT:
                tasks.add(new Event(cmd.desc, cmd.from, cmd.to));
                storage.saveTasks(tasks.getTasks());
                ui.showTaskAdded(tasks.get(tasks.size() - 1).toString(), tasks.size());
                return false;

            default:
                throw new NovaException("So sorry, I don't understand what that means.");
        }
    }

    public static void main(String[] args) {
        new Nova("data/nova.txt").run();
    }
}