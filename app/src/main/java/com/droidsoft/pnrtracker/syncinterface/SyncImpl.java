package com.droidsoft.pnrtracker.syncinterface;

import android.content.Context;

import com.droidsoft.pnrtracker.database.TicketDatabaseInterface;
import com.droidsoft.pnrtracker.database.TicketDbImpl;
import com.droidsoft.pnrtracker.datatypes.Ticket;
import com.droidsoft.pnrtracker.parser.JsonResponseParser;
import com.droidsoft.pnrtracker.webinterface.HttpBasedTicketFetcher;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mitesh.patel on 18-09-2014.
 * Sync Impl responsible for doing sync and updating DB with DATA
 */
public class SyncImpl extends SyncInterface {
    private static SyncImpl myself = null;
    Context context;
    private HttpBasedTicketFetcher httpBasedTicketFetcher;
    private TicketDatabaseInterface ticketDatabaseInterface;
    private int requestCounter = 0;


    private SyncImpl(Context context) {
        super();
        this.context = context;
        httpBasedTicketFetcher = new HttpBasedTicketFetcher();

        ticketDatabaseInterface = TicketDbImpl.createTicketDBImpl(context);
    }

    public static SyncImpl createSimpleSync(Context context) {
        if (myself == null) {
            myself = new SyncImpl(context);
        }
        return myself;
    }

    @Override
    public void doTimedSync(int interval) {
        ArrayList<String> syncPnrs = ticketDatabaseInterface.getPNRsForSync(interval);

        for (String pnr : syncPnrs) {
            doServerRequest(pnr);
        }
    }

    @Override
    public void doServerRequest(String pnrNo) {

        requestCounter++;
        for (SyncListener syncListener : syncListeners) {
            syncListener.onSyncStart();
        }

        WorkerThread thread = new WorkerThread(pnrNo, httpBasedTicketFetcher);
        thread.start();

    }


    class WorkerThread extends Thread {
        String Pnr;
        HttpBasedTicketFetcher httpBasedTicketFetcher;
        Ticket ticket;

        WorkerThread(String pnr, HttpBasedTicketFetcher httpBasedTicketFetcher) {
            Pnr = pnr;
            this.httpBasedTicketFetcher = httpBasedTicketFetcher;
        }

        @Override
        public void run() {
            super.run();

            try {
                String serverResponse;
                serverResponse = httpBasedTicketFetcher.getTicketData(Pnr);

                ticket = JsonResponseParser.readTicketResponse(serverResponse);

                for (SyncListener syncListener : syncListeners)
                    syncListener.onCurrentSyncComplete(ticket, serverResponse);

            } catch (IOException e) {
                for (SyncListener syncListener : syncListeners)
                    syncListener.onSyncError(e);
            } finally {
                requestCounter--;
                if (requestCounter == 0) {
                    for (SyncListener syncListener : syncListeners)
                        syncListener.onAllSyncComplete();
                }
            }
        }
    }
}
