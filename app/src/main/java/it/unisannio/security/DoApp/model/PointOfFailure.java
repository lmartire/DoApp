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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointOfFailure that = (PointOfFailure) o;

        if (lineNumber != that.lineNumber) return false;
        return className != null ? className.equals(that.className) : that.className == null;

    }

}