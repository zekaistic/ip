package chungus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private LocalDate dueDate;

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

    public String getByIso() {
        return this.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public String toString() {
        String formatted = this.dueDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        return String.format("[%s] %s (by: %s)", TaskType.DEADLINE.getSymbol(), super.toString(), formatted);
    }
}


