package com.example.jacob.myapplication.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jacob.myapplication.Constants;
import com.example.jacob.myapplication.CreateDataTask;
import com.example.jacob.myapplication.DB.DataDBHandler;
import com.example.jacob.myapplication.DB.SavedDataAdapter;
import com.example.jacob.myapplication.Logic.IConversationData;
import com.example.jacob.myapplication.R;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Constants.dbHandler = new DataDBHandler(this);
        SavedDataAdapter sva = new SavedDataAdapter(this, Constants.dbHandler.getAllData());
        ListView l = (ListView) findViewById(R.id.saved_analysis_list);
        l.setAdapter(sva);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "item", Toast.LENGTH_SHORT).show();
                Constants.conversationData =(IConversationData) parent.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), ResultsActivity.class);
                startActivity(intent);
            }
        });

        if (getIntent().getAction().equals(Intent.ACTION_VIEW)){
            Uri returnUri = getIntent().getData();
            ParcelFileDescriptor mInputPFD = null;
            try {
                mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity", "File not found.");
            }
            new CreateDataTask(this,returnUri.getLastPathSegment()).execute(mInputPFD);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClick(View view){

        Context context = getApplicationContext();
        // This always works
        Intent i = new Intent(context, MyPickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(MyPickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(MyPickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(MyPickerActivity.EXTRA_MODE, MyPickerActivity.MODE_FILE);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(MyPickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, 1);
    }

    public void goToGraphs(View view){
        Intent intent = new Intent(this, ResultsActivity.class);
        this.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            ParcelFileDescriptor mInputPFD = null;
            try {
                mInputPFD = getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity", "File not found.");
            }
            new CreateDataTask(this, uri.getLastPathSegment()).execute(mInputPFD);
        }
    }
}
