package chungus.common;

/**
 * String and token constants to avoid magic strings
 * across the codebase.
 */
public final class Constants {
    public static final String SPACE = " ";
    public static final String TOKEN_BY = "/by";
    public static final String TOKEN_FROM = "/from";
    public static final String TOKEN_TO = "/to";

    public static final String DEFAULT_STORAGE_PATH = "data/chungus.txt";

    public static final String MSG_UNKNOWN = "I'm sorry, but I don't know what that means :-(";
    public static final String MSG_PROVIDE_KEYWORD = "Please provide a keyword to find.";
    public static final String MSG_PROVIDE_TASK_NUMBER = "Please provide a task number.";
    public static final String MSG_TODO_EMPTY = "The description of a todo cannot be empty.";
    public static final String MSG_DEADLINE_NEEDS_BY = "Deadline command must include '/by' followed by the due date.";
    public static final String MSG_DEADLINE_DESC_EMPTY = "The description of a deadline cannot be empty.";
    public static final String MSG_DEADLINE_DATE_EMPTY = "The due date cannot be empty.";
    public static final String MSG_EVENT_NEEDS_FROM_TO = "Event command must include both '/from' and"
                                                    + " '/to' followed by start and end times.";
    public static final String MSG_EVENT_DESC_EMPTY = "The description of an event cannot be empty.";
    public static final String MSG_EVENT_START_EMPTY = "The start time cannot be empty.";
    public static final String MSG_EVENT_END_EMPTY = "The end time cannot be empty.";

    private Constants() {}

}


