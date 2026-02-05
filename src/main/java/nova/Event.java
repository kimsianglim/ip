package nova;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;

/**
 * Represents an event task.
 * <p>
 * A {@code Event} is a {@link Task} that starts at a specific date/time and ends at a specific date/time
 */
public class Event extends Task {
    private final String from;
    private final String to;

    private final Temporal fromTemporal;
    private final Temporal toTemporal;

    private static final DateTimeFormatter DATETIME_INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATETIME_OUTPUT = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
    private static final DateTimeFormatter DATE_OUTPUT = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter TIME_OUTPUT = DateTimeFormatter.ofPattern("HH:mm");

    public Event(String description, String from, String to) {
        super(description);
        this.from = from.trim();
        this.to = to.trim();
        this.fromTemporal = tryParse(this.from);
        this.toTemporal = tryParse(this.to);
    }

    public Event(String description, String from, String to, boolean isDone) {
        super(description, isDone);
        this.from = from.trim();
        this.to = to.trim();
        this.fromTemporal = tryParse(this.from);
        this.toTemporal = tryParse(this.to);
    }

    private static Temporal tryParse(String s) {

        try {
            return LocalDateTime.parse(s, DATETIME_INPUT);
        } catch (DateTimeParseException ignored) { }


        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException ignored) { }


        try {
            return LocalTime.parse(s);
        } catch (DateTimeParseException ignored) { }

        return null;
    }

    @Override
    public String toFileString() {
        return "E | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        String displayFrom;
        if (fromTemporal instanceof LocalDateTime) {
            displayFrom = DATETIME_OUTPUT.format(fromTemporal);
        } else if (fromTemporal instanceof LocalDate) {
            displayFrom = DATE_OUTPUT.format(fromTemporal);
        } else if (fromTemporal instanceof LocalTime) {
            displayFrom = TIME_OUTPUT.format(fromTemporal);
        } else {
            displayFrom = from;
        }

        String displayTo;
        if (toTemporal instanceof LocalDateTime) {
            displayTo = DATETIME_OUTPUT.format(toTemporal);
        } else if (toTemporal instanceof LocalDate) {
            displayTo = DATE_OUTPUT.format(toTemporal);
        } else if (toTemporal instanceof LocalTime) {
            displayTo = TIME_OUTPUT.format(toTemporal);
        } else {
            displayTo = to;
        }

        return "[E]" + super.toString() + " (from: " + displayFrom + " to: " + displayTo + ")";
    }

}
