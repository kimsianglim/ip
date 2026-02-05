package nova;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> loadedTasks) {
        this.tasks = loadedTasks;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) throws NovaException {
        if (index < 0 || index >= tasks.size()) {
            throw new NovaException("Invalid task number");
        }
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) throws NovaException {
        if (index < 0 || index >= tasks.size()) {
            throw new NovaException("Invalid task number");
        }
        return tasks.remove(index);
    }

    public void mark(int index) throws NovaException {
        get(index).markAsDone();
    }

    public void unmark(int index) throws NovaException {
        get(index).markAsUndone();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Integer> findIndexes(String keyword) {
        ArrayList<Integer> matches = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDescription().contains(keyword)) {
                matches.add(i);
            }
        }
        return matches;
    }
}
