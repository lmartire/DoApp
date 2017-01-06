package it.unisannio.security.DoApp.model;

/**
 * Created by security on 31/12/2016.
 */

import android.support.annotation.NonNull;

/**
 * Model a single log message output from {@code logcat -v long}.
 * A logcat message has a {@link LogLevel}, the pid (process id) of the process
 * generating the message, the time at which the message was generated, and
 * the tag and message itself.
 */
public final class LogCatMessage {
    private final LogLevel mLogLevel;
    private final String mPid;
    private final String mTid;
    private final String mAppName;
    private final String mTag;
    private final String mTime;
    private final String mMessage;
    /**
     * Construct an immutable log message object.
     */
    public LogCatMessage(@NonNull LogLevel logLevel, @NonNull String pid, @NonNull String tid,
                         @NonNull String appName, @NonNull String tag,
                         @NonNull String time, @NonNull String msg) {
        mLogLevel = logLevel;
        mPid = pid;
        mAppName = appName;
        mTag = tag;
        mTime = time;
        mMessage = msg;
        long tidValue;
        try {
            // Thread id's may be in hex on some platforms.
            // Decode and store them in radix 10.
            tidValue = Long.decode(tid.trim());
        } catch (NumberFormatException e) {
            tidValue = -1;
        }
        mTid = Long.toString(tidValue);
    }
    @NonNull
    public LogLevel getLogLevel() {
        return mLogLevel;
    }
    @NonNull
    public String getPid() {
        return mPid;
    }
    @NonNull
    public String getTid() {
        return mTid;
    }
    @NonNull
    public String getAppName() {
        return mAppName;
    }
    @NonNull
    public String getTag() {
        return mTag;
    }
    @NonNull
    public String getTime() {
        return mTime;
    }
    @NonNull
    public String getMessage() {
        return mMessage;
    }
    @Override
    public String toString() {
        return mTime + ": "
                + mLogLevel.getPriorityLetter() + "/"
                + mTag + "("
                + mPid + "): "
                + mMessage;
    }
}