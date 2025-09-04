package chungus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Task with a due date.
 */
public class Deadline extends Task {
    private LocalDate dueDate;

    /**
     * Creates a deadline task.
     *
     * @param description description text
     * @param rawDeadline date string in yyyy-MM-dd or d/M/yyyy
     */
    public Deadline(String description, String rawDeadline) {
        super(description);
        this.dueDate = parseDate(rawDeadline);
    }

    private LocalDate parseDate(String input) {
        String trimmed = input.trim();
        DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("d/M/yyyy")
        };
        for (DateTimeFormatter f : formatters) {
            try {
                return LocalDate.parse(trimmed, f);
            } catch (DateTimeParseException ignored) {
                // try next
            }
        }
        return dueDate;
    }

    /**
     * Returns the due date in ISO-8601 (yyyy-MM-dd) format.
     *
     * @return ISO date
     */
    public String getByIso() {
        return this.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public String toString() {
        String formatted = this.dueDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        return String.format("[%s] %s (by: %s)", TaskType.DEADLINE.getSymbol(), super.toString(), formatted);
    }
}
