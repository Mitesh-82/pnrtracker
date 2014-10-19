package com.droidsoft.pnrtracker.ui.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.droidsoft.pnrtracker.database.TicketDatabaseInterface;
import com.droidsoft.pnrtracker.database.TicketDbImpl;
import com.droidsoft.pnrtracker.syncinterface.SyncInterface;
import com.droidsoft.pnrtracker.utils.Logger;

/**
 * Created by mitesh on 14. 10. 9.
 * Sync Dialog Class for Showing Sync Options
 */
public class SyncDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private String pnrNo;

    public void setPnrNo(String pnrNo) {
        this.pnrNo = pnrNo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Sync Intervals")
                .setItems(SyncInterface.SyncIntervals.SYNCINTERVAL_ARRAY, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Logger.LogD("Interval + " + i);
        TicketDatabaseInterface ticketDatabaseInterface = TicketDbImpl.createTicketDBImpl(getActivity().getApplicationContext());
        ticketDatabaseInterface.changePNRSync(pnrNo, SyncInterface.SyncIntervals.getIntervalFromIndex(i));
    }
}
