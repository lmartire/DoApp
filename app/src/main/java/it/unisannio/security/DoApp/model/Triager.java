package it.unisannio.security.DoApp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonio on 20/01/17.
 */

public class Triager {

    public static List<ExceptionReport> triage(List<ExceptionReport> reports){
        //tutti gli exceptionReports passati da parametro hanno un solo MalIntent nella propria lista
        List<ExceptionReport> finalReports = new ArrayList<ExceptionReport>();
        for(ExceptionReport er : reports){
            if(!finalReports.contains(er))
                finalReports.add(er);
            else{
                //aggiungo solo il MalIntent alla lista (di ExceptionReport) se l'ExceptionReport è già stato inserito
                ExceptionReport current = finalReports.get(finalReports.indexOf(er));
                current.addMalIntent(er.getMalIntents().get(0));
            }
        }
        return finalReports;
    }
}
