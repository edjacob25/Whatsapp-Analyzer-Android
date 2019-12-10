package me.jacobrr.whatsappanalyzer.db

import android.provider.BaseColumns

/**
 * Created by jacob on 22/11/2015.
 */
class DataReaderContract {

    abstract class DataEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "analysis"
            val COLUMN_NAME_ID = "_id"
            val COLUMN_NAME_NAME = "conversationName"
            val COLUMN_NULLABLE = "conversationName"
            val COLUMN_NAME_DAILY_AVG = "dailyAvg"
            val COLUMN_NAME_REAL_DAILY_AVG = "realDailyAvg"
            val COLUMN_NAME_MOST_TDAY = "mostTalkedDay"
            val COLUMN_NAME_MOST_TMON = "mostTalkedMonth"
            val COLUMN_NAME_PARTICIPANTS = "participants"
            val COLUMN_NAME_TOTAL_MSGS = "totalMessages"
            val COLUMN_NAME_TOTAL_DAYS = "totalDays"
        }

    }

    companion object {
        val DATABASE_VERSION = 2
        val DATABASE_NAME = "SavedAnalysis.db"
        private val TEXT_TYPE = " TEXT"
        private val BLOB_TYPE = " BLOB"
        private val COMMA_SEP = ","

        val SQL_CREATE_ENTRIES = "CREATE TABLE " + DataEntry.TABLE_NAME + " (" +
                DataEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                DataEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_DAILY_AVG + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_REAL_DAILY_AVG + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_MOST_TDAY + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_MOST_TMON + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_PARTICIPANTS + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_TOTAL_MSGS + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_TOTAL_DAYS + TEXT_TYPE + ")"

        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME
    }
}
