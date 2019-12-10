package me.jacobrr.whatsappanalyzer.tasks

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.view.drawToBitmap
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * Created by jacob on 24/11/2015.
 */
class ShareScreenshotTask(private val myAct: Activity) : AsyncTask<Void, Void, Uri>() {

    override fun doInBackground(vararg params: Void): Uri {
        return saveBitmap()
    }

    override fun onPostExecute(aVoid: Uri) {
        super.onPostExecute(aVoid)
        sendIntent(aVoid)
    }

    private fun saveBitmap(): Uri {
        val v1 = myAct.findViewById<View>(android.R.id.content) //this works too
        val myBitmap = v1.drawToBitmap()
        val now = Date()
        val name = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        val resolver = myAct.applicationContext.contentResolver

        val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val imageData = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Screenshots/")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = resolver.insert(imageCollection, imageData)

        try {
            resolver.openOutputStream(uri!!).use { out ->
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            imageData.clear()
            imageData.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, imageData, null, null)
        } catch (e: FileNotFoundException) {
            Log.e("GREC", e.message, e)
        } catch (e: IOException) {
            Log.e("GREC", e.message, e)
        }
        return uri!!
    }

    fun sendIntent(path: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,
                "Generated with Whatsapp Analyzer")
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, path)
        myAct.startActivity(Intent.createChooser(intent, "Send image"))
    }
}
