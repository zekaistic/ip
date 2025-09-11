package chungus.model;

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
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy")
        };
        for (DateTimeFormatter f : formatters) {
            try {
                return LocalDate.parse(trimmed, f);
            } catch (DateTimeParseException ignored) {
                // try next
            }
        }
        // If no format matches, return null instead of dueDate (which is still null)
        return null;
    }

    /**
     * Returns the due date in yyyy-MM-dd format.
     *
     * @return ISO date or null if date parsing fails
     */
    public String getByIso() {
        return this.dueDate != null ? this.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    @Override
    public String toString() {
        String formatted;
        if (this.dueDate != null) {
            formatted = this.dueDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        } else {
            // If date parsing fails, use the raw input
            formatted = "Invalid date format";
        }
        return String.format("[%s] %s (by: %s)", TaskType.DEADLINE.getSymbol(), super.toString(), formatted);
    }
}


