package chungus.common;

/**
 * Custom exception type used for user-facing validation and parsing errors.
 */
public class ChungusException extends Exception {
    /**
     * Creates an exception with the provided message.
     *
     * @param message explanation for the failure
     */
    public ChungusException(String message) {
        super(message);
    }
}


