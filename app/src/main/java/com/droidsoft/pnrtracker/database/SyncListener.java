package com.droidsoft.pnrtracker.database;

import com.droidsoft.pnrtracker.datatypes.Ticket;

/**
 * Created by mitesh.patel on 18-09-2014.
 */
public interface SyncListener {
    public void onSyncComplete(Ticket ticket, String responseJson);

    public void onSyncError(Exception exception);
}
