package com.droidsoft.pnrtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by mitesh.patel on 17-09-2014.
 */
public class SyncDatabase {

    static final String SQL_DELETE_SYNCDB_ENTRIES =
            "DROP TABLE IF EXISTS " + SyncDBRecord.TABLE_NAME;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    static final String SQL_CREATE_SYNCDB_ENTRIES =
            "CREATE TABLE " + SyncDBRecord.TABLE_NAME + " (" +
                    SyncDBRecord.COLUMN_NAME_PNRNO + TEXT_TYPE + COMMA_SEP +
                    SyncDBRecord.COLUMN_NAME_SYNC_DATA + INT_TYPE + " )";
    private Context context;
    private DBHelper dbhelper;

    public SyncDatabase(Context context) {
        this.context = context;

        dbhelper = new DBHelper(context);
    }

    public boolean isSyncRecordPresent(String pnr) {
        String[] projection = {SyncDBRecord.COLUMN_NAME_PNRNO};
        String selection = SyncDBRecord.COLUMN_NAME_PNRNO + " LIKE ?";
        String[] selectionArgs = {pnr};
        boolean isSyncPresent = false;

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor c = db.query(SyncDBRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (c.getCount() != 0) {
            isSyncPresent = true;
        }

        c.close();

        return isSyncPresent;
    }

    public ArrayList<String> getPrnsforSync(int syncInterval) {
        String[] projection = {SyncDBRecord.COLUMN_NAME_PNRNO};
        String selection = SyncDBRecord.COLUMN_NAME_SYNC_DATA + " <= ?";
        String[] selectionArgs = {Integer.toString(syncInterval)};

        ArrayList<String> Pnrs = new ArrayList<String>();

        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor cursor = db.query(SyncDBRecord.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (cursor.isAfterLast()) {
                Pnrs.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }

        cursor.close();

        return Pnrs;
    }

    public void addPnr(String pnr, int syncInterval) {

        if ((pnr == null) || pnr.isEmpty() || (syncInterval == SyncInterface.SyncIntervals.NEVER)) {
            return;
        }

        if (isSyncRecordPresent(pnr)) {
            updateSyncRecord(pnr, syncInterval);
        } else {

            ContentValues values = new ContentValues();
            values.put(SyncDBRecord.COLUMN_NAME_PNRNO, pnr);
            values.put(SyncDBRecord.COLUMN_NAME_SYNC_DATA, syncInterval);

            SQLiteDatabase db = dbhelper.getWritableDatabase();

            db.insert(SyncDBRecord.TABLE_NAME, null, values);

        }

    }

    private void updateSyncRecord(String pnr, int interval) {
        String selection = SyncDBRecord.COLUMN_NAME_PNRNO + " LIKE ?";
        String[] selectionArgs = {pnr};


        ContentValues values = new ContentValues();
        values.put(SyncDBRecord.COLUMN_NAME_SYNC_DATA, interval);

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.update(SyncDBRecord.TABLE_NAME, values, selection, selectionArgs);

    }

    public void delPnr(String pnr) {
        if (isSyncRecordPresent(pnr)) {
            String selection = SyncDBRecord.COLUMN_NAME_PNRNO + " LIKE ?";
            String[] selectionArgs = {pnr};
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            db.delete(SyncDBRecord.TABLE_NAME, selection, selectionArgs);
        }
    }

    public abstract class SyncDBRecord implements BaseColumns {
        public static final String TABLE_NAME = "SyncDB";
        public static final String COLUMN_NAME_PNRNO = "PNR_no";
        public static final String COLUMN_NAME_SYNC_DATA = "Sync_Data";
    }




}
