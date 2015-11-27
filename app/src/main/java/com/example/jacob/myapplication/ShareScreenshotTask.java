package com.example.jacob.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by jacob on 24/11/2015.
 */
public class ShareScreenshotTask extends AsyncTask<Void, Void, String>{

    private Activity myAct;
    public ShareScreenshotTask(Activity myAct) {
        this.myAct = myAct;
    }

    @Override
    protected String doInBackground(Void... params) {
        return saveBitmap();
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        sendIntent(aVoid);
    }

    public String saveBitmap() {
        Bitmap myBitmap;
        View v1 = myAct.findViewById(android.R.id.content); //this works too
        v1.setDrawingCacheEnabled(true);
        myBitmap = v1.getDrawingCache();
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator + "WhatsAppAnalyzer");
        if (!dir.exists())
            dir.mkdir();
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "WhatsAppAnalyzer" + File.separator + now +  ".png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        v1.setDrawingCacheEnabled(false);
        return filePath;
    }

    public void sendIntent(String path) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Generated with Whatsapp Analyzer");
        intent.setType("image/png");
        Uri myUri = Uri.parse("file://" + path);
        intent.putExtra(Intent.EXTRA_STREAM, myUri);
        myAct.startActivity(Intent.createChooser(intent, "Send image"));
    }
}
