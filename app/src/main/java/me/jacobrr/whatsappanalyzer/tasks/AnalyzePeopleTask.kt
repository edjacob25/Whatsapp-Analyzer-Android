package me.jacobrr.whatsappanalyzer.tasks

import android.app.Activity
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.PeopleListAdapter
import me.jacobrr.whatsappanalyzer.R
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
        val l = myAct.findViewById<View>(R.id.people_list) as RecyclerView
        val adapter = l.adapter as PeopleListAdapter
        adapter.ready = true
        Toast.makeText(myAct, "Done", Toast.LENGTH_SHORT).show()
    }
}
