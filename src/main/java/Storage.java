import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private static final String DATA_DIR = "data";
    private static final String FILE_NAME = "chungus.txt";
    private static final String FILE_PATH = DATA_DIR + "/" + FILE_NAME;

    public static ArrayList<Task> loadTasks() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        
        // Create data directory if it doesn't exist
        createDataDirectoryIfNeeded();
        
        // Check if file exists, if not return empty list
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return tasks;
        }
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = parseTaskFromLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File not found: " + FILE_PATH, e);
        }
        
        return tasks;
    }
    
    public static void saveTasks(ArrayList<Task> tasks) throws IOException {
        // Create data directory if it doesn't exist
        createDataDirectoryIfNeeded();
        
        FileWriter fw = new FileWriter(FILE_PATH);
        try {
            for (Task task : tasks) {
                String line = convertTaskToLine(task);
                fw.write(line + System.lineSeparator());
            }
        } finally {
            fw.close();
        }
    }
    
    private static void createDataDirectoryIfNeeded() throws IOException {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            if (!dataDir.mkdir()) {
                throw new IOException("Could not create directory: " + DATA_DIR);
            }
        }
    }
    
    private static String convertTaskToLine(Task task) {
        StringBuilder line = new StringBuilder();
        
        if (task instanceof Todo) {
            line.append("T | ");
        } else if (task instanceof Deadline) {
            line.append("D | ");
        } else if (task instanceof Event) {
            line.append("E | ");
        }
        
        line.append(task.getStatusIcon().equals("X") ? "1" : "0");
        line.append(" | ");
        line.append(task.getDescription());
        
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            line.append(" | ").append(deadline.getByIso());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            line.append(" | ").append(event.getFromIso()).append(" | ").append(event.getToIso());
        }
        
        return line.toString();
    }
    
    private static Task parseTaskFromLine(String line) {
        try {
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                return null;
            }
            
            String taskType = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();
            
            Task task = null;
            
            switch (taskType) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length >= 4) {
                        String by = parts[3].trim();
                        task = new Deadline(description, by);
                    }
                    break;
                case "E":
                    if (parts.length >= 5) {
                        String from = parts[3].trim();
                        String to = parts[4].trim();
                        task = new Event(description, from, to);
                    }
                    break;
            }
            
            if (task != null && isDone) {
                task.markAsDone();
            }
            
            return task;
        } catch (Exception e) {
            return null;
        }
    }
}
