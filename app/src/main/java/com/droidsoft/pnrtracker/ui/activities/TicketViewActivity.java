package com.droidsoft.pnrtracker.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.droidsoft.pnrtracker.R;
import com.droidsoft.pnrtracker.database.DBBroker;
import com.droidsoft.pnrtracker.database.SimpleSync;
import com.droidsoft.pnrtracker.database.SyncInterface;
import com.droidsoft.pnrtracker.database.SyncListener;
import com.droidsoft.pnrtracker.datatypes.Ticket;
import com.droidsoft.pnrtracker.ui.views.SyncDialog;
import com.droidsoft.pnrtracker.ui.views.TicketWidget;


public class TicketViewActivity extends Activity implements SyncListener {
    Context context;
    RelativeLayout layout;
    ActivityHandler handler = new ActivityHandler();
    private String pnr;
    private SyncInterface syncInterface;
    private ImageView imageAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnrview);

        context = this;
        layout = (RelativeLayout) findViewById(R.id.baselayout);

        imageAnimation = (ImageView)findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.draw_working);
        imageAnimation.startAnimation(animation);


        syncInterface = SimpleSync.createSimpleSync(context);
        syncInterface.registerListener(this);

        Ticket ticket = (Ticket) getIntent().getExtras().getSerializable(DBBroker.BUNDLE_KEY_PNR_DATA_DATAKEY);
        //if there is no Ticket from previous activity it means we need to request from server
        if (ticket == null) {
            pnr = getIntent().getExtras().getString(DBBroker.BUNDLE_KEY_PNR_DATA_PNRKEY);

            syncInterface.doServerRequest(pnr);
         } else {
            pnr = ticket.getPnrNo();
            updateTicketView(ticket);
        }

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

            imageAnimation.clearAnimation();
            imageAnimation.setImageDrawable(null);

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
            SyncDialog syncDialog = new SyncDialog();
            syncDialog.setPnrNo(pnr);

            syncDialog.show(this.getFragmentManager(), "Sync Intervals");
            return true;
        } else if (id == R.id.action_delete) {

            DBBroker.createDataFetcher(this).delTicket(pnr);
            SimpleSync.createSimpleSync(this).delSyncRequest(pnr);
            this.finish();
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
        if ((ticket != null) && (ticket.getIsValid()) && (ticket.getPnrNo().equals(pnr))) {
            Message msg = handler.obtainMessage(ActivityHandler.MSG_UPDATE_VIEW, ticket);

            handler.sendMessage(msg);
        }
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
