package it.unisannio.security.DoApp.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 20/01/2017.
 */

public class UnixCommands {

    public static void clearLogCat(){
        try {
            Runtime.getRuntime().exec(new String[]{"su", "-c","logcat -c"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readLogCat(){
        BufferedReader bufferedReader;
        List<String> lines = new ArrayList<String>();

        String[] commands = {"su", "-c","logcat -d -v long"};
        java.lang.Process process;
        try {
            process = Runtime.getRuntime().exec(commands);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }


    public static List<String> readLogCat(java.lang.Process suProcess){
        BufferedReader bufferedReader;
        List<String> lines = new ArrayList<String>();

        try {
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes("logcat -d -v long \n");
            os.flush();
            bufferedReader = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }

    public static void killApp(int PID){
        java.lang.Process suProcess = null;
        String[] commands = {"su", "-c","kill "+PID};
        try {
            suProcess = Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getAppPID(String pkgname) {
        BufferedReader bufferedReader;
        String[] commands = {"su", "-c","pidof "+pkgname};
        java.lang.Process process;
        String line = null;
        try {
            process = Runtime.getRuntime().exec(commands);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(line!=null)
            return Integer.parseInt(line);
        else
            return -1;
    }

    public static void killAll(String processName){
        java.lang.Process suProcess = null;
        String[] commands = {"su", "-c","killall -9 " + processName};
        try {
            suProcess = Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
