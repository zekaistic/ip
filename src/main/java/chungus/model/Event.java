package chungus.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Task that spans a start and end date/time.
 */
public class Event extends Task {
    private String fromRaw;
    private String toRaw;
    private LocalDate fromDate;
    private LocalDate toDate;

    /**
     * Creates an event.
     *
     * @param description description text
     * @param from        start date (yyyy-MM-dd or d/M/yyyy) or free text
     * @param to          end date (yyyy-MM-dd or d/M/yyyy) or free text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.fromRaw = from;
        this.toRaw = to;
        this.fromDate = tryParseDate(from);
        this.toDate = tryParseDate(to);
    }

    private LocalDate tryParseDate(String input) {
        if (input == null) {
            return null;
        }
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
                // not a date in this format; continue
            }
        }
        return null;
    }

    private String formatForDisplay(String raw, LocalDate date) {
        if (date != null) {
            return date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return raw;
    }

    public String getFrom() {
        return this.fromRaw;
    }

    public String getTo() {
        return this.toRaw;
    }

    public String getFromIso() {
        return this.fromDate != null ? this.fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : this.fromRaw;
    }

    public String getToIso() {
        return this.toDate != null ? this.toDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : this.toRaw;
    }

    @Override
    public String toString() {
        String fromDisplay = formatForDisplay(this.fromRaw, this.fromDate);
        String toDisplay = formatForDisplay(this.toRaw, this.toDate);
        return String.format(
                "[%s] %s (from: %s to: %s)",
                TaskType.EVENT.getSymbol(),
                super.toString(),
                fromDisplay,
                toDisplay);
    }
}


