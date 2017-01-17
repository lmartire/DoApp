package it.unisannio.security.DoApp.model;

/**
 * Created by security on 31/12/2016.
 */


import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by antonio on 26/12/16.
 */

public class ExceptionReport {

    private String appName;
    private int PID;
    private Date time;
    private String type;
    private Stack<PointOfFailure> stacktrace;
    private MalIntent malIntent;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public MalIntent getMalIntent() {
        return malIntent;
    }

    public void setMalIntent(MalIntent malIntent) {
        this.malIntent = malIntent;
    }


    public String toString(){
        String stack_string="";
        Iterator<PointOfFailure> iterator = stacktrace.iterator();
        while(iterator.hasNext()){
            PointOfFailure pof = iterator.next();
            stack_string = stack_string+"\t"+pof.getClassName()+":"+pof.getLineNumber()+"\n";
        }
        return "appName: "+appName+"\n"+"PID: "+PID+"\n"+
                ((malIntent==null)?"Impossibile recuperare MalIntent" : malIntent.toString()) +
                "\n" + "ExceptionType: "+type+"\n"
                + "Stacktrace: \n"+stack_string;
    }
}