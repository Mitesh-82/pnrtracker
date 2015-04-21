package com.droidsoft.pnrtracker.webinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by mitesh.patel on 09-04-2015.
 */
public class IndianRailGovServer implements RailwayServerInterface {
    @Override
    public String getTicketData_BlockingCall(String pnr) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url_pnr = "http://www.indianrail.gov.in/cgi_bin/inet_pnstat_cgi_10521.cgi";


        HttpPost httpPost = new HttpPost(url_pnr);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("lccp_pnrno1", pnr));
        params.add(new BasicNameValuePair("lccp_cap_val", "23456"));
        params.add(new BasicNameValuePair("lccp_capinp_val", "23456"));

        httpPost.setEntity(new UrlEncodedFormEntity(params));


//        httpPost.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
        httpPost.addHeader("USER_AGENT", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
        httpPost.addHeader("Origin", "http://www.indianrail.gov.in");
        httpPost.addHeader("REFERER", "http://www.indianrail.gov.in/valid.php");

        ResponseHandler<String> responseHandlerBlocking = new BasicResponseHandler();

        String serverResponse = httpClient.execute(httpPost, responseHandlerBlocking);

        if ((serverResponse == null) || (serverResponse.isEmpty()))
            throw new IOException("No PNR Data Available");

        HtmlParser.parseHTMLtoTicketType(serverResponse);

        return serverResponse;

    }


    ResponseHandler<String> responseHandler = new BasicResponseHandler() {

        @Override
        public String handleResponse(HttpResponse response)
                throws HttpResponseException, IOException {

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                return null;

            return super.handleResponse(response);
        }

    };

    @Override
    public void getTicketData_Async(String PnrNo) {

    }

    @Override
    public void setRailwayServerResponseListener(RailwayServerResponseListener railwayServerResponseListener) {

    }

    @Override
    public void removeRailwayServerResponseListener(RailwayServerResponseListener railwayServerResponseListener) {

    }
}
