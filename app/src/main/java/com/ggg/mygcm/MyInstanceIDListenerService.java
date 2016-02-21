package com.ggg.mygcm;

import android.content.Intent;
import android.net.nsd.NsdManager;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by relfenbein on 21/02/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Fetch updated instance token
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

    }
}
