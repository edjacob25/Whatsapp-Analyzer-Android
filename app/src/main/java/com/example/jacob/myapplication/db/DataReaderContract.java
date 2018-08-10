package com.example.jacob.myapplication.db;

import android.provider.BaseColumns;

/**
 * Created by jacob on 22/11/2015.
 */
public class DataReaderContract {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "SavedAnalysis.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataEntry.TABLE_NAME + " (" +
                    DataEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DataEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_DAILY_AVG + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_REAL_DAILY_AVG + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_MOST_TDAY + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_MOST_TMON + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_PARTICIPANTS + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_TOTAL_MSGS + TEXT_TYPE + COMMA_SEP +
                    DataEntry.COLUMN_NAME_TOTAL_DAYS + TEXT_TYPE + ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME;

    public DataReaderContract() {
    }

    public static abstract class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "analysis";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "conversationName";
        public static final String COLUMN_NULLABLE = "conversationName";
        public static final String COLUMN_NAME_DAILY_AVG = "dailyAvg";
        public static final String COLUMN_NAME_REAL_DAILY_AVG = "realDailyAvg";
        public static final String COLUMN_NAME_MOST_TDAY = "mostTalkedDay";
        public static final String COLUMN_NAME_MOST_TMON = "mostTalkedMonth";
        public static final String COLUMN_NAME_PARTICIPANTS = "participants";
        public static final String COLUMN_NAME_TOTAL_MSGS = "totalMessages";
        public static final String COLUMN_NAME_TOTAL_DAYS = "totalDays";

    }
}
