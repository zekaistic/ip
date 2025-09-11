package chungus.model;

/**
 * Types of tasks supported by the application.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the single-letter symbol used in serialization/display.
     *
     * @return symbol string
     */
    public String getSymbol() {
        return symbol;
    }
}


