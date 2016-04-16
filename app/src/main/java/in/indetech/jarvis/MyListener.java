package in.indetech.jarvis;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by darshan on 16/04/16.
 */
public class MyListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //..............

        if (sbn != null && sbn.getPackageName().equalsIgnoreCase("com.whatsapp")) {

            String str = sbn.getNotification().toString();

            String s = "" + sbn.getNotification().tickerText;
            Log.d("testing from listener", s);

            if (!s.equals("null")) {
                String username = s.substring(13);
                String time = Constants.getCurrentTime();
                DbHelper dbHelper = new DbHelper(getApplicationContext());
                dbHelper.insertMessages(username, time);
            }


        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //..............
    }

}
