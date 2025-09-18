package chungus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TodoTest {
    private Todo todo;

    @BeforeEach
    public void setUp() {
        todo = new Todo("Test todo task");
    }

    @Test
    public void constructor_createsTodoWithCorrectDescription() {
        assertEquals("Test todo task", todo.getDescription());
        assertEquals(" ", todo.getStatusIcon());
        assertEquals(Priority.MEDIUM, todo.getPriority());
    }

    @Test
    public void toString_includesTodoSymbol() {
        String result = todo.toString();
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[ ] [P:M] Test todo task"));
    }

    @Test
    public void toString_whenMarkedDone_includesDoneStatus() {
        todo.markAsDone();
        String result = todo.toString();
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[X] [P:M] Test todo task"));
    }

    @Test
    public void toString_withDifferentPriority_showsCorrectPriority() {
        todo.setPriority(Priority.HIGH);
        String result = todo.toString();
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[ ] [P:H] Test todo task"));
    }

    @Test
    public void toString_withLowPriority_showsCorrectPriority() {
        todo.setPriority(Priority.LOW);
        String result = todo.toString();
        assertTrue(result.contains("[T]"));
        assertTrue(result.contains("[ ] [P:L] Test todo task"));
    }

    @Test
    public void toString_combinesAllElements() {
        todo.markAsDone();
        todo.setPriority(Priority.HIGH);
        String result = todo.toString();
        assertEquals("[T] [X] [P:H] Test todo task", result);
    }

    @Test
    public void inheritsTaskFunctionality() {
        // Test that Todo inherits all Task functionality
        assertEquals("Test todo task", todo.getDescription());
        assertEquals(" ", todo.getStatusIcon());
        assertEquals(Priority.MEDIUM, todo.getPriority());

        todo.markAsDone();
        assertEquals("X", todo.getStatusIcon());

        todo.toggleStatus();
        assertEquals(" ", todo.getStatusIcon());

        todo.setPriority(Priority.LOW);
        assertEquals(Priority.LOW, todo.getPriority());
    }
}
