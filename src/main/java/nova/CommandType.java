package nova;

/**
 * Represents the types of commands supported by the application.
 * <p>
 * Each constant corresponds to a specific user command that can be
 * parsed and executed.
 */
public enum CommandType {
    EXIT, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND
}
