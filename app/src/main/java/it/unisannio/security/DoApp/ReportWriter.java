package it.unisannio.security.DoApp;

import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import it.unisannio.security.DoApp.model.Commons;
import it.unisannio.security.DoApp.model.ExceptionReport;

/**
 * Created by Luigi on 15/01/2017.
 */

public class ReportWriter {

    public static String scriviSuFile(List<ExceptionReport> reports, String pkgname){
        try {
            String fil=".txt";
            File fa = new File(Commons.path);
            if(!fa.exists())
                fa.mkdirs();
            int k=0;

            String pathCompleta=Commons.path+"report"+k+".txt";
            File f = new File(pathCompleta);
            while (f.exists()){
                ++k;
                pathCompleta=Commons.path+"report" + k + ".txt";
                f = new File(pathCompleta);
            }

            Log.i("STAMPA", "HO APERTO SCRIVI SU FILE");
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            PrintStream ps = new PrintStream(fileOutputStream);

            //Intestazione
            Date d = new Date();
            ps.println(d.toString());
            ps.println("Tested App: "+pkgname);
            ps.println();
            ps.println();

            //numero reports
            int i=1;
            for(ExceptionReport er : reports){
                ps.println( (i++) + er.toString2());
            }
            ps.close();
            return pathCompleta;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
