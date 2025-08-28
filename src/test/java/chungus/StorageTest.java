package chungus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
        toSave.add(t1);
        toSave.add(t2);
        toSave.add(t3);

        storage.save(toSave);

        assertTrue(new File(tempFile.toString()).exists());

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());

        assertEquals("[X] [T] [X] read book", String.format("[%s] %s", t1.getStatusIcon(), t1.toString()));
        assertEquals(t1.getStatusIcon(), loaded.get(0).getStatusIcon());
        assertEquals("read book", loaded.get(0).getDescription());

        assertEquals("return book", loaded.get(1).getDescription());
        assertTrue(loaded.get(1) instanceof Deadline);

        assertTrue(loaded.get(2) instanceof Event);
        assertEquals("conference", loaded.get(2).getDescription());
    }
}


