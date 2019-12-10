package me.jacobrr.whatsappanalyzer.db

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import me.jacobrr.whatsappanalyzer.R
import me.jacobrr.whatsappanalyzer.logic.ConversationDataDB
import java.util.*

/**
 * Created by jacob on 19/11/15.
 */
class SavedDataAdapter(context: Context, c: Cursor) : BaseAdapter() {
    private val conversations = ArrayList<ConversationDataDB>()
    private val inflater: LayoutInflater

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (c.moveToFirst()) {
            do {
                val conver = ConversationDataDB(
                        c.getInt(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_ID)),
                        c.getFloat(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_DAILY_AVG)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TDAY)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TMON)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_PARTICIPANTS)),
                        c.getFloat(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_REAL_DAILY_AVG)),
                        c.getInt(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_DAYS)),
                        c.getInt(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_MSGS)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_NAME))
                )

                conversations.add(conver)

            } while (c.moveToNext())
        }

    }

    override fun getCount(): Int {
        return conversations.size
    }

    override fun getItem(position: Int): ConversationDataDB {
        return conversations[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.conversation_list_item, null)
        val conversationName = view.findViewById<TextView>(R.id.conversation_item_name)
        conversationName.text = conversations[position].conversationName
        val conversationMessages = view.findViewById<TextView>(R.id.conversation_item_msg)
        conversationMessages.text = parent.resources.getString(R.string.messages, conversations[position].totalMessages)
        return view
    }
}
