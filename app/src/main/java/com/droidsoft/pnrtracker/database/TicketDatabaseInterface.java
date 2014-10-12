package com.droidsoft.pnrtracker.database;

import java.util.ArrayList;

/**
 * Created by mitesh on 14. 10. 12.
 * Ticket DB interface to be used by app
 */
public interface TicketDatabaseInterface {

    public ArrayList<String> getPNRsForSync(int syncInterval);

    public void changePNRSync(String pnr, long syncInterval);

    public void delTicket(String pnr);

    ArrayList<String> getAllSyncablePNRs();
}
