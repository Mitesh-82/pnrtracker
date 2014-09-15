package com.droidsoft.pnrtracker.database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.droidsoft.pnrtracker.parser.TicketDataParser;
import com.droidsoft.pnrtracker.views.Ticket;
import com.droidsoft.pnrtracker.webinterface.HttpBasedTicketFetcher;

import java.io.IOException;

/**
 * Created by mitesh.patel on 15-09-2014.
 */
public class TicketDataFetcher {
    public static final String ACTION_PNR_DATA_AVAILABLE = "com.droidsoft.pnrtracker.ACTION_PNR_DATA_AVAILABLE";
    private Context context;
    private TicketDatabase ticketDB;
    private HttpBasedTicketFetcher httpBasedTicketFetcher;

    public TicketDataFetcher(Context context) {
        this.context = context;
        ticketDB = new TicketDatabase(context);
        httpBasedTicketFetcher = new HttpBasedTicketFetcher(context);
    }

    public Ticket getTicketData(String pnr) {
        Ticket ticket = null;

        if (ticketDB.isTicketRecordPresent(pnr)) {
            try {
                ticket = TicketDataParser.readTicketResponse(ticketDB.getTicketData(pnr));
            } catch (Exception e) {
                WorkerThread thread = new WorkerThread(ticketDB, pnr, httpBasedTicketFetcher);
                thread.start();
                return ticket;
            }

        }


        WorkerThread thread = new WorkerThread(ticketDB, pnr, httpBasedTicketFetcher);
        thread.start();
        return ticket;
    }

    public void broadcastTicketData(Ticket ticket) {


        Bundle bundle = new Bundle();
        bundle.putString("PNR", ticket.getPnrNo());
        bundle.putSerializable("DATA", ticket);

        Intent intent = new Intent(ACTION_PNR_DATA_AVAILABLE);
        intent.putExtras(bundle);

        context.sendBroadcast(intent);

    }


    class WorkerThread extends Thread {
        TicketDatabase ticketDatabase;
        String Pnr;
        HttpBasedTicketFetcher httpBasedTicketFetcher;
        Ticket ticket;

        WorkerThread(TicketDatabase ticketDatabase, String pnr, HttpBasedTicketFetcher httpBasedTicketFetcher) {
            this.ticketDatabase = ticketDatabase;
            Pnr = pnr;
            this.httpBasedTicketFetcher = httpBasedTicketFetcher;


        }

        @Override
        public void run() {
            super.run();

            try {
                String serverResponse;
                serverResponse = httpBasedTicketFetcher.getTicketData(Pnr);

                ticket = TicketDataParser.readTicketResponse(serverResponse);

                if (ticket.getIsValid()) {
                    ticketDatabase.updateTicketRecord(Pnr, serverResponse);
                    broadcastTicketData(ticket);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
