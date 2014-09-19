package com.droidsoft.pnrtracker.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.droidsoft.pnrtracker.R;
import com.droidsoft.pnrtracker.database.DBBroker;
import com.droidsoft.pnrtracker.database.SimpleSync;
import com.droidsoft.pnrtracker.database.SyncInterface;
import com.droidsoft.pnrtracker.database.SyncListener;
import com.droidsoft.pnrtracker.datatypes.Ticket;
import com.droidsoft.pnrtracker.ui.views.TicketWidget;


public class TicketViewActivity extends Activity implements SyncListener {
    Context context;
    RelativeLayout layout;
    ActivityHandler handler = new ActivityHandler();
    private String pnr;
    private SyncInterface syncInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnrview);

        context = this;
        layout = (RelativeLayout) findViewById(R.id.baselayout);

        syncInterface = SimpleSync.createSimpleSync(context);
        syncInterface.registerListener(this);

        Ticket ticket = (Ticket) getIntent().getExtras().getSerializable(DBBroker.BUNDLE_KEY_PNR_DATA_DATAKEY);
        //if there is no Ticket from previous activity it means we need to request from server
        if (ticket == null) {
            String pnr = getIntent().getExtras().getString(DBBroker.BUNDLE_KEY_PNR_DATA_PNRKEY);

            syncInterface.doServerRequest(pnr);
        } else
            updateTicketView(ticket);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (syncInterface != null) {
            syncInterface.removeListener(this);
        }
    }

    private void updateTicketView(Ticket ticket) {

        if ((ticket != null) && (ticket.getIsValid())) {
            layout.removeAllViews();
            TicketWidget ticketWidget = new TicketWidget(context, ticket);

            layout.addView(ticketWidget);
        }
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

    @Override
    public void onSyncStart() {

    }

    @Override
    public void onSyncEnd() {

    }

    @Override
    public void onSyncComplete(Ticket ticket, String responseJson) {
        Message msg = handler.obtainMessage(ActivityHandler.MSG_UPDATE_VIEW, ticket);

        handler.sendMessage(msg);
    }

    @Override
    public void onSyncError(Exception exception) {

    }

    class ActivityHandler extends Handler {
        public static final int MSG_UPDATE_VIEW = 0;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_UPDATE_VIEW:
                    updateTicketView((Ticket) msg.obj);
            }
        }
    }
}
