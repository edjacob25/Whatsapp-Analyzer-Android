package me.jacobrr.whatsappanalyzer.tasks

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.ParcelFileDescriptor
import android.view.View
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.R
import me.jacobrr.whatsappanalyzer.activities.ResultsActivity
import me.jacobrr.whatsappanalyzer.logic.ConversationData
import me.jacobrr.whatsappanalyzer.logic.LineAnalyzer

import java.io.*

/**
 * Created by jacob on 21/11/2015.
 */
class CreateDataTask(internal var myAct: Activity, internal var fileName: String) : AsyncTask<ParcelFileDescriptor, Void, ConversationData>() {

    override fun onPreExecute() {
        super.onPreExecute()
        myAct.findViewById<View>(R.id.saved_analysis_list).visibility = View.INVISIBLE
        //Debug.startMethodTracing("analysis");
        myAct.findViewById<View>(R.id.loadingPanel).visibility = View.VISIBLE
    }

    override fun doInBackground(vararg params: ParcelFileDescriptor): ConversationData {
        return openFile(params[0])
    }

    override fun onPostExecute(o: ConversationData?) {
        super.onPostExecute(o)
        if (o != null) {
            myAct.findViewById<View>(R.id.saved_analysis_list).visibility = View.VISIBLE
            myAct.findViewById<View>(R.id.loadingPanel).visibility = View.GONE
            Constants.conversationData = o
            val intent = Intent(myAct, ResultsActivity::class.java)
            myAct.startActivity(intent)
        }
    }

    private fun openFile(arg: ParcelFileDescriptor): ConversationData {
        val fd = arg.fileDescriptor
        var bf: BufferedReader? = null
        try {
            bf = BufferedReader(InputStreamReader(FileInputStream(fd), "utf-8"))
        } catch (e: Exception) {
            System.err.println(e)
        }

        val analyzer = LineAnalyzer()
        val data = ConversationData(fileName)

        try {
            var line: String? = bf!!.readLine()
            while (line != null) {
                val a = analyzer.analyzeLine(line)
                if (a != null)
                    data.addData(a)
                line = bf.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        data.createTotalDaysData()
        data.createMonthsData()
        return data
    }
}