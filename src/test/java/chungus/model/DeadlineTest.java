package chungus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeadlineTest {
    private Deadline deadline;

    @BeforeEach
    public void setUp() {
        deadline = new Deadline("Test deadline", "2025-12-31");
    }

    @Test
    public void constructor_createsDeadlineWithCorrectDescription() {
        assertEquals("Test deadline", deadline.getDescription());
        assertEquals(" ", deadline.getStatusIcon());
        assertEquals(Priority.MEDIUM, deadline.getPriority());
    }

    @Test
    public void constructor_withIsoDate_parsesCorrectly() {
        Deadline isoDeadline = new Deadline("ISO deadline", "2025-12-31");
        assertEquals("2025-12-31", isoDeadline.getByIso());
    }

    @Test
    public void constructor_withSlashDate_parsesCorrectly() {
        Deadline slashDeadline = new Deadline("Slash deadline", "31/12/2025");
        assertEquals("2025-12-31", slashDeadline.getByIso());
    }

    @Test
    public void constructor_withDashDate_parsesCorrectly() {
        Deadline dashDeadline = new Deadline("Dash deadline", "31-12-2025");
        assertEquals("2025-12-31", dashDeadline.getByIso());
    }

    @Test
    public void constructor_withInvalidDate_returnsNullForIso() {
        Deadline invalidDeadline = new Deadline("Invalid deadline", "invalid date");
        assertNull(invalidDeadline.getByIso());
    }

    @Test
    public void getByIso_returnsCorrectIsoFormat() {
        assertEquals("2025-12-31", deadline.getByIso());
    }

    @Test
    public void getByIso_withInvalidDate_returnsNull() {
        Deadline invalidDeadline = new Deadline("Invalid", "not a date");
        assertNull(invalidDeadline.getByIso());
    }

    @Test
    public void toString_includesDeadlineSymbol() {
        String result = deadline.toString();
        assertTrue(result.contains("[D]"));
        assertTrue(result.contains("Test deadline"));
    }

    @Test
    public void toString_includesFormattedDate() {
        String result = deadline.toString();
        assertTrue(result.contains("(by: Dec 31 2025)"));
    }

    @Test
    public void toString_withInvalidDate_showsInvalidDateMessage() {
        Deadline invalidDeadline = new Deadline("Invalid", "not a date");
        String result = invalidDeadline.toString();
        assertTrue(result.contains("(by: Invalid date format)"));
    }

    @Test
    public void toString_whenMarkedDone_includesDoneStatus() {
        deadline.markAsDone();
        String result = deadline.toString();
        assertTrue(result.contains("[D]"));
        assertTrue(result.contains("[X] [P:M] Test deadline"));
        assertTrue(result.contains("(by: Dec 31 2025)"));
    }

    @Test
    public void toString_withDifferentPriority_showsCorrectPriority() {
        deadline.setPriority(Priority.HIGH);
        String result = deadline.toString();
        assertTrue(result.contains("[D]"));
        assertTrue(result.contains("[ ] [P:H] Test deadline"));
        assertTrue(result.contains("(by: Dec 31 2025)"));
    }

    @Test
    public void toString_combinesAllElements() {
        deadline.markAsDone();
        deadline.setPriority(Priority.LOW);
        String result = deadline.toString();
        assertEquals("[D] [X] [P:L] Test deadline (by: Dec 31 2025)", result);
    }

    @Test
    public void inheritsTaskFunctionality() {
        // Test that Deadline inherits all Task functionality
        assertEquals("Test deadline", deadline.getDescription());
        assertEquals(" ", deadline.getStatusIcon());
        assertEquals(Priority.MEDIUM, deadline.getPriority());
        
        deadline.markAsDone();
        assertEquals("X", deadline.getStatusIcon());
        
        deadline.toggleStatus();
        assertEquals(" ", deadline.getStatusIcon());
        
        deadline.setPriority(Priority.HIGH);
        assertEquals(Priority.HIGH, deadline.getPriority());
    }

    @Test
    public void dateParsing_variousFormats_worksCorrectly() {
        // Test different date formats
        String[] testDates = {
            "2025-01-01",    // ISO format
            "1/1/2025",      // Slash format
            "1-1-2025",      // Dash format
            "31/12/2025",    // Slash format with 2-digit day
            "31-12-2025"     // Dash format with 2-digit day
        };
        
        for (String date : testDates) {
            Deadline testDeadline = new Deadline("Test", date);
            assertTrue(testDeadline.getByIso() != null, 
                "Date format should be parsed: " + date);
        }
    }

    @Test
    public void dateParsing_invalidFormats_returnsNull() {
        String[] invalidDates = {
            "not a date",
            "2025/13/01",    // Invalid month
            "2025/01/32",    // Invalid day
            "25-12-31",      // Invalid year format
            "2025-13-01",    // Invalid month
            "2025-01-32"     // Invalid day
        };
        
        for (String date : invalidDates) {
            Deadline testDeadline = new Deadline("Test", date);
            assertNull(testDeadline.getByIso(), 
                "Invalid date should return null: " + date);
        }
    }
}
