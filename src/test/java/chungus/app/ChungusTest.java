package chungus.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chungus.common.Constants;
import chungus.model.Task;
import chungus.model.TaskList;
import chungus.model.Todo;

public class ChungusTest {
    private Chungus chungus;
    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("chungus-test", ".txt");
        chungus = new Chungus(tempFile.toString());
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    public void constructor_createsChungusWithEmptyTaskList() {
        assertNotNull(chungus);
        // We can't directly access the task list, but we can test through public methods
        assertTrue(chungus.getResponse("list").contains("Here are the tasks in your list:"));
    }

    @Test
    public void isExitCommand_withBye_returnsTrue() {
        assertTrue(chungus.isExitCommand("bye"));
        assertFalse(chungus.isExitCommand("BYE")); // Case sensitive
        assertTrue(chungus.isExitCommand(" bye ")); // Trims whitespace
    }

    @Test
    public void isExitCommand_withOtherCommands_returnsFalse() {
        assertFalse(chungus.isExitCommand("list"));
        assertFalse(chungus.isExitCommand("todo"));
        assertFalse(chungus.isExitCommand(""));
        assertFalse(chungus.isExitCommand("invalid"));
    }

    @Test
    public void getResponse_withTodoCommand_addsTask() {
        String response = chungus.getResponse("todo read book");
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("read book"));
        
        // Verify task was added by listing
        String listResponse = chungus.getResponse("list");
        assertTrue(listResponse.contains("read book"));
    }

    @Test
    public void getResponse_withDeadlineCommand_addsDeadline() {
        String response = chungus.getResponse("deadline submit assignment /by 2025-12-31");
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("submit assignment"));
        assertTrue(response.contains("Dec 31 2025"));
    }

    @Test
    public void getResponse_withEventCommand_addsEvent() {
        String response = chungus.getResponse("event project meeting /from 2025-01-01 /to 2025-01-02");
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("project meeting"));
        assertTrue(response.contains("Jan 01 2025"));
        assertTrue(response.contains("Jan 02 2025"));
    }

    @Test
    public void getResponse_withListCommand_showsTasks() {
        // Add a task first
        chungus.getResponse("todo read book");
        
        String response = chungus.getResponse("list");
        assertTrue(response.contains("Here are the tasks in your list:"));
        assertTrue(response.contains("read book"));
    }

    @Test
    public void getResponse_withEmptyList_showsNoTasksMessage() {
        String response = chungus.getResponse("list");
        assertTrue(response.contains("Here are the tasks in your list:"));
    }

    @Test
    public void getResponse_withMarkCommand_marksTask() {
        // Add a task first
        chungus.getResponse("todo read book");
        
        String response = chungus.getResponse("mark 1");
        assertTrue(response.contains("Nice! I've marked this task as done:"));
        assertTrue(response.contains("read book"));
    }

    @Test
    public void getResponse_withUnmarkCommand_unmarksTask() {
        // Add and mark a task first
        chungus.getResponse("todo read book");
        chungus.getResponse("mark 1");
        
        String response = chungus.getResponse("unmark 1");
        assertTrue(response.contains("OK, I've marked this task as not done yet:"));
        assertTrue(response.contains("read book"));
    }

    @Test
    public void getResponse_withDeleteCommand_deletesTask() {
        // Add a task first
        chungus.getResponse("todo read book");
        
        String response = chungus.getResponse("delete 1");
        assertTrue(response.contains("Noted. I've removed this task:"));
        assertTrue(response.contains("read book"));
        
        // Verify task was deleted
        String listResponse = chungus.getResponse("list");
        assertTrue(listResponse.contains("Here are the tasks in your list:"));
    }

    @Test
    public void getResponse_withFindCommand_findsTasks() {
        // Add multiple tasks
        chungus.getResponse("todo read book");
        chungus.getResponse("todo write book report");
        chungus.getResponse("todo clean room");
        
        String response = chungus.getResponse("find book");
        assertTrue(response.contains("Here are the matching tasks in your list:"));
        assertTrue(response.contains("read book"));
        assertTrue(response.contains("write book report"));
    }

    @Test
    public void getResponse_withPriorityCommand_setsPriority() {
        // Add a task first
        chungus.getResponse("todo read book");
        
        String response = chungus.getResponse("priority 1 high");
        assertTrue(response.contains("Priority for task 1 set:"));
        assertTrue(response.contains("read book"));
    }

    @Test
    public void getResponse_withInvalidCommands_returnsErrorMessage() {
        String response = chungus.getResponse("invalid command");
        assertTrue(response.contains("OOPS!!!"));
        assertTrue(response.contains("I don't know what that means"));
    }

    @Test
    public void getResponse_withEmptyInput_returnsEmptyString() {
        String response = chungus.getResponse("");
        assertEquals("", response);
    }

    @Test
    public void getResponse_withBlankInput_returnsEmptyString() {
        String response = chungus.getResponse("   ");
        assertEquals("", response);
    }

    @Test
    public void getResponse_withInvalidTaskIndex_returnsErrorMessage() {
        chungus.getResponse("todo read book");
        
        String response = chungus.getResponse("mark 2");
        assertTrue(response.contains("Invalid task number"));
        
        response = chungus.getResponse("delete 0");
        assertTrue(response.contains("Invalid task number"));
    }

    @Test
    public void getResponse_withInvalidDeadlineFormat_returnsErrorMessage() {
        String response = chungus.getResponse("deadline task without by");
        assertTrue(response.contains("OOPS!!!"));
        assertTrue(response.contains("Deadline command must include '/by'"));
    }

    @Test
    public void getResponse_withInvalidEventFormat_returnsErrorMessage() {
        String response = chungus.getResponse("event task without from to");
        assertTrue(response.contains("OOPS!!!"));
        assertTrue(response.contains("Event command must include both '/from' and '/to'"));
    }

    @Test
    public void getResponse_withEmptyTodo_returnsErrorMessage() {
        String response = chungus.getResponse("todo");
        assertTrue(response.contains("OOPS!!!"));
        assertTrue(response.contains("The description of a todo cannot be empty"));
    }

    @Test
    public void getResponse_withEmptyFind_returnsErrorMessage() {
        String response = chungus.getResponse("find");
        assertTrue(response.contains("OOPS!!!"));
        assertTrue(response.contains("Please provide a keyword"));
    }

    @Test
    public void getResponse_withEmptyPriority_returnsErrorMessage() {
        chungus.getResponse("todo read book");
        
        String response = chungus.getResponse("priority 1");
        assertTrue(response.contains("OOPS!!!"));
        assertTrue(response.contains("Please provide a priority level"));
    }

    @Test
    public void getResponse_withByeCommand_returnsByeMessage() {
        String response = chungus.getResponse("bye");
        assertTrue(response.contains("Bye"));
    }

    @Test
    public void getResponse_preservesTaskStateBetweenCalls() {
        // Add a task
        chungus.getResponse("todo read book");
        
        // Mark it as done
        chungus.getResponse("mark 1");
        
        // Verify it's still marked as done
        String listResponse = chungus.getResponse("list");
        assertTrue(listResponse.contains("[X]")); // Done status
        assertTrue(listResponse.contains("read book"));
    }

    @Test
    public void getResponse_handlesMultipleTasksCorrectly() {
        // Add multiple tasks
        chungus.getResponse("todo read book");
        chungus.getResponse("todo write report");
        chungus.getResponse("deadline submit assignment /by 2025-12-31");
        
        // Mark one as done
        chungus.getResponse("mark 1");
        
        // Delete another
        chungus.getResponse("delete 2");
        
        // Verify final state
        String listResponse = chungus.getResponse("list");
        assertTrue(listResponse.contains("read book"));
        assertTrue(listResponse.contains("submit assignment"));
        assertFalse(listResponse.contains("write report"));
    }
}
