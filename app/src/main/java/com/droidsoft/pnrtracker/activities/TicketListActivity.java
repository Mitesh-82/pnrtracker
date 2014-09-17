package com.droidsoft.pnrtracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.droidsoft.pnrtracker.R;

public class TicketListActivity extends Activity {

    ListView ticketListView;
    EditText editTextPnrSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        ticketListView = (ListView) findViewById(R.id.listView_tickets);
        editTextPnrSearch = (EditText) findViewById(R.id.editTextPNR);

        editTextPnrSearch.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

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
}
