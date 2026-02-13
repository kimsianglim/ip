package task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;

/**
 * Represents a deadline task.
 * <p>
 * A {@code Deadline} is a {@link Task} that needs to be done before a specific date/time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATETIME_INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATETIME_OUTPUT = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
    private static final DateTimeFormatter DATE_OUTPUT = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter TIME_OUTPUT = DateTimeFormatter.ofPattern("HH:mm");

    private final String by;
    private final Temporal byTemporal; //the interface for LocalDate, LocalDateTime and LocalTime

    /**
     * Constructs a {@code Deadline} task with the given description and deadline.
     *
     * @param description The description of the task.
     * @param by The deadline of the task as a string. This value will be trimmed
     *           and parsed into a temporal representation if possible.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by.trim();
        this.byTemporal = tryParse(this.by);
    }

    /**
     * Constructs a {@code Deadline} task with the given description, deadline,
     * and completion status.
     *
     * @param description The description of the task.
     * @param by The deadline of the task as a string. This value will be trimmed
     *           and parsed into a temporal representation if possible.
     * @param isDone Indicates whether the task has already been completed.
     */
    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by.trim();
        this.byTemporal = tryParse(this.by);
    }

    private static Temporal tryParse(String s) {
        // Try DateTime
        try {
            return LocalDateTime.parse(s, DATETIME_INPUT);
        } catch (DateTimeParseException ignored) {
            // Fall through and try parsing as date.
        }

        // Try Date
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException ignored) {
            // Fall through and try parsing as time.
        }

        // Try Time
        try {
            return LocalTime.parse(s);
        } catch (DateTimeParseException ignored) {
            // Parsing failed for all supported formats.
        }

        return null;
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + by;
    }

    @Override
    public String toString() {
        String displayBy;
        if (byTemporal instanceof LocalDateTime) {
            displayBy = DATETIME_OUTPUT.format(byTemporal);
        } else if (byTemporal instanceof LocalDate) {
            displayBy = DATE_OUTPUT.format(byTemporal);
        } else if (byTemporal instanceof LocalTime) {
            displayBy = TIME_OUTPUT.format(byTemporal);
        } else {
            displayBy = by;
        }

        return "[D]" + super.toString() + " (by: " + displayBy + ")";
    }
}
