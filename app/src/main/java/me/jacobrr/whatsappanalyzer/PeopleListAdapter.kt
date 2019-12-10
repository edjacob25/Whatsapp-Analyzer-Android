package me.jacobrr.whatsappanalyzer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import me.jacobrr.whatsappanalyzer.logic.IConversationData

/**
 * Created by jacob on 21/11/2015.
 */
class PeopleListAdapter(context: Context, private val cv: IConversationData) : BaseAdapter() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return cv.participants.size
    }

    override fun getItem(position: Int): Any {
        return cv.participants[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view= inflater.inflate(R.layout.people_list_item, null)
        val tName = view.findViewById<TextView>(R.id.people_name)
        val tPercentage = view.findViewById<TextView>(R.id.people_percentage)
        val tWords = view.findViewById<TextView>(R.id.words_per_message)
        val name = cv.participants[position]
        tName.text = name
        val percentage = String.format("%.2f%%", cv.getParticipantShare(name))
        tPercentage.text = percentage
        tWords.text = String.format("%.2f", cv.getWordsAvg(name))

        return view
    }
}
