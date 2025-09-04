package chungus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void parseDeadline_validInputs_parsedCorrectly() throws Exception {
        Parser parser = new Parser();

        String[] partsIso = parser.parseDeadline("deadline submit iP /by 2025-12-31");
        assertEquals("submit iP", partsIso[0]);
        assertEquals("2025-12-31", partsIso[1]);

        String[] partsSlashes = parser.parseDeadline("deadline submit iP /by 1/1/2025");
        assertEquals("submit iP", partsSlashes[0]);
        assertEquals("1/1/2025", partsSlashes[1]);
    }

    @Test
    public void parseDeadline_missingBy_throws() {
        Parser parser = new Parser();
        assertThrows(ChungusException.class, () -> parser.parseDeadline("deadline oops no by marker"));
    }

    @Test
    public void parseEvent_validInputs_parsedCorrectly() throws Exception {
        Parser parser = new Parser();
        String[] parts = parser.parseEvent("event project meeting /from 2025-01-01 /to 2025-01-02");
        assertEquals("project meeting", parts[0]);
        assertEquals("2025-01-01", parts[1]);
        assertEquals("2025-01-02", parts[2]);
    }

    @Test
    public void parseEvent_missingFromOrTo_throws() {
        Parser parser = new Parser();
        assertThrows(ChungusException.class, () -> parser.parseEvent("event party /from 2025-01-01"));
        assertThrows(ChungusException.class, () -> parser.parseEvent("event party /to 2025-01-01"));
    }
}
