package chungus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaskStatusTest {

    @Test
    public void getIcon_returnsCorrectIcons() {
        assertEquals("X", TaskStatus.DONE.getIcon());
        assertEquals(" ", TaskStatus.NOT_DONE.getIcon());
    }

    @Test
    public void toggle_fromDone_returnsNotDone() {
        assertEquals(TaskStatus.NOT_DONE, TaskStatus.DONE.toggle());
    }

    @Test
    public void toggle_fromNotDone_returnsDone() {
        assertEquals(TaskStatus.DONE, TaskStatus.NOT_DONE.toggle());
    }

    @Test
    public void toggle_multipleTimes_alternatesCorrectly() {
        TaskStatus status = TaskStatus.NOT_DONE;

        // First toggle: NOT_DONE -> DONE
        status = status.toggle();
        assertEquals(TaskStatus.DONE, status);

        // Second toggle: DONE -> NOT_DONE
        status = status.toggle();
        assertEquals(TaskStatus.NOT_DONE, status);

        // Third toggle: NOT_DONE -> DONE
        status = status.toggle();
        assertEquals(TaskStatus.DONE, status);

        // Fourth toggle: DONE -> NOT_DONE
        status = status.toggle();
        assertEquals(TaskStatus.NOT_DONE, status);
    }

    @Test
    public void toggle_doesNotModifyOriginal() {
        TaskStatus original = TaskStatus.DONE;
        TaskStatus toggled = original.toggle();

        // Original should remain unchanged
        assertEquals(TaskStatus.DONE, original);
        // Toggled should be different
        assertEquals(TaskStatus.NOT_DONE, toggled);
    }

    @Test
    public void toggle_isSymmetric() {
        // Toggling twice should return to original state
        TaskStatus original = TaskStatus.DONE;
        TaskStatus toggled = original.toggle().toggle();
        assertEquals(original, toggled);

        original = TaskStatus.NOT_DONE;
        toggled = original.toggle().toggle();
        assertEquals(original, toggled);
    }

    @Test
    public void enumValues_areCorrect() {
        // Test that enum values are as expected
        TaskStatus[] values = TaskStatus.values();
        assertEquals(2, values.length);
        assertEquals(TaskStatus.DONE, values[0]);
        assertEquals(TaskStatus.NOT_DONE, values[1]);
    }

    @Test
    public void valueOf_worksCorrectly() {
        assertEquals(TaskStatus.DONE, TaskStatus.valueOf("DONE"));
        assertEquals(TaskStatus.NOT_DONE, TaskStatus.valueOf("NOT_DONE"));
    }
}
