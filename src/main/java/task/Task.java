package task;

/**
 * Represents a generic task in the application.
 * <p>
 * A {@code Task} has a description and a completion status.
 * Subclasses such as {@code ToDo}, {@code Deadline}, and {@code Event}
 * may extend this class to provide additional task-specific behaviour.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Constructs a task with the given description.
     * <p>
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructs a task with the given description and completion status.
     *
     * @param description The description of the task.
     * @param isDone Indicates whether the task is already completed.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }


    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getDescription() {
        return description;
    }

    public String toFileString() {
        return "";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
