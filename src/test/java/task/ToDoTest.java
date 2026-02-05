package task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ToDoTest {

    @Test
    public void toFileString_notDone_correctFormat() {
        ToDo todo = new ToDo("read book");

        assertEquals("T | 0 | read book", todo.toFileString());
    }

    @Test
    public void toFileString_done_correctFormat() {
        ToDo todo = new ToDo("read book", true);

        assertEquals("T | 1 | read book", todo.toFileString());
    }

    @Test
    public void toString_notDone_correctFormat() {
        ToDo todo = new ToDo("read book");

        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void toString_done_correctFormat() {
        ToDo todo = new ToDo("read book", true);

        assertEquals("[T][X] read book", todo.toString());
    }
}