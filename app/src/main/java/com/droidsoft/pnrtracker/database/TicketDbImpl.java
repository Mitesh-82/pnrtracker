package com.droidsoft.pnrtracker.database;

import android.content.Context;

import com.droidsoft.pnrtracker.datatypes.Ticket;
import com.droidsoft.pnrtracker.parser.JsonResponseParser;
import com.droidsoft.pnrtracker.syncinterface.SyncImpl;
import com.droidsoft.pnrtracker.syncinterface.SyncListener;

import java.util.ArrayList;

/**
 * Created by mitesh.patel on 15-09-2014.
 * SQL DB implementation of Ticket DB which takes care of Adding Ticket based on TicketListener
 */
public class TicketDbImpl implements TicketDatabaseInterface, SyncListener {
    public static final String BUNDLE_KEY_PNR_DATA_DATAKEY = "DATA";
    public static final String BUNDLE_KEY_PNR_DATA_PNRKEY = "PNR";
    private static TicketDbImpl myself = null;
    private final TicketDatabase ticketDB;

    private TicketDbImpl(Context context) {
        super();
        ticketDB = new TicketDatabase(context);
    }

    public static TicketDbImpl createTicketDBImpl(Context context) {

        if (myself == null) {
            myself = new TicketDbImpl(context);
            SyncImpl.createSimpleSync(context).registerListener(myself);
        }

        return myself;
    }

    @Override
    public ArrayList<String> getPNRsForSync(int syncInterval) {
        return ticketDB.getPNRsForSync(syncInterval);
    }

    @Override
    public void changePNRSync(String pnr, long syncInterval) {
        ticketDB.updateSyncRecord(pnr, syncInterval);
    }

    public ArrayList<Ticket> getAllTickets() {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();

        ArrayList<String> ticketData = ticketDB.getAllPnrs();

        if (!ticketData.isEmpty()) {
            for (String ticketStr : ticketData) {
                tickets.add(JsonResponseParser.readTicketResponse(ticketStr));
            }
        }

        return tickets;
    }

    @Override
    public void delTicket(String pnr) {
        if ((pnr == null) || (pnr.isEmpty()))
            return;

        ticketDB.delTicketRecord(pnr);
    }


    @Override
    public void onSyncStart() {

    }

    @Override
    public void onAllSyncComplete() {

    }

    @Override
    public void onCurrentSyncComplete(Ticket ticket, String responseJson) {
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

    @Override
    public ArrayList<String> getAllSyncablePNRs() {
        ArrayList<Ticket> tickets = getAllTickets();

        ArrayList<String> pnrs = new ArrayList<String>();

        for (Ticket ticket : tickets) {
            if (Ticket.IsValidTicket(ticket))
                pnrs.add(ticket.getPnrNo());
        }

        return pnrs;
    }
}
