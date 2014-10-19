package com.droidsoft.pnrtracker.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.droidsoft.pnrtracker.syncinterface.SyncImpl;
import com.droidsoft.pnrtracker.syncinterface.SyncInterface;
import com.droidsoft.pnrtracker.utils.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class SyncService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = SyncInterface.SyncIntervals.EVERY_15_MINUTES; // 15 minutes
    private static boolean isCreated = false;
    SyncInterface syncInterface;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private int syncCounter = 0;

    public static void createSyncService(Context context) {

        if (!isCreated) {
            Intent intent = new Intent(context, SyncService.class);
            context.startService(intent);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.LogD("SyncService onCreate");

        isCreated = true;

        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        syncInterface = SyncImpl.createSimpleSync(getApplicationContext());

        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    private void doSync() {
        Logger.LogD("SyncService : doSync");

        syncCounter += NOTIFY_INTERVAL;

        syncInterface.doTimedSync(syncCounter);

        if (syncCounter >= SyncImpl.SyncIntervals.MAX_INTERVAL)
            syncCounter = 0;
    }

    class TimeDisplayTimerTask extends TimerTask {


        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    doSync();
                }

            });
        }
    }

}
