package com.droidsoft.pnrtracker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mitesh.patel on 17-09-2014.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentStartService = new Intent(context, SyncService.class);

        context.startService(intentStartService);
    }


}
