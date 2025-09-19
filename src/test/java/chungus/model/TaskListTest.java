package chungus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chungus.common.ChungusException;

public class TaskListTest {
    private TaskList taskList;
    private Task todo1;
    private Task todo2;
    private Task deadline1;

    @BeforeEach
    public void setUp() throws ChungusException {
        taskList = new TaskList();
        todo1 = new Todo("Read book");
        todo2 = new Todo("Write report");
        deadline1 = new Deadline("Submit assignment", "2025-12-31");
    }

    @Test
    public void constructor_createsEmptyTaskList() {
        TaskList emptyList = new TaskList();
        assertEquals(0, emptyList.size());
    }

    @Test
    public void constructor_withNullList_createsEmptyTaskList() {
        TaskList list = new TaskList(null);
        assertEquals(0, list.size());
    }

    @Test
    public void constructor_withExistingList_createsTaskListWithTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(todo1);
        tasks.add(todo2);

        TaskList list = new TaskList(tasks);
        assertEquals(2, list.size());
        assertEquals(todo1, list.get(0));
        assertEquals(todo2, list.get(1));
    }

    @Test
    public void add_addsTaskToList() {
        taskList.add(todo1);
        assertEquals(1, taskList.size());
        assertEquals(todo1, taskList.get(0));
    }

    @Test
    public void add_multipleTasks_addsAllTasks() {
        taskList.add(todo1);
        taskList.add(todo2);
        taskList.add(deadline1);

        assertEquals(3, taskList.size());
        assertEquals(todo1, taskList.get(0));
        assertEquals(todo2, taskList.get(1));
        assertEquals(deadline1, taskList.get(2));
    }

    @Test
    public void get_validIndex_returnsCorrectTask() {
        taskList.add(todo1);
        taskList.add(todo2);

        assertEquals(todo1, taskList.get(0));
        assertEquals(todo2, taskList.get(1));
    }

    @Test
    public void get_invalidIndex_throwsException() {
        taskList.add(todo1);

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(1));
    }

    @Test
    public void remove_validIndex_removesAndReturnsTask() {
        taskList.add(todo1);
        taskList.add(todo2);

        Task removed = taskList.remove(0);
        assertEquals(todo1, removed);
        assertEquals(1, taskList.size());
        assertEquals(todo2, taskList.get(0));
    }

    @Test
    public void remove_invalidIndex_throwsException() {
        taskList.add(todo1);

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(1));
    }

    @Test
    public void size_returnsCorrectSize() {
        assertEquals(0, taskList.size());

        taskList.add(todo1);
        assertEquals(1, taskList.size());

        taskList.add(todo2);
        assertEquals(2, taskList.size());
    }

    @Test
    public void asArrayList_returnsBackingList() {
        ArrayList<Task> tasks = taskList.asArrayList();
        assertNotNull(tasks);
        assertEquals(0, tasks.size());

        taskList.add(todo1);
        assertEquals(1, tasks.size());
        assertEquals(todo1, tasks.get(0));
    }

    @Test
    public void findByKeyword_validKeyword_returnsMatchingTasks() {
        taskList.add(todo1);
        taskList.add(todo2);
        taskList.add(deadline1);

        ArrayList<Task> matches = taskList.findByKeyword("book");
        assertEquals(1, matches.size());
        assertEquals(todo1, matches.get(0));

        matches = taskList.findByKeyword("report");
        assertEquals(1, matches.size());
        assertEquals(todo2, matches.get(0));
    }

    @Test
    public void findByKeyword_caseInsensitive_returnsMatchingTasks() {
        taskList.add(todo1);
        taskList.add(todo2);

        ArrayList<Task> matches = taskList.findByKeyword("BOOK");
        assertEquals(1, matches.size());
        assertEquals(todo1, matches.get(0));

        matches = taskList.findByKeyword("Report");
        assertEquals(1, matches.size());
        assertEquals(todo2, matches.get(0));
    }

    @Test
    public void findByKeyword_partialMatch_returnsMatchingTasks() {
        taskList.add(todo1);
        taskList.add(todo2);

        ArrayList<Task> matches = taskList.findByKeyword("ea");
        assertEquals(1, matches.size());
        assertEquals(todo1, matches.get(0));
    }

    @Test
    public void findByKeyword_noMatches_returnsEmptyList() {
        taskList.add(todo1);
        taskList.add(todo2);

        ArrayList<Task> matches = taskList.findByKeyword("nonexistent");
        assertTrue(matches.isEmpty());
    }

    @Test
    public void findByKeyword_nullKeyword_returnsEmptyList() {
        taskList.add(todo1);

        ArrayList<Task> matches = taskList.findByKeyword(null);
        assertTrue(matches.isEmpty());
    }

    @Test
    public void findByKeyword_emptyKeyword_returnsEmptyList() {
        taskList.add(todo1);

        ArrayList<Task> matches = taskList.findByKeyword("");
        assertTrue(matches.isEmpty());

        matches = taskList.findByKeyword("   ");
        assertTrue(matches.isEmpty());
    }

    @Test
    public void findByKeyword_multipleMatches_returnsAllMatchingTasks() {
        Task task1 = new Todo("Read book about Java");
        Task task2 = new Todo("Write book report");
        Task task3 = new Todo("Clean room");

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        ArrayList<Task> matches = taskList.findByKeyword("book");
        assertEquals(2, matches.size());
        assertTrue(matches.contains(task1));
        assertTrue(matches.contains(task2));
    }
}
