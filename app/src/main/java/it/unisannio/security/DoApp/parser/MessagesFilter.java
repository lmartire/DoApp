package it.unisannio.security.DoApp.parser;

import android.util.Log;

import it.unisannio.security.DoApp.model.ExceptionReport;
import it.unisannio.security.DoApp.model.LogCatMessage;
import it.unisannio.security.DoApp.model.PointOfFailure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by antonio on 26/12/16.
 */

public class MessagesFilter {

    /**

     The method support this type of report, parsed as List<LogCatMessage>

     [ 12-23 12:10:07.115 20687:20687 E/AndroidRuntime ]
     FATAL EXCEPTION: main
     Process: com.project.antonyflour.intentfilterex, PID: 20687
     java.lang.RuntimeException: Unable to start activity ComponentInfo{com.project.antonyflour.intentfilterex/com.project.antonyflour.intentfilterex.ShareActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String java.lang.String.toString()' on a null object reference
     at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2325)
     at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2387)
     at android.app.ActivityThread.access$800(ActivityThread.java:151)
     at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1303)
     Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String java.lang.String.toString()' on a null object reference
     at com.project.antonyflour.intentfilterex.ShareActivity.onCreate(ShareActivity.java:23)
     at android.app.Activity.performCreate(Activity.java:5990)
     at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1106)
     at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2278)
     ... 10 more
     */
    public static List<ExceptionReport> filterByFatalException(List<LogCatMessage> messages){
        ArrayList<ExceptionReport> reports = new ArrayList<ExceptionReport>();
        Iterator<LogCatMessage> messageIterator = messages.iterator();
        LogCatMessage m;
        ExceptionReport exceptionReport;
        while(messageIterator.hasNext()){
            m = messageIterator.next();

            //encountered FATAL EXCEPTION message
            if(m.getMessage().contains("FATAL EXCEPTION")){
                exceptionReport = new ExceptionReport();


                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
                Date messageTime = null;
                try {
                    messageTime = formatter.parse(m.getTime());
                    exceptionReport.setTime(messageTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.i("*DEBUG", messageTime.toString());

                //pid = -1 if impossible to find
                int pid;
                if(m.getPid()!=null && !m.getPid().isEmpty())
                    pid = Integer.parseInt(m.getPid());
                else
                    pid=-1;

                exceptionReport.setPID(pid);

                if(messageIterator.hasNext())
                    m = messageIterator.next(); //process

                //extract componentInfo
                if(messageIterator.hasNext()) {
                    m = messageIterator.next();
                    exceptionReport.setAppName(MessagesFilter.extractComponent(m.getMessage()));
                }

                //extract exception type
                while(messageIterator.hasNext() && !(m = messageIterator.next()).getMessage().contains("Caused by"));

                if(m.getMessage().contains("Caused by"))
                    exceptionReport.setType(MessagesFilter.extractExceptionType(m.getMessage()));
                else
                    exceptionReport.setType("Type not found");

                //extract stacktrace
                while(messageIterator.hasNext() && (m = messageIterator.next()).getMessage().contains("at")){
                    PointOfFailure pof = new PointOfFailure();
                    pof.setClassName(MessagesFilter.extractClass(m.getMessage()));
                    pof.setLineNumber(MessagesFilter.extractLineNumber(m.getMessage()));
                    exceptionReport.addPointOfFailure(pof);
                }

                reports.add(exceptionReport);
            }
        }
        return reports;
    }




    /**
     * extract component name from this LogCatMessage:
     *  java.lang.RuntimeException: Unable to start activity ComponentInfo{com.project.antonyflour.intentfilterex/com.project.antonyflour.intentfilterex.ShareActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String java.lang.String.toString()' on a null object reference
     * @param message
     * @return component name
     */
    public static String extractComponent(String message){
        if(!message.contains("ComponentInfo")) return "null";
        int start = message.indexOf('/')+1;
        int stop = message.lastIndexOf('}');
        return message.substring(start,stop);
    }

    /**
     * extract class name from this LogCatMessage:
     * at com.project.antonyflour.intentfilterex.ShareActivity.onCreate(ShareActivity.java:23)
     * @param message
     * @return
     */
    public static String extractClass(String message){
        int start = message.indexOf('(')+1;
        int stop = message.lastIndexOf(':');
        return message.substring(start,stop);
    }

    /**
     * extract line number from this LogCatMessage:
     * at com.project.antonyflour.intentfilterex.ShareActivity.onCreate(ShareActivity.java:23)
     * @param message
     * @return
     */
    public static int extractLineNumber(String message){
        int start = message.indexOf(':')+1;
        int stop = message.lastIndexOf(')');
        return Integer.parseInt(message.substring(start,stop));
    }


    /**
     * extract exception type from this LogCatMessage:
     * Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String java.lang.String.toString...
     * @param message
     * @return
     */
    public static String extractExceptionType(String message){
        int start = message.indexOf(':')+2;
        int stop = message.indexOf(':', start);
        return message.substring(start,stop);
    }
}