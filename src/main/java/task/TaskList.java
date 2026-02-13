package task;

import java.util.ArrayList;

import exception.NovaException;

/**
 * Represents a collection of tasks.
 * <p>
 * {@code TaskList} manages a list of {@link Task} objects and provides
 * operations to add, remove, retrieve, and update tasks by index.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> loadedTasks) {
        this.tasks = loadedTasks;
    }

    /**
     * Returns the number of tasks in the task list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }


    /**
     * Retrieves the task at the specified index.
     *
     * @param index Zero-based index of the task to retrieve.
     * @return The task at the given index.
     * @throws NovaException If the index is out of bounds.
     */
    public Task get(int index) throws NovaException {
        if (index < 0 || index >= tasks.size()) {
            throw new NovaException("Invalid task number");
        }
        return tasks.get(index);
    }

    /**
     * Adds a task to the task list.
     *
     * @param task The task to be added.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index Zero-based index of the task to remove.
     * @return The removed task.
     * @throws NovaException If the index is out of bounds.
     */
    public Task remove(int index) throws NovaException {
        if (index < 0 || index >= tasks.size()) {
            throw new NovaException("Invalid task number");
        }
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index Zero-based index of the task to mark.
     * @throws NovaException If the index is out of bounds.
     */
    public void mark(int index) throws NovaException {
        get(index).markAsDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index Zero-based index of the task to unmark.
     * @throws NovaException If the index is out of bounds.
     */
    public void unmark(int index) throws NovaException {
        get(index).markAsUndone();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return The list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Finds the indexes of tasks whose descriptions contain the given keyword.
     *
     * @param keyword The keyword to search for within task descriptions.
     * @return A list of zero-based indexes of tasks that contain the keyword.
     *         Returns an empty list if no matches are found.
     */
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
