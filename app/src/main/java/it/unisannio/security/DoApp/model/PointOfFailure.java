package it.unisannio.security.DoApp.model;

/**
 * Created by security on 31/12/2016.
 */
public class PointOfFailure {

    private String className;
    private int lineNumber;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}