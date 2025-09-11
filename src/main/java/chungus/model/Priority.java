package chungus.model;

/**
 * Priority levels for tasks.
 */
public enum Priority {
    HIGH("H"),
    MEDIUM("M"),
    LOW("L");

    private final String symbol;

    Priority(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the single-letter symbol used for display.
     *
     * @return symbol string
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Parses a string into a Priority, defaulting to MEDIUM on unknown input.
     *
     * @param input raw input
     * @return parsed Priority (defaults to MEDIUM)
     */
    public static Priority parseOrDefault(String input) {
        if (input == null) {
            return MEDIUM;
        }
        String t = input.trim();
        if (t.isEmpty()) {
            return MEDIUM;
        }
        String lower = t.toLowerCase();
        switch (lower) {
        case "h":
        case "high":
            return HIGH;
        case "l":
        case "low":
            return LOW;
        case "m":
        case "med":
        case "medium":
        default:
            return MEDIUM;
        }
    }
}


