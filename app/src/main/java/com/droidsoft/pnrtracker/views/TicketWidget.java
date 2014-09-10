package com.droidsoft.pnrtracker.views;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by mitesh on 14. 9. 10.
 */
public class TicketWidget extends LinearLayout {
    private String jsonTicketData;

    public TicketWidget(Context context, String jsonTicketData) {
        super(context);

        this.jsonTicketData = jsonTicketData;
    }


}
