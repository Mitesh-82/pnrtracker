package com.droidsoft.pnrtracker.webinterface;

import java.io.IOException;

/**
 * Created by mitesh.patel on 09-04-2015.
 */
public interface RailwayServerInterface {
    String getTicketData_BlockingCall(String pnr) throws IOException;

    void getTicketData_Async(String PnrNo);

    void setRailwayServerResponseListener(RailwayServerResponseListener railwayServerResponseListener);

    void removeRailwayServerResponseListener(RailwayServerResponseListener railwayServerResponseListener);
}
