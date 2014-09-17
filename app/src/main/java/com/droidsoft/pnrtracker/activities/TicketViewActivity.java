package com.droidsoft.pnrtracker.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.droidsoft.pnrtracker.R;
import com.droidsoft.pnrtracker.database.TicketDataFetcher;
import com.droidsoft.pnrtracker.parser.Ticket;
import com.droidsoft.pnrtracker.views.TicketWidget;


public class TicketViewActivity extends Activity {
    Context context;
    RelativeLayout layout;
    TicketDataFetcher ticketDataFetcher;
    private String pnr;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle.getString(TicketDataFetcher.BUNDLE_KEY_PNR_DATA_PNRKEY).equals(pnr)) {
                Ticket ticket = (Ticket) bundle.getSerializable(TicketDataFetcher.BUNDLE_KEY_PNR_DATA_DATAKEY);

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

        ticketDataFetcher = new TicketDataFetcher(context);

        //TODO: remove Hardcoding
        Ticket ticket = ticketDataFetcher.getTicketDataFromServer("8726709666");
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
}
