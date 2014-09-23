package com.droidsoft.pnrtracker.webinterface;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by mitesh.patel on 15-09-2014.
 */
public class HttpBasedTicketFetcher {

    private HttpClient httpClient;

    public String getTicketData(String pnr) throws IOException {
        String serverResponse = "";

        httpClient = new DefaultHttpClient();
        String url = "http://www.kirant400.com/irctc/pnr.php?pnrno=" + pnr + "&rtype=JSON";
        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        serverResponse = httpClient.execute(httpget, responseHandler);

        if ((serverResponse == null) || (serverResponse.isEmpty()))
            throw new IOException("No PNR Data Available");

        return serverResponse;
    }
}
