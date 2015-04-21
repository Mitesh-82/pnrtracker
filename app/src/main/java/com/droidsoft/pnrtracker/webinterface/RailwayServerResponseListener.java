package com.droidsoft.pnrtracker.webinterface;

/**
 * Created by mitesh.patel on 09-04-2015.
 */
public interface RailwayServerResponseListener {

    void handlerServerResponse(String PnrNo, String serverResponse);

    void handlerException(String PnrNo, Exception exception);
}
