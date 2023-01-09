package im.zego.effectsexample.effectsforexpress.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppUtils {
    public static final String VERSION = "version";


    public static int getApp(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(VERSION,0);
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void setApp(Context context, int value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VERSION,value);
        editor.apply();

    }

}
