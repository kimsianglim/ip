package nova;

import static org.junit.jupiter.api.Assertions.*;

import exception.NovaException;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void parse_bye_returnsExitCommand() throws NovaException {
        Command c = Parser.parse("bye");
        assertEquals(CommandType.EXIT, c.getType());
    }

    @Test
    public void parse_list_returnsListCommand() throws NovaException {
        Command c = Parser.parse("list");
        assertEquals(CommandType.LIST, c.getType());
    }

    @Test
    public void parse_mark_convertsToZeroBasedIndex() throws NovaException {
        Command c = Parser.parse("mark 1");
        assertEquals(CommandType.MARK, c.getType());
        assertEquals(0, c.getIndex());
    }

    @Test
    public void parse_unmark_convertsToZeroBasedIndex() throws NovaException {
        Command c = Parser.parse("unmark 2");
        assertEquals(CommandType.UNMARK, c.getType());
        assertEquals(1, c.getIndex());
    }

    @Test
    public void parse_delete_convertsToZeroBasedIndex() throws NovaException {
        Command c = Parser.parse("delete 3");
        assertEquals(CommandType.DELETE, c.getType());
        assertEquals(2, c.getIndex());
    }

    @Test
    public void parse_mark_missingNumber_throws() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("mark "));
        assertEquals("mark needs a task number, e.g. mark 1", ex.getMessage());
    }

    @Test
    public void parse_mark_notANumber_throws() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("mark abc"));
        assertEquals("mark needs a task number, e.g. mark 1", ex.getMessage());
    }

    @Test
    public void parse_todo_valid_parsesDescription() throws NovaException {
        Command c = Parser.parse("todo read book");
        assertEquals(CommandType.TODO, c.getType());
        assertEquals("read book", c.getDescription());
    }

    @Test
    public void parse_todo_empty_throws() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("todo"));
        assertEquals("The description of a todo cannot be empty.", ex.getMessage());
    }

    @Test
    public void parse_deadline_valid_parsesFields() throws NovaException {
        Command c = Parser.parse("deadline submit report /by 2026-02-10");
        assertEquals(CommandType.DEADLINE, c.getType());
        assertEquals("submit report", c.getDescription());
        assertEquals("2026-02-10", c.getBy());
    }

    @Test
    public void parse_deadline_missingByToken_throws() {
        NovaException ex = assertThrows(NovaException.class,
                () -> Parser.parse("deadline submit report"));
        assertEquals("follow this format: deadline <description> /by <time>", ex.getMessage());
    }

    @Test
    public void parse_deadline_emptyParts_throws() {
        NovaException ex = assertThrows(NovaException.class,
                () -> Parser.parse("deadline  /by tomorrow"));
        assertEquals("description and by time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parse_event_valid_parsesFields() throws NovaException {
        Command c = Parser.parse("event camp /from 2026-02-12 /to 2026-02-13");
        assertEquals(CommandType.EVENT, c.getType());
        assertEquals("camp", c.getDescription());
        assertEquals("2026-02-12", c.getFrom());
        assertEquals("2026-02-13", c.getTo());
    }

    @Test
    public void parse_event_missingTokens_throws() {
        NovaException ex = assertThrows(NovaException.class,
                () -> Parser.parse("event camp /from 2026-02-12"));
        assertEquals("follow this format: event <description> /from <start> /to <end>", ex.getMessage());
    }

    @Test
    public void parse_unknownCommand_throws() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("hello"));
        assertEquals("So sorry, I don't understand what that means.", ex.getMessage());
    }
}