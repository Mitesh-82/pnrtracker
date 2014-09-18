package com.droidsoft.pnrtracker.database;

import android.content.Context;

import com.droidsoft.pnrtracker.datatypes.Ticket;
import com.droidsoft.pnrtracker.parser.JsonResponseParser;
import com.droidsoft.pnrtracker.webinterface.HttpBasedTicketFetcher;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mitesh.patel on 18-09-2014.
 */
public class SimpleSync extends SyncInterface {
    private static SimpleSync myself = null;
    Context context;
    private HttpBasedTicketFetcher httpBasedTicketFetcher;
    private SyncDatabase syncDatabase;


    private SimpleSync(Context context) {
        super();
        this.context = context;
        httpBasedTicketFetcher = new HttpBasedTicketFetcher(context);

        syncDatabase = new SyncDatabase(context);
    }

    public static SimpleSync createSimpleSync(Context context) {
        if (myself == null) {
            myself = new SimpleSync(context);
        }
        return myself;
    }

    @Override
    public void doTimedSync(int interval) {
        ArrayList<String> syncPnrs = syncDatabase.getPrnsforSync(interval);

        for (String pnr : syncPnrs) {
            doServerRequest(pnr);
        }
    }

    @Override
    public void doServerRequest(String pnrNo) {

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
                    syncListener.onSyncComplete(ticket, serverResponse);


            } catch (IOException e) {
                for (SyncListener syncListener : syncListeners)
                    syncListener.onSyncError(e);
            }
        }
    }
}