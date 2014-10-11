package com.droidsoft.pnrtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by mitesh.patel on 15-09-2014.
 */
public class TicketDatabase {
    static final String SQL_DELETE_TICKETDB_ENTRIES =
            "DROP TABLE IF EXISTS " + TicketDBRecord.TABLE_NAME;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    static final String SQL_CREATE_TICKETDB_ENTRIES =
            "CREATE TABLE " + TicketDBRecord.TABLE_NAME + " (" +
                    TicketDBRecord.COLUMN_NAME_PNRNO + TEXT_TYPE + COMMA_SEP +
                    TicketDBRecord.COLUMN_NAME_TICKET_DATA + TEXT_TYPE + " )";


    private DBHelper dbHelper;

    private String[] projection = {
            TicketDBRecord.COLUMN_NAME_PNRNO,
            TicketDBRecord.COLUMN_NAME_TICKET_DATA};

    private String selection = TicketDBRecord.COLUMN_NAME_PNRNO + " LIKE ?";

    public TicketDatabase(Context context) {

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
        if (isTicketRecordPresent(pnr)) {
            updateTicketRecord(pnr, pnrData);
        } else {

            ContentValues values = new ContentValues();
            values.put(TicketDBRecord.COLUMN_NAME_PNRNO, pnr);
            values.put(TicketDBRecord.COLUMN_NAME_TICKET_DATA, pnrData);

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.insert(TicketDBRecord.TABLE_NAME, null, values);
            db.close();

        }
    }

    private void updateTicketRecord(String pnr, String pnrData) {

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

    public ArrayList<String> getAllTicketRecords() {
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

    public String getTicketData(String pnr) {
        if (isTicketRecordPresent(pnr)) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] selectionArgs = {pnr};
            Cursor cursor = db.query(TicketDBRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

            cursor.moveToFirst();
            return cursor.getString(1);
        } else return null;
    }

    public abstract class TicketDBRecord implements BaseColumns {
        public static final String TABLE_NAME = "TicketDB";
        public static final String COLUMN_NAME_PNRNO = "PNR_no";
        public static final String COLUMN_NAME_TICKET_DATA = "Ticket_Data";

    }


}
