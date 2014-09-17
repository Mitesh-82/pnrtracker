package com.droidsoft.pnrtracker.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droidsoft.pnrtracker.R;
import com.droidsoft.pnrtracker.database.TicketDataFetcher;
import com.droidsoft.pnrtracker.parser.Ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mitesh.patel on 17-09-2014.
 */
public class TicketListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    TicketDataFetcher ticketDataFetcher;
    ArrayList<Ticket> ticketList;
    private Context context;


    public TicketListAdapter(Context context) {
        this.context = context;

        ticketDataFetcher = new TicketDataFetcher(context);
        ticketList = ticketDataFetcher.getAllTicketData();

        Collections.sort(ticketList);
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return ticketList.get(position).getTravelDate().;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.pnr_list_element, null);

        }

        Ticket ticket = (Ticket) getItem(position);

        if (ticket != null) {

            TextView pnrText = (TextView) v.findViewById(R.id.textView_PnrNo);
            TextView dojText = (TextView) v.findViewById(R.id.textView_DOJ);
            TextView sourceText = (TextView) v.findViewById(R.id.textView_source);
            TextView destText = (TextView) v.findViewById(R.id.textView_destination);

            if (pnrText != null) {
                pnrText.setText(ticket.getPnrNo());
            }
            if (dojText != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");

                dojText.setText(sdf.format(ticket.getTravelDate()));
            }
            if (destText != null) {

                destText.setText(ticket.getToStation());
            }
            if (sourceText != null) {

                sourceText.setText(ticket.getFromStation());
            }
        }

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
