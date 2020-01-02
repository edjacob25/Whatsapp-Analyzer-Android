package me.jacobrr.whatsappanalyzer

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.jacobrr.whatsappanalyzer.activities.PersonActivity
import me.jacobrr.whatsappanalyzer.logic.ConversationData
import me.jacobrr.whatsappanalyzer.logic.IConversationData

/**
 * Created by jacob on 21/11/2015.
 */
class PeopleListAdapter(private val cv: IConversationData) : RecyclerView.Adapter<PeopleListAdapter.PersonHolder>() {

    var ready = false

    class PersonHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = cv.participants.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.people_list_item, parent, false)
        return PersonHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        val tName = holder.view.findViewById<TextView>(R.id.people_name)
        val tPercentage = holder.view.findViewById<TextView>(R.id.people_percentage)
        val tWords = holder.view.findViewById<TextView>(R.id.words_per_message)
        val name = cv.participants[position]
        tName.text = name
        val percentage = String.format("%.2f%%", cv.getParticipantShare(name))
        tPercentage.text = percentage
        tWords.text = String.format("%.2f", cv.getWordsAvg(name))

        Log.d("", "Binding for $name")

        holder.view.setOnClickListener { view ->
            if (ready) {
                //Toast.makeText(view.getContext(), "item", Toast.LENGTH_SHORT).show();
                Constants.person = (Constants.conversationData as ConversationData).getPersonData(position)
                val intent = Intent(view.context, PersonActivity::class.java)
                view.context.startActivity(intent)
            }

        }
    }

}
