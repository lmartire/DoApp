package it.unisannio.security.DoApp.model;

/**
 * Created by security on 31/12/2016.
 */

public enum LogLevel {
    VERBOSE(2, "verbose", 'V'),
    DEBUG(3, "debug", 'D'),
    INFO(4, "info", 'I'),
    WARN(5, "warn", 'W'),
    ERROR(6, "error", 'E'),
    ASSERT(7, "assert", 'A');


    private int mPriorityLevel;
    private String mStringValue;
    private char mPriorityLetter;

    LogLevel(int intPriority, String stringValue, char priorityChar) {
        mPriorityLevel = intPriority;
        mStringValue = stringValue;
        mPriorityLetter = priorityChar;
    }

    public static LogLevel getByString(String value) {
        for (LogLevel mode : values()) {
            if (mode.mStringValue.equals(value)) {
                return mode;
            }
        }
        return null;
    }

    /**
     * Returns the {@link LogLevel} enum matching the specified letter.
     * @param letter the letter matching a <code>LogLevel</code> enum
     * @return a <code>LogLevel</code> object or <code>null</code> if no match were found.
     */
    public static LogLevel getByLetter(char letter) {
        for (LogLevel mode : values()) {
            if (mode.mPriorityLetter == letter) {
                return mode;
            }
        }
        return null;
    }
    /**
     * Returns the {@link LogLevel} enum matching the specified letter.
     * <p/>
     * The letter is passed as a {@link String} argument, but only the first character
     * is used.
     * @param letter the letter matching a <code>LogLevel</code> enum
     * @return a <code>LogLevel</code> object or <code>null</code> if no match were found.
     */
    public static LogLevel getByLetterString(String letter) {
        if (!letter.isEmpty()) {
            return getByLetter(letter.charAt(0));
        }
        return null;
    }
    /**
     * Returns the letter identifying the priority of the {@link LogLevel}.
     */
    public char getPriorityLetter() {
        return mPriorityLetter;
    }
    /**
     * Returns the numerical value of the priority.
     */
    public int getPriority() {
        return mPriorityLevel;
    }
    /**
     * Returns a non translated string representing the LogLevel.
     */
    public String getStringValue() {
        return mStringValue;
    }
}