package com.droidsoft.pnrtracker.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidsoft.pnrtracker.parser.TicketDataParser;

/**
 * Created by mitesh on 14. 9. 10.
 */
public class TicketWidget extends LinearLayout {

    public TicketWidget(Context context, String jsonTicketData) {
        super(context);

        Ticket ticket = TicketDataParser.readTicketResponse(jsonTicketData);

        this.setOrientation(LinearLayout.VERTICAL);

        inflateTicketView(ticket, context);
    }

    private void inflateTicketView(Ticket ticket, Context context) {
        if (!ticket.getIsValid()) {
            addTextView("Invalid PNR / Server Error", this, context);
            return;
        }

        addTextView("PNR : - " + ticket.getPnrNo(), this, context);

        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(HORIZONTAL);
        this.addView(horizontal);

        addTextView("Train Number : - " + ticket.getTrainNumber(), horizontal, context);
        addTextView("Train Name : - " + ticket.getTrainName(), horizontal, context);


        horizontal = new LinearLayout(context);
        horizontal.setOrientation(HORIZONTAL);
        this.addView(horizontal);
        addTextView("From : - " + ticket.getFromStation(), horizontal, context);
        addTextView("to : - " + ticket.getToStation(), horizontal, context);

        horizontal = new LinearLayout(context);
        horizontal.setOrientation(HORIZONTAL);
        this.addView(horizontal);
        addTextView("Boarding : - " + ticket.getBoardingStation(), horizontal, context);
        addTextView("Reservation : - " + ticket.getReserved_toStation(), horizontal, context);

        horizontal = new LinearLayout(context);
        horizontal.setOrientation(HORIZONTAL);
        this.addView(horizontal);
        addTextView("Travel Date : - " + ticket.getTravelDate().toString(), horizontal, context);
        addTextView("Class : - " + ticket.getReservationClass(), horizontal, context);

        for (int i = 0; i < ticket.getPassengerCount(); i++) {

            Ticket.PassengerData pd = ticket.getPassengerData().get(i);

            horizontal = new LinearLayout(context);
            horizontal.setOrientation(HORIZONTAL);
            this.addView(horizontal);
            addTextView("Passenger " + (i + 1), horizontal, context);
            addTextView("Seat No : - " + pd.getSeatNumber(), horizontal, context);
            addTextView("Status : - " + pd.getBookingStatus(), horizontal, context);
        }


    }

    private void addTextView(String data, LinearLayout layout, Context context) {
        TextView textView;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        textView = new TextView(context);
        textView.setLayoutParams(layoutParams);


        textView.setText(data);
        layout.addView(textView);
    }


}
