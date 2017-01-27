package it.unisannio.security.DoApp.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

    // se sono attivi più processi della stessa app il comando pidof restituisce tutti i pid
    // dei processi separati da uno spazio
    public static List<Integer> getAppPID(String pkgname) {
        List<Integer> pids = new ArrayList<Integer>();
        BufferedReader bufferedReader;
        String[] commands = {"su", "-c","pidof "+pkgname};
        java.lang.Process process;
        String line;
        try {
            process = Runtime.getRuntime().exec(commands);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = bufferedReader.readLine();
            if(line!=null) {
                try {
                    pids.add(Integer.parseInt(line));
                } catch (NumberFormatException e) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    while (tokenizer.hasMoreTokens()) {
                        pids.add(Integer.parseInt(tokenizer.nextToken()));
                    }
                }
            }
            return pids;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pids;
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
