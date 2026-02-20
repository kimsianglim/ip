package nova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import command.Command;
import command.CommandType;
import core.Parser;
import exception.NovaException;

/**
 * Tests for the {@link Parser} class.
 * Covers all command types and key edge cases:
 * <ul>
 *   <li>Leading/trailing/multiple spaces</li>
 *   <li>Missing/invalid indices</li>
 *   <li>Token parsing for /by, /from, /to with flexible whitespace</li>
 *   <li>Unknown commands and unexpected extra args for no-arg commands</li>
 * </ul>
 *
 * <p>
 * This test class was initially written independently and later refactored
 * with the assistance of GitHub Copilot to improve structure, coverage,
 * and readability.
 * </p>
 */
public class ParserTest {

    // ==================== Bye / List ====================

    @Test
    public void parse_bye_success() throws NovaException {
        Command c = Parser.parse("bye");
        assertEquals(CommandType.EXIT, c.getType());
    }

    @Test
    public void parseBye_withSpaces_success() throws NovaException {
        Command c = Parser.parse("   bye   ");
        assertEquals(CommandType.EXIT, c.getType());
    }

    @Test
    public void parseBye_withExtraArgs_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("bye now"));
        assertEquals("So sorry, I don't understand what that means.", ex.getMessage());
    }

    @Test
    public void parse_list_success() throws NovaException {
        Command c = Parser.parse("list");
        assertEquals(CommandType.LIST, c.getType());
    }

    @Test
    public void parseList_withSpaces_success() throws NovaException {
        Command c = Parser.parse("  list  ");
        assertEquals(CommandType.LIST, c.getType());
    }

    @Test
    public void parseList_withExtraArgs_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("list all"));
        assertEquals("So sorry, I don't understand what that means.", ex.getMessage());
    }

    // ==================== Mark ====================

    @Test
    public void parseMarkValid_index_success() throws NovaException {
        Command c = Parser.parse("mark 1");
        assertEquals(CommandType.MARK, c.getType());
        assertEquals(0, c.getIndex());
    }

    @Test
    public void parseMarkMultiSpaces_success() throws NovaException {
        Command c = Parser.parse("mark     2");
        assertEquals(CommandType.MARK, c.getType());
        assertEquals(1, c.getIndex());
    }

    @Test
    public void parseMarkNo_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("mark"));
        assertEquals("mark needs a task number, e.g. mark 1", ex.getMessage());
    }

    @Test
    public void parseMarkBlank_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("mark "));
        assertEquals("mark needs a task number, e.g. mark 1", ex.getMessage());
    }

    @Test
    public void parseMarkInvalid_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("mark abc"));
        assertEquals("mark needs a task number, e.g. mark 1", ex.getMessage());
    }

    @Test
    public void parseMarkZero_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("mark 0"));
        assertEquals("mark needs a task number, e.g. mark 1", ex.getMessage());
    }

    @Test
    public void parseMarkNegative_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("mark -1"));
        assertEquals("mark needs a task number, e.g. mark 1", ex.getMessage());
    }

    // ==================== Unmark ====================

    @Test
    public void parseUnmarkValid_index_success() throws NovaException {
        Command c = Parser.parse("unmark 2");
        assertEquals(CommandType.UNMARK, c.getType());
        assertEquals(1, c.getIndex());
    }

    @Test
    public void parseUnmarkNo_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("unmark"));
        assertEquals("unmark needs a task number, e.g. unmark 1", ex.getMessage());
    }

    @Test
    public void parseUnmarkBlank_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("unmark "));
        assertEquals("unmark needs a task number, e.g. unmark 1", ex.getMessage());
    }

    @Test
    public void parseUnmarkInvalid_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("unmark xyz"));
        assertEquals("unmark needs a task number, e.g. unmark 1", ex.getMessage());
    }

    @Test
    public void parseUnmarkZero_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("unmark 0"));
        assertEquals("unmark needs a task number, e.g. unmark 1", ex.getMessage());
    }

    // ==================== Delete ====================

    @Test
    public void parseDeleteValid_index_success() throws NovaException {
        Command c = Parser.parse("delete 3");
        assertEquals(CommandType.DELETE, c.getType());
        assertEquals(2, c.getIndex());
    }

    @Test
    public void parseDeleteNo_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("delete"));
        assertEquals("delete needs a task number, e.g. delete 1", ex.getMessage());
    }

    @Test
    public void parseDeleteBlank_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("delete "));
        assertEquals("delete needs a task number, e.g. delete 1", ex.getMessage());
    }

    @Test
    public void parseDeleteInvalid_number_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("delete one"));
        assertEquals("delete needs a task number, e.g. delete 1", ex.getMessage());
    }

    @Test
    public void parseDeleteNegative_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("delete -2"));
        assertEquals("delete needs a task number, e.g. delete 1", ex.getMessage());
    }

    // ==================== Todo ====================

    @Test
    public void parseTodoValid_desc_success() throws NovaException {
        Command c = Parser.parse("todo read book");
        assertEquals(CommandType.TODO, c.getType());
        assertEquals("read book", c.getDescription());
    }

    @Test
    public void parseTodoLeadingSpaces_desc_success() throws NovaException {
        Command c = Parser.parse("todo     read book");
        assertEquals(CommandType.TODO, c.getType());
        assertEquals("read book", c.getDescription());
    }

    @Test
    public void parseTodoNo_desc_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("todo"));
        assertEquals("The description of a todo cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseTodoEmpty_desc_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("todo "));
        assertEquals("The description of a todo cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseTodoSpaces_desc_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("todo    "));
        assertEquals("The description of a todo cannot be empty.", ex.getMessage());
    }

    // ==================== Deadline ====================

    @Test
    public void parseDeadlineValid_input_success() throws NovaException {
        Command c = Parser.parse("deadline submit report /by 2026-02-10");
        assertEquals(CommandType.DEADLINE, c.getType());
        assertEquals("submit report", c.getDescription());
        assertEquals("2026-02-10", c.getBy());
    }

    @Test
    public void parseDeadlineMulti_spaces_success() throws NovaException {
        Command c = Parser.parse("deadline   submit report   /by   2026-02-10");
        assertEquals(CommandType.DEADLINE, c.getType());
        assertEquals("submit report", c.getDescription());
        assertEquals("2026-02-10", c.getBy());
    }

    @Test
    public void parseDeadlineNo_byToken_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("deadline submit report"));
        assertEquals("follow this format: deadline <description> /by <time>", ex.getMessage());
    }

    @Test
    public void parseDeadlineBy_present_butNoValue() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("deadline submit report /by"));
        assertEquals("description and by time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseDeadlineBy_present_butBlankValue() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("deadline submit report /by   "));
        assertEquals("description and by time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseDeadlineNo_desc_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("deadline /by tomorrow"));
        assertEquals("description and by time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseDeadlineBoth_empty_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("deadline /by"));
        assertEquals("description and by time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseDeadlineTokenNotPrecededByWhitespace_exception() {
        // indexOfToken requires "/by" to be at start or preceded by whitespace
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("deadline submit/by 2026-02-10"));
        assertEquals("follow this format: deadline <description> /by <time>", ex.getMessage());
    }

    // ==================== Event ====================

    @Test
    public void parseEventValid_input_success() throws NovaException {
        Command c = Parser.parse("event camp /from 2026-02-12 /to 2026-02-13");
        assertEquals(CommandType.EVENT, c.getType());
        assertEquals("camp", c.getDescription());
        assertEquals("2026-02-12", c.getFrom());
        assertEquals("2026-02-13", c.getTo());
    }

    @Test
    public void parseEventMulti_words_success() throws NovaException {
        Command c = Parser.parse("event team building camp /from 2026-02-12 /to 2026-02-13");
        assertEquals(CommandType.EVENT, c.getType());
        assertEquals("team building camp", c.getDescription());
        assertEquals("2026-02-12", c.getFrom());
        assertEquals("2026-02-13", c.getTo());
    }

    @Test
    public void parseEventMulti_spaces_success() throws NovaException {
        Command c = Parser.parse("event  camp   /from   2026-02-12   /to   2026-02-13");
        assertEquals(CommandType.EVENT, c.getType());
        assertEquals("camp", c.getDescription());
        assertEquals("2026-02-12", c.getFrom());
        assertEquals("2026-02-13", c.getTo());
    }

    @Test
    public void parseEventNo_tokens_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("event camp"));
        assertEquals("follow this format: event <description> /from <start> /to <end>", ex.getMessage());
    }

    @Test
    public void parseEventNo_toToken_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("event camp /from 2026-02-12"));
        assertEquals("follow this format: event <description> /from <start> /to <end>", ex.getMessage());
    }

    @Test
    public void parseEventNo_fromToken_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("event camp /to 2026-02-13"));
        assertEquals("follow this format: event <description> /from <start> /to <end>", ex.getMessage());
    }

    @Test
    public void parseEventWrong_order_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser
                .parse("event camp /to 2026-02-13 /from 2026-02-12"));
        assertEquals("follow this format: event <description> /from <start> /to <end>", ex.getMessage());
    }

    @Test
    public void parseEventEmpty_desc_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser
                .parse("event /from 2026-02-12 /to 2026-02-13"));
        assertEquals("description and time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseEventEmpty_from_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("event camp /from   /to 2026-02-13"));
        assertEquals("description and time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseEventEmpty_to_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("event camp /from 2026-02-12 /to  "));
        assertEquals("description and time cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseEventTokenNotPrecededByWhitespace_exception() {
        // indexOfToken requires "/from" to be at start or preceded by whitespace
        NovaException ex = assertThrows(NovaException.class, () -> Parser
                .parse("event camp/from 2026-02-12 /to 2026-02-13"));
        assertEquals("follow this format: event <description> /from <start> /to <end>", ex.getMessage());
    }

    // ==================== Find ====================

    @Test
    public void parseFindValid_keyword_success() throws NovaException {
        Command c = Parser.parse("find book");
        assertEquals(CommandType.FIND, c.getType());
        assertEquals("book", c.getDescription());
    }

    @Test
    public void parseFindMulti_words_success() throws NovaException {
        Command c = Parser.parse("find reading book");
        assertEquals(CommandType.FIND, c.getType());
        assertEquals("reading book", c.getDescription());
    }

    @Test
    public void parseFindNo_keyword_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("find"));
        assertEquals("The keyword for find cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseFindEmpty_keyword_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("find "));
        assertEquals("The keyword for find cannot be empty.", ex.getMessage());
    }

    @Test
    public void parseFindSpaces_keyword_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("find    "));
        assertEquals("The keyword for find cannot be empty.", ex.getMessage());
    }

    // ==================== Unknown / Empty ====================

    @Test
    public void parseUnknown_command_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("hello"));
        assertEquals("So sorry, I don't understand what that means.", ex.getMessage());
    }

    @Test
    public void parseRandom_text_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("xyz abc 123"));
        assertEquals("So sorry, I don't understand what that means.", ex.getMessage());
    }

    @Test
    public void parseEmpty_input_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse(""));
        assertEquals("So sorry, I don't understand what that means.", ex.getMessage());
    }

    @Test
    public void parseOnly_spaces_exception() {
        NovaException ex = assertThrows(NovaException.class, () -> Parser.parse("   "));
        assertEquals("So sorry, I don't understand what that means.", ex.getMessage());
    }
}
