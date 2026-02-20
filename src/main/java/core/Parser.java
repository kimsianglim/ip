package core;

import command.Command;
import command.CommandType;
import exception.NovaException;

/**
 * Parses user input into {@link Command} objects.
 * <p>
 * This class contains utility methods for validating and interpreting
 * raw command strings entered by the user.
 */
public class Parser {

    /**
     * Parses the user input string into a {@link Command}.
     * <p>
     * Recognised commands include:
     * <ul>
     *   <li>{@code bye}</li>
     *   <li>{@code list}</li>
     *   <li>{@code mark <taskNumber>}</li>
     *   <li>{@code unmark <taskNumber>}</li>
     *   <li>{@code delete <taskNumber>}</li>
     *   <li>{@code todo <description>}</li>
     *   <li>{@code deadline <description> /by <time>}</li>
     *   <li>{@code event <description> /from <start> /to <end>}</li>
     * </ul>
     *
     * @param input The raw command entered by the user.
     * @return A {@code Command} representing the parsed user input.
     * @throws NovaException If the input is invalid, missing required parts, or the command is unrecognised.
     */
    public static Command parse(String input) throws NovaException {
        String trimmed = normalizeInput(input);
        String[] cmdAndRest = splitCommand(trimmed);

        String cmdWord = cmdAndRest[0];
        String rest = cmdAndRest[1];

        switch (cmdWord) {
        case "bye":
            return parseNoArg(rest, Command.exit());
        case "list":
            return parseNoArg(rest, Command.list());
        case "mark":
            return parseMark(rest);
        case "unmark":
            return parseUnmark(rest);
        case "delete":
            return parseDelete(rest);
        case "todo":
            return parseTodo(rest);
        case "deadline":
            return parseDeadline(rest);
        case "event":
            return parseEvent(rest);
        case "find":
            return parseFind(rest);
        default:
            throw new NovaException("So sorry, I don't understand what that means.");
        }
    }

    private static Command parseMark(String rest) throws NovaException {
        int idx = parseIndex(rest, "mark needs a task number, e.g. mark 1");
        return Command.mark(CommandType.MARK, idx);
    }

    private static Command parseUnmark(String rest) throws NovaException {
        int idx = parseIndex(rest, "unmark needs a task number, e.g. unmark 1");
        return Command.mark(CommandType.UNMARK, idx);
    }

    private static Command parseDelete(String rest) throws NovaException {
        int idx = parseIndex(rest, "delete needs a task number, e.g. delete 1");
        return Command.delete(idx);
    }

    private static Command parseTodo(String rest) throws NovaException {
        String desc = rest.trim();
        if (desc.isEmpty()) {
            throw new NovaException("The description of a todo cannot be empty.");
        }
        return Command.todo(desc);
    }

    private static Command parseDeadline(String rest) throws NovaException {
        int byPos = indexOfToken(rest, "/by");
        if (byPos == -1) {
            throw new NovaException("follow this format: deadline <description> /by <time>");
        }

        String desc = rest.substring(0, byPos).trim();
        String by = rest.substring(byPos + 3).trim(); // 3 = length of "/by"
        if (desc.isEmpty() || by.isEmpty()) {
            throw new NovaException("description and by time cannot be empty.");
        }

        return Command.deadline(desc, by);
    }

    private static Command parseEvent(String rest) throws NovaException {
        int fromPos = indexOfToken(rest, "/from");
        int toPos = indexOfToken(rest, "/to");
        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new NovaException("follow this format: event <description> /from <start> /to <end>");
        }

        String desc = rest.substring(0, fromPos).trim();
        String from = rest.substring(fromPos + 5, toPos).trim(); // 5 = length of "/from"
        String to = rest.substring(toPos + 3).trim(); // 3 = length of "/to"
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new NovaException("description and time cannot be empty.");
        }

        return Command.event(desc, from, to);
    }

    private static Command parseFind(String rest) throws NovaException {
        String keyword = rest.trim();
        if (keyword.isEmpty()) {
            throw new NovaException("The keyword for find cannot be empty.");
        }
        return Command.find(keyword);
    }

    private static String normalizeInput(String input) throws NovaException {
        if (input == null) {
            throw new NovaException("So sorry, I don't understand what that means.");
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new NovaException("So sorry, I don't understand what that means.");
        }
        return trimmed;
    }

    /**
     * Splits input into:
     * <ul>
     *   <li>command word (first token)</li>
     *   <li>rest of the input (maybe empty string)</li>
     * </ul>
     *
     * @param trimmed non-empty trimmed input
     * @return an array of length 2: {commandWord, rest}
     */
    private static String[] splitCommand(String trimmed) {
        String[] parts = trimmed.split("\\s+", 2);
        String cmdWord = parts[0];
        String rest = parts.length == 2 ? parts[1] : "";
        return new String[] { cmdWord, rest };
    }

    /**
     * Ensures the command has no extra arguments.
     *
     * @param rest the remainder of the user input after the command word
     * @param command the command to return if valid
     * @return the given command if {@code rest} is empty
     * @throws NovaException if there are unexpected extra arguments
     */
    private static Command parseNoArg(String rest, Command command) throws NovaException {
        if (!rest.isEmpty()) {
            throw new NovaException("So sorry, I don't understand what that means.");
        }
        return command;
    }

    /**
     * Parses a one-based task number from the given string and converts it to a zero-based index.
     * Rejects missing, non-integer, and numbers &lt; 1.
     */
    private static int parseIndex(String s, String errorMsg) throws NovaException {
        String trimmed = s == null ? "" : s.trim();
        if (trimmed.isEmpty()) {
            throw new NovaException(errorMsg);
        }

        try {
            int oneBased = Integer.parseInt(trimmed);
            if (oneBased < 1) {
                throw new NovaException(errorMsg);
            }
            return oneBased - 1;
        } catch (NumberFormatException e) {
            throw new NovaException(errorMsg);
        }
    }

    /**
     * Finds the position of a token (e.g. "/by", "/from", "/to") even if spacing varies.
     * Matches only when the token is at the start of the string or preceded by whitespace,
     * so "abc/by" does not match but "abc /by" matches.
     */
    private static int indexOfToken(String s, String token) {
        if (s == null || token == null || token.isEmpty()) {
            return -1;
        }

        for (int i = 0; i <= s.length() - token.length(); i++) {
            if (s.regionMatches(i, token, 0, token.length())) {
                boolean atStart = (i == 0);
                boolean precededBySpace = (!atStart && Character.isWhitespace(s.charAt(i - 1)));
                if (atStart || precededBySpace) {
                    return i;
                }
            }
        }
        return -1;
    }
}
