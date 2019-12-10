package me.jacobrr.whatsappanalyzer.tasks

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.view.View

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

/**
 * Created by jacob on 24/11/2015.
 */
class ShareScreenshotTask(private val myAct: Activity) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void): String {
        return saveBitmap()
    }

    override fun onPostExecute(aVoid: String) {
        super.onPostExecute(aVoid)
        sendIntent(aVoid)
    }

    fun saveBitmap(): String {
        val myBitmap: Bitmap
        val v1 = myAct.findViewById<View>(android.R.id.content) //this works too
        v1.isDrawingCacheEnabled = true
        myBitmap = v1.drawingCache
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        val dir = File(Environment.getExternalStorageDirectory().toString()
                + File.separator + "WhatsAppAnalyzer")
        if (!dir.exists())
            dir.mkdir()
        val filePath = (Environment.getExternalStorageDirectory().toString()
                + File.separator + "WhatsAppAnalyzer" + File.separator + now + ".png")
        val imagePath = File(filePath)
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(imagePath)
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e("GREC", e.message, e)
        } catch (e: IOException) {
            Log.e("GREC", e.message, e)
        }

        v1.isDrawingCacheEnabled = false
        return filePath
    }

    fun sendIntent(path: String) {
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Generated with Whatsapp Analyzer")
        intent.type = "image/png"
        val myUri = Uri.parse("file://$path")
        intent.putExtra(Intent.EXTRA_STREAM, myUri)
        myAct.startActivity(Intent.createChooser(intent, "Send image"))
    }
}
