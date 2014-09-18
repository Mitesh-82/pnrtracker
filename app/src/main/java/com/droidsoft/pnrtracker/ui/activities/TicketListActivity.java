package com.droidsoft.pnrtracker.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.droidsoft.pnrtracker.R;
import com.droidsoft.pnrtracker.database.TicketDataFetcher;
import com.droidsoft.pnrtracker.ui.views.TicketListAdapter;

public class TicketListActivity extends Activity implements View.OnClickListener {

    private static final int PNR_LENGTH = 10;
    ListView ticketListView;
    EditText editTextPnrSearch;
    ImageButton buttonSearchPnr;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_ticket_list);

        ticketListView = (ListView) findViewById(R.id.listView_tickets);
        editTextPnrSearch = (EditText) findViewById(R.id.editTextPNR);
        buttonSearchPnr = (ImageButton) findViewById(R.id.buttonSearchPNR);

        editTextPnrSearch.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        buttonSearchPnr.setOnClickListener(this);


        TicketListAdapter ticketListAdapter = new TicketListAdapter(this);
        ticketListView.setAdapter(ticketListAdapter);
        ticketListView.setOnItemClickListener(ticketListAdapter);
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
        if (id == R.id.action_settings) {
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
                bundle.putString(TicketDataFetcher.BUNDLE_KEY_PNR_DATA_PNRKEY, editTextPnrSearch.getText().toString());

                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        }
    }
}
