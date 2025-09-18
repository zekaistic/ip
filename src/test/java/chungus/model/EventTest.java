package chungus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTest {
    private Event event;

    @BeforeEach
    public void setUp() {
        event = new Event("Test event", "2025-01-01", "2025-01-02");
    }

    @Test
    public void constructor_createsEventWithCorrectDescription() {
        assertEquals("Test event", event.getDescription());
        assertEquals(" ", event.getStatusIcon());
        assertEquals(Priority.MEDIUM, event.getPriority());
    }

    @Test
    public void constructor_withIsoDates_parsesCorrectly() {
        Event isoEvent = new Event("ISO event", "2025-01-01", "2025-01-02");
        assertEquals("2025-01-01", isoEvent.getFromIso());
        assertEquals("2025-01-02", isoEvent.getToIso());
    }

    @Test
    public void constructor_withSlashDates_parsesCorrectly() {
        Event slashEvent = new Event("Slash event", "1/1/2025", "2/1/2025");
        assertEquals("2025-01-01", slashEvent.getFromIso());
        assertEquals("2025-01-02", slashEvent.getToIso());
    }

    @Test
    public void constructor_withDashDates_parsesCorrectly() {
        Event dashEvent = new Event("Dash event", "1-1-2025", "2-1-2025");
        assertEquals("2025-01-01", dashEvent.getFromIso());
        assertEquals("2025-01-02", dashEvent.getToIso());
    }

    @Test
    public void constructor_withInvalidDates_returnsRawInput() {
        Event invalidEvent = new Event("Invalid event", "invalid from", "invalid to");
        assertEquals("invalid from", invalidEvent.getFromIso());
        assertEquals("invalid to", invalidEvent.getToIso());
    }

    @Test
    public void getFrom_returnsRawFromValue() {
        assertEquals("2025-01-01", event.getFrom());
    }

    @Test
    public void getTo_returnsRawToValue() {
        assertEquals("2025-01-02", event.getTo());
    }

    @Test
    public void getFromIso_returnsIsoFormat() {
        assertEquals("2025-01-01", event.getFromIso());
    }

    @Test
    public void getToIso_returnsIsoFormat() {
        assertEquals("2025-01-02", event.getToIso());
    }

    @Test
    public void getFromIso_withInvalidDate_returnsRawInput() {
        Event invalidEvent = new Event("Invalid", "not a date", "2025-01-01");
        assertEquals("not a date", invalidEvent.getFromIso());
    }

    @Test
    public void getToIso_withInvalidDate_returnsRawInput() {
        Event invalidEvent = new Event("Invalid", "2025-01-01", "not a date");
        assertEquals("not a date", invalidEvent.getToIso());
    }

    @Test
    public void toString_includesEventSymbol() {
        String result = event.toString();
        assertTrue(result.contains("[E]"));
        assertTrue(result.contains("Test event"));
    }

    @Test
    public void toString_includesFormattedDates() {
        String result = event.toString();
        assertTrue(result.contains("(from: Jan 01 2025 to: Jan 02 2025)"));
    }

    @Test
    public void toString_withInvalidDates_showsRawInput() {
        Event invalidEvent = new Event("Invalid", "not a date", "also not a date");
        String result = invalidEvent.toString();
        assertTrue(result.contains("(from: not a date to: also not a date)"));
    }

    @Test
    public void toString_whenMarkedDone_includesDoneStatus() {
        event.markAsDone();
        String result = event.toString();
        assertTrue(result.contains("[E]"));
        assertTrue(result.contains("[X] [P:M] Test event"));
        assertTrue(result.contains("(from: Jan 01 2025 to: Jan 02 2025)"));
    }

    @Test
    public void toString_withDifferentPriority_showsCorrectPriority() {
        event.setPriority(Priority.HIGH);
        String result = event.toString();
        assertTrue(result.contains("[E]"));
        assertTrue(result.contains("[ ] [P:H] Test event"));
        assertTrue(result.contains("(from: Jan 01 2025 to: Jan 02 2025)"));
    }

    @Test
    public void toString_combinesAllElements() {
        event.markAsDone();
        event.setPriority(Priority.LOW);
        String result = event.toString();
        assertEquals("[E] [X] [P:L] Test event (from: Jan 01 2025 to: Jan 02 2025)", result);
    }

    @Test
    public void inheritsTaskFunctionality() {
        // Test that Event inherits all Task functionality
        assertEquals("Test event", event.getDescription());
        assertEquals(" ", event.getStatusIcon());
        assertEquals(Priority.MEDIUM, event.getPriority());

        event.markAsDone();
        assertEquals("X", event.getStatusIcon());

        event.toggleStatus();
        assertEquals(" ", event.getStatusIcon());

        event.setPriority(Priority.HIGH);
        assertEquals(Priority.HIGH, event.getPriority());
    }

    @Test
    public void dateParsing_variousFormats_worksCorrectly() {
        // Test different date formats
        String[][] testDatePairs = {
            {"2025-01-01", "2025-01-02"}, // ISO format
            {"1/1/2025", "2/1/2025"}, // Slash format
            {"1-1-2025", "2-1-2025"}, // Dash format
            {"31/12/2025", "1/1/2026"}, // Slash format with 2-digit day
            {"31-12-2025", "1-1-2026"} // Dash format with 2-digit day
        };

        for (String[] dates : testDatePairs) {
            Event testEvent = new Event("Test", dates[0], dates[1]);
            assertTrue(testEvent.getFromIso() != null,
                "From date format should be parsed: " + dates[0]);
            assertTrue(testEvent.getToIso() != null,
                "To date format should be parsed: " + dates[1]);
        }
    }

    @Test
    public void dateParsing_invalidFormats_returnsRawInput() {
        String[] invalidDates = {
            "not a date",
            "2025/13/01", // Invalid month
            "2025/01/32", // Invalid day
            "25-12-31", // Invalid year format
            "2025-13-01", // Invalid month
            "2025-01-32" // Invalid day
        };

        for (String date : invalidDates) {
            Event testEvent = new Event("Test", date, "2025-01-01");
            assertEquals(date, testEvent.getFromIso(),
                "Invalid from date should return raw input: " + date);
        }
    }

    @Test
    public void mixedDateFormats_handlesCorrectly() {
        // One valid date, one invalid
        Event mixedEvent = new Event("Mixed", "2025-01-01", "invalid date");
        assertEquals("2025-01-01", mixedEvent.getFromIso());
        assertEquals("invalid date", mixedEvent.getToIso());

        // Both invalid
        Event bothInvalidEvent = new Event("Both invalid", "not a date", "also not a date");
        assertEquals("not a date", bothInvalidEvent.getFromIso());
        assertEquals("also not a date", bothInvalidEvent.getToIso());
    }
}
