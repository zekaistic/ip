package chungus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chungus.model.Deadline;
import chungus.model.Event;
import chungus.model.Priority;
import chungus.model.Task;
import chungus.model.Todo;
import chungus.storage.Storage;

public class StorageTest {
    private Path tempDir;
    private Path tempFile;

    @BeforeEach
    public void setup() throws Exception {
        tempDir = Files.createTempDirectory("chungus-test-");
        tempFile = tempDir.resolve("tasks.txt");
    }

    @AfterEach
    public void cleanup() throws Exception {
        if (Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
        if (Files.exists(tempDir)) {
            Files.delete(tempDir);
        }
    }

    @Test
    public void saveAndLoad_roundTrip_preservesTasks() throws Exception {
        Storage storage = new Storage(tempFile.toString());

        ArrayList<Task> toSave = new ArrayList<>();
        Task t1 = new Todo("read book");
        Task t2 = new Deadline("return book", "2025-12-31");
        Task t3 = new Event("conference", "2025-01-01", "2025-01-03");
        t1.markAsDone();
        t2.setPriority(Priority.HIGH);
        t3.setPriority(Priority.LOW);
        toSave.add(t1);
        toSave.add(t2);
        toSave.add(t3);

        storage.save(toSave);

        assertTrue(new File(tempFile.toString()).exists());

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());

        // Item 1: Todo, done, default MEDIUM priority
        assertTrue(loaded.get(0) instanceof Todo);
        assertEquals(t1.getStatusIcon(), loaded.get(0).getStatusIcon());
        assertEquals("read book", loaded.get(0).getDescription());
        assertEquals(Priority.MEDIUM, loaded.get(0).getPriority());

        // Item 2: Deadline, HIGH priority
        assertTrue(loaded.get(1) instanceof Deadline);
        assertEquals("return book", loaded.get(1).getDescription());
        assertEquals(Priority.HIGH, loaded.get(1).getPriority());

        // Item 3: Event, LOW priority
        assertTrue(loaded.get(2) instanceof Event);
        assertEquals("conference", loaded.get(2).getDescription());
        assertEquals(Priority.LOW, loaded.get(2).getPriority());
    }
}
