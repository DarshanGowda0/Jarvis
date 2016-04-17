package in.indetech.jarvis;

import java.text.SimpleDateFormat;

/**
 * Created by darshan on 16/04/16.
 */
public class Constants {

    public static String PERMISSION_PREF = "pref.permission";
    public static String ALL_PERMISSIONS_DONE = "all.permission";

    public static String color_array[] = {"#303F9F", "#9C27B0", "#00BCD4", "#727272", "#009688", "#FF4081",
            "#795548", "#FF5722", "#8BC34A", "#7B1FA2", "#FF5252", "#F57C00"};



    public static String getCurrentTime() {

        return new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }

    public static String getDate() {

        return new SimpleDateFormat("dd").format(new java.util.Date());
    }


}
