package kr.co.t_woori.market.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.main.MainActivity;
import kr.co.t_woori.market.main.SplashActivity;
import kr.co.t_woori.market.notice.NoticeActivity;
import kr.co.t_woori.market.sale.FlierActivity;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-11-22.
 */

public class MyJobService extends JobService {

    private static final String TAG = "Test";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Intent intent;
        switch (jobParameters.getTag()){
            case "flier" :
                intent = new Intent(this, FlierActivity.class);
                if(jobParameters.getExtras() != null){
                    intent.putExtras(jobParameters.getExtras());
                }
                startActivity(intent);
                break;
            case "notice" :
//                intent = new Intent(this, SplashActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("tag", "notice");
//                if(jobParameters.getExtras() != null){
//                    intent.putExtras(jobParameters.getExtras());
//                }
//                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                String channelId = getString(R.string.default_notification_channel_id);
//                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder =
//                        new NotificationCompat.Builder(this, channelId)
//                                .setSmallIcon(R.drawable.ic_cart)
//                                .setContentTitle("비래스토아")
//                                .setContentText("새로운 공지사항이 있습니다.")
//                                .setAutoCancel(true)
//                                .setSound(defaultSoundUri)
//                                .setContentIntent(pendingIntent);
//
//                NotificationManager notificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

                intent = new Intent(this, NoticeActivity.class);
                if(jobParameters.getExtras() != null){
                    intent.putExtras(jobParameters.getExtras());
                }
                startActivity(intent);
                break;
        }

        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
