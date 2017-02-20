package it.unisannio.security.DoApp.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by security on 05/01/2017.
 *
 * Questa classe descrive come deve essere una riga della listview che sta in activity_app_list,
 * quindi abbiamo bisogno di tener presente l'icona, il nome dell'app e il package
 *
 */


public class AppInfo {

    private String appName = "";
    private String packageName = "";
    private Drawable appIcon = null;
    private PackageInfo packageInfo = null;

    private AppInfo(){}

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName(){
        return packageName ;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName ;
    }

    public PackageInfo getPackageInfo(){
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo){
        this.packageInfo = packageInfo;
    }

    // il metodo restituisce la lista delle app che non sono di sistema
    public static List<AppInfo> getPackageInfo(Context context){
        List<AppInfo> pkgInfoList = new ArrayList<AppInfo>();

        List<ApplicationInfo> pkgAppsList = context.getApplicationContext().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        for(int i=0; i<pkgAppsList.size(); i++){
            ApplicationInfo applicationInfo = pkgAppsList.get(i);

            //prendiamo solo le app che NON sono di sistema
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0 && applicationInfo.packageName != null &&
                    !applicationInfo.packageName.equalsIgnoreCase("it.unisannio.security.DoApp"))
                try{
                    PackageInfo pkg = context.getPackageManager().getPackageInfo(applicationInfo.packageName,
                            PackageManager.GET_DISABLED_COMPONENTS
                                    | PackageManager.GET_ACTIVITIES
                                    | PackageManager.GET_RECEIVERS
                                    | PackageManager.GET_SERVICES );
                    pkgInfoList.add(fillAppInfo(pkg, context.getApplicationContext()));
                } catch(PackageManager.NameNotFoundException e){
                    Log.e("DoAppLOG", "namenotfound - AppInfo.java:83");
                }

        }
        return pkgInfoList;

    }

    private static AppInfo fillAppInfo(PackageInfo packageInfo, Context context) {
        AppInfo appInfo = new AppInfo();
        appInfo.setPackageInfo(packageInfo);
        appInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
        appInfo.setPackageName(packageInfo.packageName);
        appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));

        return appInfo;
    }

}
