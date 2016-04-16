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

        String str = sbn.getNotification().toString();

        String s = "" + sbn.getNotification().tickerText;

        Log.d("testing", s);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //..............
    }

}
