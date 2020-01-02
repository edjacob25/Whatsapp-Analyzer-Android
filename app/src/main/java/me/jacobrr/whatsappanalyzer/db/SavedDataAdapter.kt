package me.jacobrr.whatsappanalyzer.db

import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.R
import me.jacobrr.whatsappanalyzer.activities.ResultsActivity
import me.jacobrr.whatsappanalyzer.logic.ConversationDataDB
import java.util.*

/**
 * Created by jacob on 19/11/15.
 */
class SavedDataAdapter(c: Cursor) : RecyclerView.Adapter<SavedDataAdapter.SavedItemHolder>() {
    private val conversations = ArrayList<ConversationDataDB>()

    init {
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

    class SavedItemHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = conversations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedItemHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.conversation_list_item, parent, false)
        return SavedItemHolder(view)
    }

    override fun onBindViewHolder(holder: SavedItemHolder, position: Int) {
        val conversationName = holder.view.findViewById<TextView>(R.id.conversation_item_name)
        conversationName.text = conversations[position].conversationName
        val conversationMessages = holder.view.findViewById<TextView>(R.id.conversation_item_msg)
        conversationMessages.text = holder.view.resources.getString(R.string.messages, conversations[position].totalMessages)

        holder.view.setOnClickListener { view ->
            Constants.conversationData = conversations[position]
            val intent = Intent(view.context, ResultsActivity::class.java)
            view.context.startActivity(intent)
        }
    }
}
