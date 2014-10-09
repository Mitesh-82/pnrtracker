package com.droidsoft.pnrtracker.database;

import android.content.Context;

import com.droidsoft.pnrtracker.datatypes.Ticket;
import com.droidsoft.pnrtracker.parser.JsonResponseParser;

import java.util.ArrayList;

/**
 * Created by mitesh.patel on 15-09-2014.
 */
public class DBBroker implements SyncListener {
    public static final String ACTION_PNR_DATA_AVAILABLE = "com.droidsoft.pnrtracker.ACTION_PNR_DATA_AVAILABLE";
    public static final String BUNDLE_KEY_PNR_DATA_DATAKEY = "DATA";
    public static final String BUNDLE_KEY_PNR_DATA_PNRKEY = "PNR";
    private static DBBroker myself = null;
    private final TicketDatabase ticketDB;

    private DBBroker(Context context) {
        ticketDB = new TicketDatabase(context);
    }

    public static DBBroker createDataFetcher(Context context) {
        if (myself == null) {
            myself = new DBBroker(context);
        }

        SimpleSync.createSimpleSync(context).registerListener(myself);

        return myself;

    }


    public ArrayList<Ticket> getAllTickets() {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();

        ArrayList<String> ticketData = ticketDB.getAllTicketRecords();

        if (!ticketData.isEmpty()) {
            for (String ticketStr : ticketData) {
                tickets.add(JsonResponseParser.readTicketResponse(ticketStr));
            }
        }

        return tickets;
    }


    @Override
    public void onSyncStart() {

    }

    @Override
    public void onSyncEnd() {

    }

    @Override
    public void onSyncComplete(Ticket ticket, String responseJson) {
        if ((ticket != null) && (responseJson != null) &&
                (ticket.getIsValid()) && (!responseJson.isEmpty())) {
            synchronized (ticketDB) {
                ticketDB.addTicketRecord(ticket.getPnrNo(), responseJson);
            }
        }
    }

    @Override
    public void onSyncError(Exception exception) {

    }

    public ArrayList<String> getAllSyncablePnrs() {
        ArrayList<Ticket> tickets = getAllTickets();

        ArrayList<String> pnrs = new ArrayList<String>();

        for (Ticket ticket : tickets) {
            if (Ticket.IsValidTicket(ticket))
                pnrs.add(ticket.getPnrNo());
        }

        return pnrs;
    }
}
