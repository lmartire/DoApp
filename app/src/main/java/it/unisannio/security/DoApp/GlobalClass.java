package it.unisannio.security.DoApp;

import android.app.Application;

import java.util.List;

import it.unisannio.security.DoApp.model.ExceptionReport;

/**
 * Created by antonio on 27/01/17.
 * Classe usata per condividere oggetti tra i vari componenti
 */

public class GlobalClass extends Application {

    private static GlobalClass singleton;

    public static List<ExceptionReport> reports;

    public static GlobalClass getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
