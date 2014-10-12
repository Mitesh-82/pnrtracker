package com.droidsoft.pnrtracker.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.droidsoft.pnrtracker.R;
import com.droidsoft.pnrtracker.database.TicketDatabaseInterface;
import com.droidsoft.pnrtracker.database.TicketDbImpl;
import com.droidsoft.pnrtracker.datatypes.Ticket;
import com.droidsoft.pnrtracker.syncinterface.SyncImpl;
import com.droidsoft.pnrtracker.syncinterface.SyncInterface;
import com.droidsoft.pnrtracker.syncinterface.SyncListener;
import com.droidsoft.pnrtracker.ui.SyncService;
import com.droidsoft.pnrtracker.ui.views.TicketListAdapter;

public class TicketListActivity extends Activity implements View.OnClickListener, SyncListener {

    private static final int PNR_LENGTH = 10;
    private ListView ticketListView;
    private EditText editTextPnrSearch;
    private ImageButton buttonSearchPnr, buttonSyncAll;
    private Context context;
    private SyncInterface syncInterface;
    private TicketListAdapter ticketListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_ticket_list);

        SyncService.createSyncService(context);

        ticketListView = (ListView) findViewById(R.id.listView_tickets);
        editTextPnrSearch = (EditText) findViewById(R.id.editTextPNR);
        buttonSearchPnr = (ImageButton) findViewById(R.id.buttonSearchPNR);
        buttonSyncAll = (ImageButton) findViewById(R.id.imageButton_SyncAll);

        editTextPnrSearch.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        buttonSearchPnr.setOnClickListener(this);
        buttonSyncAll.setOnClickListener(this);

        ticketListAdapter = new TicketListAdapter(this);
        ticketListView.setAdapter(ticketListAdapter);
        ticketListView.setOnItemClickListener(ticketListAdapter);

        syncInterface = SyncImpl.createSimpleSync(context);
        syncInterface.registerListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sync_all) {
            TicketDatabaseInterface tdbInterface = TicketDbImpl.createTicketDBImpl(context);
            for (String pnr : tdbInterface.getAllSyncablePNRs()) {
                syncInterface.doServerRequest(pnr);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSearchPnr) {
            if (editTextPnrSearch.getText().length() == PNR_LENGTH) {
                Intent intent = new Intent(context, TicketViewActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString(TicketViewActivity.BUNDLE_KEY_PNR_DATA_PNRKEY, editTextPnrSearch.getText().toString());

                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        } else if (buttonSyncAll == v) {
            TicketDatabaseInterface tdbInterface = TicketDbImpl.createTicketDBImpl(context);
            for (String pnr : tdbInterface.getAllSyncablePNRs()) {
                syncInterface.doServerRequest(pnr);
            }
        }
    }

    @Override
    public void onSyncStart() {

    }

    @Override
    public void onAllSyncComplete() {

    }

    @Override
    public void onCurrentSyncComplete(Ticket ticket, String responseJson) {
        Log.d("Syncer", "Calling Sync Complete for PNR: - " + ticket.getPnrNo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticketListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSyncError(Exception exception) {

    }
}
