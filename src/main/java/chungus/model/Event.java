package chungus.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import chungus.common.ChungusException;
import chungus.common.Constants;

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
     * @param from        start date (yyyy-MM-dd, d/M/yyyy, or d-M-yyyy)
     * @param to          end date (yyyy-MM-dd, d/M/yyyy, or d-M-yyyy)
     * @throws ChungusException if either date format is invalid
     */
    public Event(String description, String from, String to) throws ChungusException {
        super(description);
        this.fromRaw = from;
        this.toRaw = to;
        this.fromDate = tryParseDate(from);
        this.toDate = tryParseDate(to);

        if (this.fromDate == null) {
            throw new ChungusException(Constants.MSG_INVALID_START_DATE);
        }
        if (this.toDate == null) {
            throw new ChungusException(Constants.MSG_INVALID_END_DATE);
        }
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
        return date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
    }

    public String getFrom() {
        return this.fromRaw;
    }

    public String getTo() {
        return this.toRaw;
    }

    public String getFromIso() {
        return this.fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getToIso() {
        return this.toDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
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


