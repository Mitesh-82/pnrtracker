package com.droidsoft.pnrtracker.syncinterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitesh.patel on 18-09-2014.
 * Interface for doing Sync
 */
public abstract class SyncInterface {
    protected List<SyncListener> syncListeners;

    protected SyncInterface() {
        syncListeners = new ArrayList<SyncListener>();
    }

    public void registerListener(SyncListener listener) {
        if (listener != null)
            syncListeners.add(listener);
    }

    public void removeListener(SyncListener listener) {
        if (listener != null)
            syncListeners.remove(listener);
    }

    public abstract void doTimedSync(int interval);

    public abstract void doServerRequest(String pnrNo);


    public static class SyncIntervals {
        public static final long EVERY_15_MINUTES = 15 * 60 * 1000;
        public static final long EVERY_30_MINUTES = 30 * 60 * 1000;
        public static final long EVERY_1_HOUR = 60 * 1 * 60 * 1000;
        public static final long EVERY_3_HOUR = 60 * 3 * 60 * 1000;
        public static final long EVERY_DAY = 60 * 24 * 60 * 1000;

        public static final long MAX_INTERVAL = EVERY_DAY;
        public static final long NEVER = 0;

        public static final String[] SYNCINTERVAL_ARRAY = {"Never", "15 Minutes", "30 Minutes",
                "1 Hour", "3 Hour", "Once a Day"};

        public static long getIntervalFromIndex(int index) {
            long interval;
            switch (index) {
                default:
                case 0:
                    interval = NEVER;
                    break;

                case 1:
                    interval = EVERY_15_MINUTES;
                    break;

                case 2:
                    interval = EVERY_30_MINUTES;
                    break;

                case 3:
                    interval = EVERY_1_HOUR;
                    break;

                case 4:
                    interval = EVERY_3_HOUR;
                    break;

                case 5:
                    interval = EVERY_1_HOUR;
                    break;
            }

            return interval;
        }
    }


}
