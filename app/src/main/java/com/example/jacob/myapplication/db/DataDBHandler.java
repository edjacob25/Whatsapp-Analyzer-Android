package com.example.jacob.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jacob.myapplication.logic.ConversationDataDB;

/**
 * Created by jacob on 22/11/2015.
 */
public class DataDBHandler {
    DataReaderHelper dbHelper;

    public DataDBHandler(Context context){
        dbHelper = new DataReaderHelper(context);
    }

    public long insert(ConversationDataDB cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataReaderContract.DataEntry.COLUMN_NAME_DAILY_AVG, cv.getDailyAvg());
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_NAME, cv.getConversationName());
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_REAL_DAILY_AVG, cv.getRealDailyAvg());
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TDAY, cv.getDayDB());
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TMON, cv.getMonthDB());
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_PARTICIPANTS, cv.getParticipantsDB());
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_DAYS, cv.getTotalDaysTalked());
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_MSGS, cv.getTotalMessages());

        long newRow;
        newRow = db.insert(DataReaderContract.DataEntry.TABLE_NAME,
                DataReaderContract.DataEntry.COLUMN_NULLABLE, values);
        return newRow;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns ={
                DataReaderContract.DataEntry.COLUMN_NAME_ID,
                DataReaderContract.DataEntry.COLUMN_NAME_NAME,
                DataReaderContract.DataEntry.COLUMN_NAME_DAILY_AVG,
                DataReaderContract.DataEntry.COLUMN_NAME_REAL_DAILY_AVG,
                DataReaderContract.DataEntry.COLUMN_NAME_MOST_TDAY,
                DataReaderContract.DataEntry.COLUMN_NAME_MOST_TMON,
                DataReaderContract.DataEntry.COLUMN_NAME_PARTICIPANTS,
                DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_DAYS,
                DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_MSGS
        };
        String selection = "1 = ? OR 1 = 1";
        String[] args = {String.valueOf(1)};
        String sort = "";
        Cursor c = db.query(DataReaderContract.DataEntry.TABLE_NAME,
                columns,
                selection,
                args,
                null,
                null,
                sort
        );
        c.moveToFirst();
        return c;
    }
}
