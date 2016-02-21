package com.ggg.mygcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by relfenbein on 21/02/2016.
 */
public class MyListenerService extends GcmListenerService{
    private static final String TAG = MyListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String msg = data.getString("message");
        Log.i(TAG, "FROM: " + from + "\nMessage:\n"+msg);

        if(from.startsWith("/topics/")){
            //message received from topic
        } else {
            // normal message with no topic
        }
        //process syncing etc here

        sendNotification(msg);
    }

    private void sendNotification(String msg){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0, //request code.
                intent,
                PendingIntent.FLAG_ONE_SHOT
        );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(
                0, //ID of notification
                notificationBuilder.build()
        );
    }
}
