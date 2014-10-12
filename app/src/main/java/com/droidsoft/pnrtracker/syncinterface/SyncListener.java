package com.droidsoft.pnrtracker.syncinterface;

import com.droidsoft.pnrtracker.datatypes.Ticket;

/**
 * Created by mitesh.patel on 18-09-2014.
 * SyncListener Interface used for informing Sync Status
 */
public interface SyncListener {
    public void onSyncStart();

    //This api is called when current Internet Sync is completed successfully
    public void onAllSyncComplete();

    public void onCurrentSyncComplete(Ticket ticket, String responseJson);

    public void onSyncError(Exception exception);
}
