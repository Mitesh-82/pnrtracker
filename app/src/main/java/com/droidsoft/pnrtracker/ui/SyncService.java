package com.droidsoft.pnrtracker.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.droidsoft.pnrtracker.database.SimpleSync;
import com.droidsoft.pnrtracker.database.SyncInterface;

import java.util.Timer;
import java.util.TimerTask;

public class SyncService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = SyncInterface.SyncIntervals.EVERY_15_MINUTES; // 15 minutes
    SyncInterface syncInterface;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private int syncCounter = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        syncInterface = SimpleSync.createSimpleSync(getApplicationContext());

        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    private void doSync() {
        Context context = getApplicationContext();

        syncCounter += NOTIFY_INTERVAL;

        syncInterface.doTimedSync(syncCounter);

        if (syncCounter >= SimpleSync.SyncIntervals.MAX_INTERVAL)
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
