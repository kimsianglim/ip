package nova;

public class Command {
    private final CommandType type;
    private final Integer index;
    private final String desc;
    private final String by;
    private final String from;
    private final String to;

    private Command(CommandType type, Integer index, String desc, String by, String from, String to) {
        this.type = type;
        this.index = index;
        this.desc = desc;
        this.by = by;
        this.from = from;
        this.to = to;
    }

    public CommandType getType() {
        return type;
    }

    public Integer getIndex() {
        return index;
    }

    public String getDescription() {
        return desc;
    }

    public String getBy() {
        return by;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public static Command exit() { return new Command(CommandType.EXIT, null, null, null, null, null); }
    public static Command list() { return new Command(CommandType.LIST, null, null, null, null, null); }

    public static Command mark(CommandType type, int index) {
        return new Command(type, index, null, null, null, null);
    }

    public static Command delete(int index) {
        return new Command(CommandType.DELETE, index, null, null, null, null);
    }

    public static Command todo(String desc) {
        return new Command(CommandType.TODO, null, desc, null, null, null);
    }

    public static Command deadline(String desc, String by) {
        return new Command(CommandType.DEADLINE, null, desc, by, null, null);
    }

    public static Command event(String desc, String from, String to) {
        return new Command(CommandType.EVENT, null, desc, null, from, to);
    }

    public static Command find(String keyword) {
        return new Command(CommandType.FIND, null, keyword, null, null, null);
    }
}
