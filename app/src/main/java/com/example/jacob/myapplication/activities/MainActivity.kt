package com.example.jacob.myapplication.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.example.jacob.myapplication.Constants
import com.example.jacob.myapplication.R
import com.example.jacob.myapplication.db.DataDBHandler
import com.example.jacob.myapplication.db.SavedDataAdapter
import com.example.jacob.myapplication.logic.IConversationData
import com.example.jacob.myapplication.tasks.CreateDataTask
import com.nononsenseapps.filepicker.FilePickerActivity
import com.nononsenseapps.filepicker.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Constants.dbHandler = DataDBHandler(this)

        val sva = SavedDataAdapter(this, Constants.dbHandler.allData)
        saved_analysis_list.adapter = sva

        saved_analysis_list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, _ ->
            Constants.conversationData = parent.getItemAtPosition(position) as IConversationData
            val intent = Intent(view.context, ResultsActivity::class.java)
            startActivity(intent)
        }

        if (intent.action == Intent.ACTION_VIEW) {
            val returnUri = intent.data
            var mInputPFD: ParcelFileDescriptor? = null
            try {
                mInputPFD = contentResolver.openFileDescriptor(returnUri!!, "r")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.e("MainActivity", "File not found.")
            }

            CreateDataTask(this, returnUri!!.lastPathSegment).execute(mInputPFD)
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

    fun buttonClick(view: View) {

        val context = applicationContext
        // This always works
        val i = Intent(context, MyPickerActivity::class.java)
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

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
        startActivityForResult(i, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (resultCode == Activity.RESULT_OK) {
            val files = Utils.getSelectedFilesFromResult(data)
            val uri = files[0]
            var mInputPFD: ParcelFileDescriptor? = null
            try {
                mInputPFD = contentResolver.openFileDescriptor(uri, "r")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.e("MainActivity", "File not found.")
            }

            CreateDataTask(this, uri.lastPathSegment).execute(mInputPFD)
        }
    }
}
