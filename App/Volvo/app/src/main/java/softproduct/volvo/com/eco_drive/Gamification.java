package softproduct.volvo.com.eco_drive;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by lesliedahlberg on 2016-12-02.
 */
public class Gamification {
    Context context;
    float scoreThreshold;
    float score;
    boolean overThreshold;
    long startThresholdTime;
    long stopThresholdTime;

    public Gamification(Context context, float scoreThreshold){
        this.scoreThreshold = scoreThreshold;
        this.context = context;
        score = 0;
    }
    public void updateScore(int score){
        this.score = (float) score;
        compare();
    }

    private void compare(){
        if(score >= scoreThreshold && !overThreshold){
            overThreshold = true;
            startThresholdTime = System.currentTimeMillis();
        }else if(score < scoreThreshold && overThreshold){
            stopThresholdTime = System.currentTimeMillis();
            liveNotification();
            overThreshold = false;
        }
    }

    private void liveNotification() {

        long duration = stopThresholdTime - startThresholdTime;

        Toast.makeText(context, (float)duration/1000.0f + "s fo great gameplay!", Toast.LENGTH_LONG).show();

        /*Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setContentTitle("Doing good!")
                        .setContentText(duration/1000 + " s of great scores!");
        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.getNotification());*/

    }
}
