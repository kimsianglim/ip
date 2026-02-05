package nova;

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
        if (input.equals("bye")) {
            return Command.exit();
        }

        if (input.equals("list")) {
            return Command.list();
        }

        if (input.startsWith("mark ")) {
            int idx = parseIndex(input.substring(5), "mark needs a task number, e.g. mark 1");
            return Command.mark(CommandType.MARK, idx);
        }

        if (input.startsWith("unmark ")) {
            int idx = parseIndex(input.substring(7), "unmark needs a task number, e.g. unmark 1");
            return Command.mark(CommandType.UNMARK, idx);
        }

        if (input.startsWith("delete ")) {
            int idx = parseIndex(input.substring(7), "delete needs a task number, e.g. delete 1");
            return Command.delete(idx);
        }

        if (input.equals("todo") || input.startsWith("todo ")) {
            String desc = input.length() > 4 ? input.substring(4).trim() : "";
            if (desc.isEmpty()) throw new NovaException("The description of a todo cannot be empty.");
            return Command.todo(desc);
        }

        if (input.startsWith("deadline ")) {
            int byIndex = input.indexOf(" /by ");
            if (byIndex == -1) throw new NovaException("follow this format: deadline <description> /by <time>");

            String desc = input.substring(9, byIndex).trim();
            String by = input.substring(byIndex + 5).trim();
            if (desc.isEmpty() || by.isEmpty()) throw new NovaException("description and by time cannot be empty.");

            return Command.deadline(desc, by);
        }

        if (input.startsWith("event ")) {
            int fromIndex = input.indexOf(" /from ");
            int toIndex = input.indexOf(" /to ");
            if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                throw new NovaException("follow this format: event <description> /from <start> /to <end>");
            }

            String desc = input.substring(6, fromIndex).trim();
            String from = input.substring(fromIndex + 7, toIndex).trim();
            String to = input.substring(toIndex + 5).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new NovaException("description and time cannot be empty.");
            }

            return Command.event(desc, from, to);
        }

        if (input.equals("find") || input.startsWith("find ")) {
            String keyword = input.length() > 4 ? input.substring(4).trim() : "";
            if (keyword.isEmpty()) {
                throw new NovaException("The keyword for find cannot be empty.");
            }
            return Command.find(keyword);
        }

        throw new NovaException("So sorry, I don't understand what that means.");
    }

    private static int parseIndex(String s, String errorMsg) throws NovaException {
        try {
            int oneBased = Integer.parseInt(s.trim());
            return oneBased - 1;
        } catch (NumberFormatException e) {
            throw new NovaException(errorMsg);
        }
    }
}
