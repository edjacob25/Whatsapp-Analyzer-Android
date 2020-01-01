package me.jacobrr.whatsappanalyzer.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import me.jacobrr.whatsappanalyzer.logic.ConversationDataDB

/**
 * Created by jacob on 22/11/2015.
 */
class DataDBHandler(context: Context) {
    private var dbHelper: DataReaderHelper = DataReaderHelper(context)

    val allData: Cursor
        get() {
            val db = dbHelper.readableDatabase
            val columns = arrayOf(DataReaderContract.DataEntry.COLUMN_NAME_ID, DataReaderContract.DataEntry.COLUMN_NAME_NAME, DataReaderContract.DataEntry.COLUMN_NAME_DAILY_AVG, DataReaderContract.DataEntry.COLUMN_NAME_REAL_DAILY_AVG, DataReaderContract.DataEntry.COLUMN_NAME_MOST_TDAY, DataReaderContract.DataEntry.COLUMN_NAME_MOST_TMON, DataReaderContract.DataEntry.COLUMN_NAME_PARTICIPANTS, DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_DAYS, DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_MSGS)
            val selection = "1 = ? OR 1 = 1"
            val args = arrayOf(1.toString())
            val sort = ""
            val c = db.query(DataReaderContract.DataEntry.TABLE_NAME,
                    columns,
                    selection,
                    args,
                    null, null,
                    sort
            )
            c.moveToFirst()
            return c
        }

    fun insert(cv: ConversationDataDB): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues()

        values.put(DataReaderContract.DataEntry.COLUMN_NAME_DAILY_AVG, cv.dailyAvg)
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_NAME, cv.conversationName)
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_REAL_DAILY_AVG, cv.realDailyAvg)
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TDAY, cv.dayDB)
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TMON, cv.monthDB)
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_PARTICIPANTS, cv.participantsDB)
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_DAYS, cv.totalDaysTalked)
        values.put(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_MSGS, cv.totalMessages)

        val newRow: Long
        newRow = db.insert(DataReaderContract.DataEntry.TABLE_NAME,
                DataReaderContract.DataEntry.COLUMN_NULLABLE, values)
        return newRow
    }
}
