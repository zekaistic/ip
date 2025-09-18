package chungus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {
    private Task task;

    @BeforeEach
    public void setUp() {
        task = new Task("Test task");
    }

    @Test
    public void constructor_createsTaskWithCorrectInitialState() {
        assertEquals("Test task", task.getDescription());
        assertEquals(" ", task.getStatusIcon());
        assertEquals(Priority.MEDIUM, task.getPriority());
    }

    @Test
    public void markAsDone_changesStatusToDone() {
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    public void markAsNotDone_changesStatusToNotDone() {
        task.markAsDone(); // First mark as done
        task.markAsNotDone(); // Then mark as not done
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void toggleStatus_togglesBetweenDoneAndNotDone() {
        // Initially NOT_DONE
        assertEquals(" ", task.getStatusIcon());
        
        // Toggle to DONE
        task.toggleStatus();
        assertEquals("X", task.getStatusIcon());
        
        // Toggle back to NOT_DONE
        task.toggleStatus();
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void setPriority_withValidPriority_setsCorrectly() {
        task.setPriority(Priority.HIGH);
        assertEquals(Priority.HIGH, task.getPriority());
        
        task.setPriority(Priority.LOW);
        assertEquals(Priority.LOW, task.getPriority());
    }

    @Test
    public void setPriority_withNull_defaultsToMedium() {
        task.setPriority(null);
        assertEquals(Priority.MEDIUM, task.getPriority());
    }

    @Test
    public void toString_includesStatusIconAndPriority() {
        String result = task.toString();
        assertEquals("[ ] [P:M] Test task", result);
        
        task.markAsDone();
        task.setPriority(Priority.HIGH);
        result = task.toString();
        assertEquals("[X] [P:H] Test task", result);
    }

    @Test
    public void getDescription_returnsCorrectDescription() {
        assertEquals("Test task", task.getDescription());
    }

    @Test
    public void getPriority_returnsCurrentPriority() {
        assertEquals(Priority.MEDIUM, task.getPriority());
        
        task.setPriority(Priority.LOW);
        assertEquals(Priority.LOW, task.getPriority());
    }

    @Test
    public void getStatusIcon_returnsCorrectIcon() {
        assertEquals(" ", task.getStatusIcon());
        
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
    }
}
