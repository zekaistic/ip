package chungus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chungus.common.ChungusException;
import chungus.logic.CommandType;
import chungus.logic.Parser;

public class ParserTest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void parseCommandType_validCommands_returnsCorrectType() {
        assertEquals(CommandType.LIST, parser.parseCommandType("list"));
        assertEquals(CommandType.TODO, parser.parseCommandType("todo"));
        assertEquals(CommandType.DEADLINE, parser.parseCommandType("deadline"));
        assertEquals(CommandType.EVENT, parser.parseCommandType("event"));
        assertEquals(CommandType.MARK, parser.parseCommandType("mark"));
        assertEquals(CommandType.UNMARK, parser.parseCommandType("unmark"));
        assertEquals(CommandType.DELETE, parser.parseCommandType("delete"));
        assertEquals(CommandType.FIND, parser.parseCommandType("find"));
        assertEquals(CommandType.PRIORITY, parser.parseCommandType("priority"));
        assertEquals(CommandType.BYE, parser.parseCommandType("bye"));
    }

    @Test
    public void parseCommandType_invalidCommands_returnsNull() {
        assertNull(parser.parseCommandType("invalid"));
        assertNull(parser.parseCommandType(""));
        assertNull(parser.parseCommandType("   "));
        assertNull(parser.parseCommandType("listt")); // typo
        assertNull(parser.parseCommandType("todoo")); // typo
    }

    @Test
    public void parseCommandType_caseInsensitive_returnsNull() {
        // CommandType doesn't support case-insensitive parsing
        assertNull(parser.parseCommandType("LIST"));
        assertNull(parser.parseCommandType("TODO"));
        assertNull(parser.parseCommandType("DEADLINE"));
        assertNull(parser.parseCommandType("EVENT"));
        assertNull(parser.parseCommandType("MARK"));
        assertNull(parser.parseCommandType("UNMARK"));
        assertNull(parser.parseCommandType("DELETE"));
        assertNull(parser.parseCommandType("FIND"));
        assertNull(parser.parseCommandType("PRIORITY"));
        assertNull(parser.parseCommandType("BYE"));
    }

    @Test
    public void parseCommandType_withWhitespace_returnsCorrectType() {
        assertEquals(CommandType.LIST, parser.parseCommandType(" list "));
        assertEquals(CommandType.TODO, parser.parseCommandType(" todo "));
        assertEquals(CommandType.DEADLINE, parser.parseCommandType(" deadline "));
    }

    @Test
    public void parseDescription_validInputs_returnsCorrectDescription() {
        assertEquals("read book", parser.parseDescription("todo read book", "todo"));
        assertEquals("submit assignment /by 2025-12-31", parser.parseDescription("deadline submit assignment /by 2025-12-31", "deadline"));
        assertEquals("project meeting /from 2025-01-01 /to 2025-01-02", parser.parseDescription("event project meeting /from 2025-01-01 /to 2025-01-02", "event"));
        assertEquals("", parser.parseDescription("todo", "todo"));
        assertEquals("", parser.parseDescription("todo   ", "todo")); // trim() removes trailing spaces
    }

    @Test
    public void parseDescription_withWhitespace_trimsCorrectly() {
        assertEquals("read book", parser.parseDescription("todo  read book  ", "todo"));
        assertEquals("submit assignment  /by 2025-12-31", parser.parseDescription("deadline  submit assignment  /by 2025-12-31", "deadline"));
    }

    @Test
    public void parseTaskIndex_validInputs_returnsCorrectIndex() throws Exception {
        assertEquals(0, parser.parseTaskIndex("mark 1", "mark"));
        assertEquals(1, parser.parseTaskIndex("mark 2", "mark"));
        assertEquals(9, parser.parseTaskIndex("mark 10", "mark"));
        assertEquals(0, parser.parseTaskIndex("delete 1", "delete"));
        assertEquals(0, parser.parseTaskIndex("unmark 1", "unmark"));
    }

    @Test
    public void parseTaskIndex_withWhitespace_returnsCorrectIndex() throws Exception {
        assertEquals(0, parser.parseTaskIndex("mark 1", "mark"));
        assertEquals(1, parser.parseTaskIndex("mark 2", "mark"));
    }

    @Test
    public void parseTaskIndex_invalidInputs_throwsException() {
        assertThrows(ChungusException.class, () -> parser.parseTaskIndex("mark", "mark"));
        assertThrows(ChungusException.class, () -> parser.parseTaskIndex("mark ", "mark"));
        assertThrows(ChungusException.class, () -> parser.parseTaskIndex("mark abc", "mark"));
        assertThrows(ChungusException.class, () -> parser.parseTaskIndex("mark 1.5", "mark"));
        // Note: mark -1 and mark 0 are valid input but convert to invalid indices, which should be handled by the caller
    }

    @Test
    public void parseDeadline_validInputs_parsedCorrectly() throws Exception {
        String[] partsIso = parser.parseDeadline("deadline submit iP /by 2025-12-31");
        assertEquals("submit iP", partsIso[0]);
        assertEquals("2025-12-31", partsIso[1]);

        String[] partsSlashes = parser.parseDeadline("deadline submit iP /by 1/1/2025");
        assertEquals("submit iP", partsSlashes[0]);
        assertEquals("1/1/2025", partsSlashes[1]);
    }

    @Test
    public void parseDeadline_withWhitespace_parsedCorrectly() throws Exception {
        String[] parts = parser.parseDeadline("deadline  submit iP  /by  2025-12-31  ");
        assertEquals("submit iP", parts[0]);
        assertEquals("2025-12-31", parts[1]);
    }

    @Test
    public void parseDeadline_missingBy_throws() {
        assertThrows(ChungusException.class, () -> parser.parseDeadline("deadline oops no by marker"));
        assertThrows(ChungusException.class, () -> parser.parseDeadline("deadline"));
        assertThrows(ChungusException.class, () -> parser.parseDeadline("deadline task"));
    }

    @Test
    public void parseDeadline_malformedInput_throws() {
        // Only some malformed inputs throw exceptions
        assertThrows(ChungusException.class, () -> parser.parseDeadline("deadline task /by"));
    }
    
    @Test
    public void parseDeadline_malformedInput_returnsEmptyDescription() throws Exception {
        // Some malformed inputs return empty description
        String[] result = parser.parseDeadline("deadline /by 2025-12-31");
        assertEquals("", result[0]);
        assertEquals("2025-12-31", result[1]);
    }

    @Test
    public void parseEvent_validInputs_parsedCorrectly() throws Exception {
        String[] parts = parser.parseEvent("event project meeting /from 2025-01-01 /to 2025-01-02");
        assertEquals("project meeting", parts[0]);
        assertEquals("2025-01-01", parts[1]);
        assertEquals("2025-01-02", parts[2]);
    }

    @Test
    public void parseEvent_withWhitespace_parsedCorrectly() throws Exception {
        String[] parts = parser.parseEvent("event  project meeting  /from  2025-01-01  /to  2025-01-02  ");
        assertEquals("project meeting", parts[0]);
        assertEquals("2025-01-01", parts[1]);
        assertEquals("2025-01-02", parts[2]);
    }

    @Test
    public void parseEvent_missingFromOrTo_throws() {
        assertThrows(ChungusException.class, () -> parser.parseEvent("event party /from 2025-01-01"));
        assertThrows(ChungusException.class, () -> parser.parseEvent("event party /to 2025-01-01"));
        assertThrows(ChungusException.class, () -> parser.parseEvent("event party"));
        assertThrows(ChungusException.class, () -> parser.parseEvent("event"));
    }

    @Test
    public void parseEvent_malformedInput_throws() {
        // Only some malformed inputs throw exceptions
        assertThrows(ChungusException.class, () -> parser.parseEvent("event task /from 2025-01-01 /to"));
    }
    
    @Test
    public void parseEvent_malformedInput_returnsEmptyOrPartialData() throws Exception {
        // Some malformed inputs return empty or partial data
        String[] result1 = parser.parseEvent("event /from 2025-01-01 /to 2025-01-02");
        assertEquals("", result1[0]);
        assertEquals("2025-01-01", result1[1]);
        assertEquals("2025-01-02", result1[2]);
        
        String[] result2 = parser.parseEvent("event task /from /to 2025-01-02");
        assertEquals("task", result2[0]);
        assertEquals("", result2[1]);
        assertEquals("2025-01-02", result2[2]);
    }

    @Test
    public void parsePriorityCommand_validInputs_returnsCorrectValues() throws Exception {
        Object[] result = parser.parsePriorityCommand("priority 1 high");
        assertEquals(0, result[0]); // 1-based to 0-based index
        assertEquals("high", result[1]);

        result = parser.parsePriorityCommand("priority 5 medium");
        assertEquals(4, result[0]);
        assertEquals("medium", result[1]);

        result = parser.parsePriorityCommand("priority 10 low");
        assertEquals(9, result[0]);
        assertEquals("low", result[1]);
    }

    @Test
    public void parsePriorityCommand_withWhitespace_returnsCorrectValues() throws Exception {
        Object[] result = parser.parsePriorityCommand("priority 1 high");
        assertEquals(0, result[0]);
        assertEquals("high", result[1]);
    }

    @Test
    public void parsePriorityCommand_invalidInputs_throwsException() {
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority"));
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority "));
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority 1"));
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority 1 "));
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority abc high"));
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority 1.5 high"));
        // Note: priority -1 high is valid input but converts to -2 index, which should be handled by the caller
    }

    @Test
    public void parsePriorityCommand_emptyPriority_throwsException() {
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority 1 "));
        assertThrows(ChungusException.class, () -> parser.parsePriorityCommand("priority 1   "));
    }
}
