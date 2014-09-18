package com.droidsoft.pnrtracker.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by mitesh.patel on 18-09-2014.
 */
public abstract class SyncInterface {
    protected List<SyncListener> syncListeners;
    private boolean isSyncInProgress = false;
    private Set<String> pendingPnr;

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

    public class SyncIntervals {
        public static final int EVERY_15_MINUTES = 15;
        public static final int EVERY_30_MINUTES = 30;
        public static final int EVERY_1_HOUR = 60 * 1;
        public static final int EVERY_3_HOUR = 60 * 3;
        public static final int EVERY_DAY = 60 * 24;

        public static final int MAX_INTERVAL = EVERY_DAY;
        public static final int NEVER = Integer.MAX_VALUE;
    }


}
