package me.jacobrr.whatsappanalyzer.tasks

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.R
import me.jacobrr.whatsappanalyzer.activities.PersonActivity
import me.jacobrr.whatsappanalyzer.logic.ConversationData

/**
 * Created by jacob on 27/11/2015.
 */
class AnalyzePeopleTask(private val myAct: Activity) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        (Constants.conversationData as ConversationData).createPeopleData()
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        val l = myAct.findViewById<View>(R.id.people_list) as ListView
        l.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            //Toast.makeText(view.getContext(), "item", Toast.LENGTH_SHORT).show();

            Constants.person = (Constants.conversationData as ConversationData).getPersonData(position)
            val intent = Intent(view.context, PersonActivity::class.java)
            myAct.startActivity(intent)
        }
        Toast.makeText(myAct, "Done", Toast.LENGTH_SHORT).show()
    }
}
