package it.unisannio.security.DoApp.model;

/**
 * Created by security on 31/12/2016.
 */


import java.util.Iterator;
import java.util.Stack;

/**
 * Created by antonio on 26/12/16.
 */

public class ExceptionReport {

    private String appName;
    private int PID;
    private String type;
    private Stack<PointOfFailure> stacktrace;

    public ExceptionReport(){
        stacktrace = new Stack<PointOfFailure>();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Stack<PointOfFailure> getStacktrace() {
        return stacktrace;
    }

    public void addPointOfFailure(PointOfFailure pof){
        stacktrace.push(pof);
    }

    public String toString(){
        String stack_string="";
        Iterator<PointOfFailure> iterator = stacktrace.iterator();
        while(iterator.hasNext()){
            PointOfFailure pof = iterator.next();
            stack_string = stack_string+"\t"+pof.getClassName()+":"+pof.getLineNumber()+"\n";
        }
        return "appName: "+appName+"\n"+"PID: "+PID+"\n"+"ExceptionType: "+type+"\n"
                + "Stacktrace: \n"+stack_string;
    }
}