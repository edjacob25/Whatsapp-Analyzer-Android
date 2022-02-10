package me.jacobrr.whatsappanalyzer

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.jacobrr.whatsappanalyzer.activities.PersonActivity
import me.jacobrr.whatsappanalyzer.databinding.PeopleListItemBinding
import me.jacobrr.whatsappanalyzer.logic.ConversationData
import me.jacobrr.whatsappanalyzer.logic.IConversationData

/**
 * Created by jacob on 21/11/2015.
 */
class PeopleListAdapter(private val cv: IConversationData, var ready: Boolean = false) :
    RecyclerView.Adapter<PeopleListAdapter.PersonHolder>() {

    class PersonHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = cv.participants.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.people_list_item, parent, false)
        return PersonHolder(view)
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        val binding = PeopleListItemBinding.bind(holder.view)
        val name = cv.participants[position]
        binding.apply {
            peopleName.text = name
            peoplePercentage.text = String.format("%.2f%%", cv.getParticipantShare(name))
            wordsPerMessage.text = String.format("%.2f", cv.getWordsAvg(name))
        }

        Log.d("", "Binding for $name")

        holder.view.setOnClickListener { view ->
            if (ready) {
                Constants.person = (Constants.conversationData as ConversationData).getPersonData(position)
                val intent = Intent(view.context, PersonActivity::class.java)
                view.context.startActivity(intent)
            }

        }
    }

}
