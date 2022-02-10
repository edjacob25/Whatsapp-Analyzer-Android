package me.jacobrr.whatsappanalyzer.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nononsenseapps.filepicker.FilePickerActivity
import com.nononsenseapps.filepicker.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.R
import me.jacobrr.whatsappanalyzer.databinding.ActivityMainBinding
import me.jacobrr.whatsappanalyzer.db.DataDBHandler
import me.jacobrr.whatsappanalyzer.db.SavedDataAdapter
import me.jacobrr.whatsappanalyzer.logic.ConversationData
import me.jacobrr.whatsappanalyzer.logic.LineAnalyzer
import me.jacobrr.whatsappanalyzer.util.executeAsyncTask
import java.io.*
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        Constants.dbHandler = DataDBHandler(this)
        val sva = SavedDataAdapter(Constants.dbHandler!!.allData)
        val viewManager = LinearLayoutManager(applicationContext)
        binding.content.savedAnalysisList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = sva
        }

        binding.fab.setOnClickListener(clickListener)

        if (intent.action == Intent.ACTION_VIEW) {
            val returnUri = intent.data
            createDataInBackground(returnUri!!)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    var launchActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            val files = Utils.getSelectedFilesFromResult(result.data!!)
            val uri = files[0]
            createDataInBackground(uri)
        }
    }

    private fun createDataInBackground(uri: Uri) {
        try {
            val mInputPFD = contentResolver.openFileDescriptor(uri, "r")
            CoroutineScope(Dispatchers.Main).executeAsyncTask(
                    onPreExecute = {
                        Log.d("Timings", "Start ${LocalDateTime.now()}")
                        binding.content.savedAnalysisList.visibility = View.INVISIBLE
                        binding.content.loadingPanel.visibility = View.VISIBLE
                    },
                    doInBackground = {
                        Log.d("Timings", "Background ${LocalDateTime.now()}")
                        val fd = mInputPFD?.fileDescriptor
                        var bf: BufferedReader? = null
                        try {
                            bf = BufferedReader(InputStreamReader(FileInputStream(fd), "utf-8"))
                        } catch (e: Exception) {
                            System.err.println(e)
                        }
                        val analyzer = LineAnalyzer()
                        val data = ConversationData(uri.lastPathSegment!!)

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
                        data
                    },
                    onPostExecute = {
                        Log.d("Timings", "Post ${LocalDateTime.now()}")
                        binding.content.savedAnalysisList.visibility = View.VISIBLE
                        binding.content.loadingPanel.visibility = View.GONE
                        Constants.conversationData = it
                        val intent = Intent(applicationContext, ResultsActivity::class.java)
                        startActivity(intent)
                    }
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("MainActivity", "File not found.")
        }
    }

    private val clickListener = View.OnClickListener {
        val context = applicationContext
        // This always works
        val i = Intent(context, MyPickerActivity::class.java)

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false)
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE)
        i.putExtra(FilePickerActivity.EXTRA_SINGLE_CLICK, false)
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_EXISTING_FILE, true)

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().path)
        launchActivity.launch(i)
    }
}
