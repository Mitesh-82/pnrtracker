package com.droidsoft.pnrtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mitesh.patel on 15-09-2014.
 */
public class TicketDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TICKETDB_ENTRIES =
            "CREATE TABLE " + TicketDatabase.TicketDBRecord.TABLE_NAME + " (" +
                    TicketDatabase.TicketDBRecord.COLUMN_NAME_PNRNO + TEXT_TYPE + COMMA_SEP +
                    TicketDatabase.TicketDBRecord.COLUMN_NAME_TICKET_DATA + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TicketDatabase.TicketDBRecord.TABLE_NAME;

    public TicketDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TICKETDB_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
