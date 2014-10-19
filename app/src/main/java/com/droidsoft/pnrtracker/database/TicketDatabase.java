package com.droidsoft.pnrtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.droidsoft.pnrtracker.syncinterface.SyncInterface;

import java.util.ArrayList;

/**
 * Created by mitesh.patel on 15-09-2014.
 * SQL TicketDatabase implementation
 */
public class TicketDatabase {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    static final String SQL_CREATE_TICKETDB_ENTRIES =
            "CREATE TABLE " + TicketDBRecord.TABLE_NAME + " (" +
                    TicketDBRecord.COLUMN_NAME_PNRNO + TEXT_TYPE + COMMA_SEP +
                    TicketDBRecord.COLUMN_NAME_TICKET_DATA + INT_TYPE + COMMA_SEP +
                    TicketDBRecord.COLUMN_NAME_SYNC_DATA + INT_TYPE + " )";


    private DBHelper dbHelper;

    private String[] projection = {
            TicketDBRecord.COLUMN_NAME_PNRNO,
            TicketDBRecord.COLUMN_NAME_TICKET_DATA};

    private String selection = TicketDBRecord.COLUMN_NAME_PNRNO + " LIKE ?";

    protected TicketDatabase(Context context) {

        dbHelper = new DBHelper(context);
    }


    public boolean isTicketRecordPresent(String pnr) {
        boolean isTicketPresent = false;

        String[] selectionArgs = {pnr};
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(TicketDBRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (c.getCount() != 0) {
            isTicketPresent = true;
        }

        c.close();

        return isTicketPresent;
    }

    public void addTicketRecord(String pnr, String pnrData) {
        addTicketRecord(pnr, pnrData, SyncInterface.SyncIntervals.NEVER);
    }

    public void addTicketRecord(String pnr, String pnrData, long interval) {
        if (isTicketRecordPresent(pnr)) {
            updatePnrData(pnr, pnrData);
        } else {

            ContentValues values = new ContentValues();
            values.put(TicketDBRecord.COLUMN_NAME_PNRNO, pnr);
            values.put(TicketDBRecord.COLUMN_NAME_TICKET_DATA, pnrData);
            values.put(TicketDBRecord.COLUMN_NAME_SYNC_DATA, interval);

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.insert(TicketDBRecord.TABLE_NAME, null, values);
            db.close();

        }
    }

    private void updatePnrData(String pnr, String pnrData) {

        String[] selectionArgs = {pnr};

        ContentValues values = new ContentValues();
        values.put(TicketDBRecord.COLUMN_NAME_TICKET_DATA, pnrData);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(TicketDBRecord.TABLE_NAME, values, selection, selectionArgs);
        db.close();

    }

    public void delTicketRecord(String pnr) {
        if (isTicketRecordPresent(pnr)) {
            String[] selectionArgs = {pnr};
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TicketDBRecord.TABLE_NAME, selection, selectionArgs);
            db.close();
        }
    }

    public ArrayList<String> getAllPnrs() {
        ArrayList<String> ticketData = new ArrayList<String>();

        String[] projection = {
                TicketDBRecord.COLUMN_NAME_TICKET_DATA};

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TicketDBRecord.TABLE_NAME, projection, null, null, null, null, null);

        cursor.moveToLast();

        while (!cursor.isBeforeFirst()) {
            ticketData.add(cursor.getString(0));
            cursor.moveToPrevious();
        }

        cursor.close();

        return ticketData;
    }

    public ArrayList<String> getPNRsForSync(int syncInterval) {
        String[] projection = {TicketDBRecord.COLUMN_NAME_PNRNO, TicketDBRecord.COLUMN_NAME_SYNC_DATA};
        String selection = TicketDBRecord.COLUMN_NAME_SYNC_DATA + " <= ?";
        String[] selectionArgs = {Integer.toString(syncInterval)};

        ArrayList<String> Pnrs = new ArrayList<String>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TicketDBRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                if (cursor.getLong(1) != 0)
                    Pnrs.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        cursor.close();

        return Pnrs;
    }


    public void updateSyncRecord(String pnr, long interval) {
        String selection = TicketDBRecord.COLUMN_NAME_PNRNO + " LIKE ?";
        String[] selectionArgs = {pnr};

        ContentValues values = new ContentValues();
        values.put(TicketDBRecord.COLUMN_NAME_SYNC_DATA, interval);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(TicketDBRecord.TABLE_NAME, values, selection, selectionArgs);
        db.close();

    }


    public abstract class TicketDBRecord implements BaseColumns {
        public static final String TABLE_NAME = "TicketDB";
        public static final String COLUMN_NAME_PNRNO = "PNR_no";
        public static final String COLUMN_NAME_TICKET_DATA = "Ticket_Data";
        public static final String COLUMN_NAME_SYNC_DATA = "Sync_Interval";

    }

}
