package in.indetech.jarvis;

import java.text.SimpleDateFormat;

/**
 * Created by darshan on 16/04/16.
 */
public class Constants {

    public static String PERMISSION_PREF = "pref.permission";
    public static String ALL_PERMISSIONS_DONE = "all.permission";

    public static String getCurrentTime() {

        return new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }

    public static String getDate() {

        return new SimpleDateFormat("dd").format(new java.util.Date());
    }


}
