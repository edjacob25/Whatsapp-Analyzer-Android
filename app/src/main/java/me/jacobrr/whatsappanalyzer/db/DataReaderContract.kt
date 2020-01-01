package me.jacobrr.whatsappanalyzer.db

import android.provider.BaseColumns

/**
 * Created by jacob on 22/11/2015.
 */
class DataReaderContract {

    abstract class DataEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "analysis"
            const val COLUMN_NAME_ID = "_id"
            const val COLUMN_NAME_NAME = "conversationName"
            const val COLUMN_NULLABLE = "conversationName"
            const val COLUMN_NAME_DAILY_AVG = "dailyAvg"
            const val COLUMN_NAME_REAL_DAILY_AVG = "realDailyAvg"
            const val COLUMN_NAME_MOST_TDAY = "mostTalkedDay"
            const val COLUMN_NAME_MOST_TMON = "mostTalkedMonth"
            const val COLUMN_NAME_PARTICIPANTS = "participants"
            const val COLUMN_NAME_TOTAL_MSGS = "totalMessages"
            const val COLUMN_NAME_TOTAL_DAYS = "totalDays"
        }

    }

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "SavedAnalysis.db"
        private const val TEXT_TYPE = " TEXT"
        private const val BLOB_TYPE = " BLOB"
        private const val COMMA_SEP = ","

        const val SQL_CREATE_ENTRIES = "CREATE TABLE " + DataEntry.TABLE_NAME + " (" +
                DataEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                DataEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_DAILY_AVG + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_REAL_DAILY_AVG + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_MOST_TDAY + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_MOST_TMON + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_PARTICIPANTS + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_TOTAL_MSGS + TEXT_TYPE + COMMA_SEP +
                DataEntry.COLUMN_NAME_TOTAL_DAYS + TEXT_TYPE + ")"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME
    }
}
