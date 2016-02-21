package com.ggg.mygcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by relfenbein on 21/02/2016.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = RegistrationIntentService.class.getSimpleName();
    private static final String[] TOPICS = {"global"};
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }
    public RegistrationIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );
            Log.i(TAG, "GCM Registration Token: " + token);

            //implement this method to send registration to app server
            sendRegistrationToServer(token);

            //subscribe to topic channels
            subscribeTopics(token);

            prefs.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).commit();
            Log.i(TAG, "Saved to preferences");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to complete token refresh\nError:\n" + e.getMessage());
            prefs.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
        }

        //Notify UI that registration is complete so progress indicator can be hidden
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token){
        Log.i(TAG, "Sending to server token: " + token);
    }

    private void subscribeTopics(String token) throws IOException{
        Log.i(TAG, "Subscribing to topics");
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
                for(String topic: TOPICS){
                    pubSub.subscribe(token, "/topics/"+topic, null);
                }
    }
}
