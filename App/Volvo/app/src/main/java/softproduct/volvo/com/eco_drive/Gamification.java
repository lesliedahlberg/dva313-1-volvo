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
    public boolean overThreshold;
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
        Toast.makeText(context, (float)duration/1000.0f + "s of great gameplay!", Toast.LENGTH_LONG).show();
    }
}
