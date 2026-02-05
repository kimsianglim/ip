package nova;

import task.Deadline;
import task.Event;
import task.Task;
import task.ToDo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storage of tasks.
 * <p>
 * {@code Storage} is responsible for reading tasks from and writing tasks
 * to a file on disk. It ensures that the storage file and its parent
 * directories exist before performing any file operations.
 */
public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = java.nio.file.Paths.get(filePath);
    }

    private void ensureExists() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Loads tasks from the storage file.
     * <p>
     * Ensures that the storage file and its parent directories exist,
     * then reads and parses each non-empty line into a {@link Task}.
     *
     * @return A list of tasks loaded from the file. Returns an empty list
     *         if the file contains no tasks.
     * @throws IOException If an I/O error occurs while accessing the file.
     */
    public ArrayList<Task> loadTasks() throws IOException {
        ensureExists();
        List<String> lines = Files.readAllLines(filePath);

        ArrayList<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            tasks.add(parseTask(line));
        }
        return tasks;
    }

    /**
     * Saves the given list of tasks to the storage file.
     * <p>
     * Ensures that the storage file and its parent directories exist,
     * then writes each task to the file using its file representation.
     *
     * @param tasks The list of tasks to be saved.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void saveTasks(ArrayList<Task> tasks) throws IOException {
        ensureExists();
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toFileString());
        }
        Files.write(filePath, lines);
    }

    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String desc = parts[2];

        switch (type) {
            case "T":
                return new ToDo(desc, isDone);
            case "D":
                return new Deadline(desc, parts[3], isDone);
            case "E":
                return new Event(desc, parts[3], parts[4], isDone);
            default:
                throw new IllegalArgumentException("Corrupted save file line: " + line);
        }
    }
}
