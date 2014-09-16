package com.droidsoft.pnrtracker.views;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidsoft.pnrtracker.parser.Ticket;
import com.droidsoft.pnrtracker.parser.TicketDataParser;

import java.text.SimpleDateFormat;

/**
 * Created by mitesh on 14. 9. 10.
 */
public class TicketWidget extends LinearLayout {
    private final int MARGINS = 4;

    public TicketWidget(Context context, String jsonTicketData) {
        super(context);

        Ticket ticket = TicketDataParser.readTicketResponse(jsonTicketData);
        initTicketWidget();
        inflateTicketView(ticket, context);
    }


    public TicketWidget(Context context, Ticket ticket) {
        super(context);
        initTicketWidget();
        inflateTicketView(ticket, context);
    }

    private void initTicketWidget() {
        this.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);


    }

    private void inflateTicketView(Ticket ticket, Context context) {
        LinearLayout layout;

        if (!ticket.getIsValid()) {
            addTextView("Invalid PNR / Server Error", this, context);
            return;
        }

        layout = addHorizontalLayout(this, context);
        addTextView("PNR : - " + ticket.getPnrNo(), layout, context);

        layout = addHorizontalLayout(this, context);
        addTextView("Train : - " + ticket.getTrainNumber() + " / " + ticket.getTrainName(), layout, context);

        layout = addHorizontalLayout(this, context);
        addTextView("From : - " + ticket.getFromStation(), layout, context);
        addTextView("To : - " + ticket.getToStation(), layout, context);

        layout = addHorizontalLayout(this, context);
        addTextView("Boarding : - " + ticket.getBoardingStation(), layout, context);
        addTextView("Reservation : - " + ticket.getReserved_toStation(), layout, context);

        layout = addHorizontalLayout(this, context);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        addTextView("Travel Date : - " + sdf.format(ticket.getTravelDate()), layout, context);
        addTextView("Class : - " + ticket.getReservationClass(), layout, context);

        layout = addHorizontalLayout(this, context);
        addTextView("Passenger#", layout, context);
        addTextView("Seat No", layout, context);
        addTextView("Status", layout, context);


        for (int i = 0; i < ticket.getPassengerCount(); i++) {

            Ticket.PassengerData pd = ticket.getPassengerData().get(i);

            layout = addHorizontalLayout(this, context);
            addTextView("#" + (i + 1), layout, context);
            addTextView(pd.getSeatNumber(), layout, context);
            addTextView(pd.getBookingStatus(), layout, context);
        }


    }

    private LinearLayout addVerticalLayout(TicketWidget ticketWidget, Context context) {
        LinearLayout vertical = new LinearLayout(context);
        vertical.setOrientation(VERTICAL);

        ticketWidget.addView(vertical);
        return vertical;
    }


    private LinearLayout addHorizontalLayout(TicketWidget ticketWidget, Context context) {
        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(HORIZONTAL);
        ticketWidget.addView(horizontal);
        return horizontal;
    }

    private void addTextView(String data, LinearLayout layout, Context context) {
        TextView textView;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layoutParams.setMargins(0, MARGINS, 0, MARGINS);
        textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(data);


        //check if W/L
        if (data.contains("W/L")) {
            textView.setTextColor(Color.RED);
        } else if (data.contains("RAC")) {
            textView.setTextColor(Color.MAGENTA);
        }

        layout.addView(textView);
    }


}
