package com.droidsoft.pnrtracker.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.droidsoft.pnrtracker.R;
import com.droidsoft.pnrtracker.database.TicketDataFetcher;
import com.droidsoft.pnrtracker.views.Ticket;
import com.droidsoft.pnrtracker.views.TicketWidget;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class PNRView extends Activity {
    Context context;
    RelativeLayout layout;
    TicketDataFetcher ticketDataFetcher;
    private String pnr;
    //    private TextView pnrResponse;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle.getString("PNR").equals(pnr)) {
                Ticket ticket = (Ticket) bundle.getSerializable("DATA");

                updateTicketView(ticket);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnrview);

        context = this;
        layout = (RelativeLayout) findViewById(R.id.baselayout);

//        RetrievePERStatus getPnrStatus = new RetrievePERStatus();
//
//        getPnrStatus.execute();

        ticketDataFetcher = new TicketDataFetcher(context);


        Ticket ticket = ticketDataFetcher.getTicketData("8726709666");

        updateTicketView(ticket);

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(TicketDataFetcher.ACTION_PNR_DATA_AVAILABLE);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void updateTicketView(Ticket ticket) {

        if ((ticket != null) && (ticket.getIsValid())) {
            layout.removeAllViews();
            TicketWidget ticketWidget = new TicketWidget(context, ticket);

            layout.addView(ticketWidget);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pnrview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class RetrievePERStatus extends AsyncTask<Void, Void, String> {
        String response;
        TicketWidget ticketWidget;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return getPnrResults();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }


        protected String getPnrResults() throws IOException {

            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://www.kirant400.com/irctc/pnr.php?pnrno=8726709666&rtype=JSON";
            String serverResponse = "";

            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            serverResponse = httpClient.execute(httpget, responseHandler);


            response = serverResponse;
            ticketWidget = new TicketWidget(context, response);

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            layout.addView(ticketWidget);
        }
    }
}
