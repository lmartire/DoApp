package it.unisannio.security.DoApp.model;

/**
 * Created by security on 31/12/2016.
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by antonio on 26/12/16.
 */

public class ExceptionReport {

    private String appName; //component name
    private String processName; //package name
    private int PID;
    private Date time;
    private String type;
    private Stack<PointOfFailure> stacktrace;
    private ArrayList<MalIntent> malIntents;

    public ExceptionReport(){
        stacktrace = new Stack<PointOfFailure>();
        malIntents = new ArrayList<MalIntent>();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
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

    public ArrayList<MalIntent> getMalIntents() {
        return malIntents;
    }

    public void addMalIntent(MalIntent malIntent) {
        if(malIntent!=null)
            malIntents.add(malIntent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExceptionReport that = (ExceptionReport) o;

        if (appName != null ? !appName.equals(that.appName) : that.appName != null) return false;
        if (processName != null ? !processName.equals(that.processName) : that.processName != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (stacktrace.size()!=that.stacktrace.size()) return false;

        for(int i = 0; i<stacktrace.size(); i++){
            if(!stacktrace.get(i).equals(that.stacktrace.get(i)))
                return false;
        }
        return true;

    }


    public String toString(){
        String stack_string="";
        Iterator<PointOfFailure> iterator = stacktrace.iterator();
        while(iterator.hasNext()){
            PointOfFailure pof = iterator.next();
            stack_string = stack_string+"\t"+pof.getClassName()+":"+pof.getLineNumber()+"\n";
        }

        String malintent_string="";
        for(MalIntent m : malIntents)
            malintent_string+= "\n "+m.toString();

        return "ExceptionReport: "+
                "\n\t Package Name: "+processName+
                "\n\t Component Name: "+appName+
                "\n\t PID del crash: "+PID+
                "\n\t"+ ((malintent_string.isEmpty())?"Impossibile recuperare MalIntent" : malintent_string) +
                "\n\t" + "ExceptionType: "+type+"" +
                "\n\t Stacktrace: \n"+stack_string;
    }
}