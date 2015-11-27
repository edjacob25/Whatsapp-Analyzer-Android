package com.example.jacob.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.view.View;

import com.example.jacob.myapplication.Activities.ResultsActivity;
import com.example.jacob.myapplication.Logic.AnalyzedLine;
import com.example.jacob.myapplication.Logic.ConversationData;
import com.example.jacob.myapplication.Logic.LineAnalyzer;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jacob on 21/11/2015.
 */
public class CreateDataTask extends AsyncTask<ParcelFileDescriptor, Void, ConversationData> {

    Activity myAct;
    String fileName;

    public CreateDataTask(Activity activity, String name){
        myAct = activity;
        fileName = name;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myAct.findViewById(R.id.saved_analysis_list).setVisibility(View.INVISIBLE);
        //Debug.startMethodTracing("analysis");
        myAct.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    protected ConversationData doInBackground(ParcelFileDescriptor...  params) {
        ConversationData cv;
        cv =  openFile(params[0]);
        return cv;
    }

    @Override
    protected void onPostExecute(ConversationData o) {
        super.onPostExecute(o);
        if (o != null){
            myAct.findViewById(R.id.saved_analysis_list).setVisibility(View.VISIBLE);
            myAct.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            Constants.conversationData = o;
            Intent intent = new Intent(myAct, ResultsActivity.class);
            myAct.startActivity(intent);
        }
    }

    private ConversationData openFile(ParcelFileDescriptor arg) {
        FileDescriptor fd = arg.getFileDescriptor();
        BufferedReader bf = null;
        try{
            bf = new BufferedReader (new InputStreamReader(new FileInputStream(fd), "utf-8"));
        }
        catch (Exception e){
            System.err.println(e);
        }
        LineAnalyzer analyzer = new LineAnalyzer();
        ConversationData data = new ConversationData(fileName);
        String line;
        try{
            while ((line = bf.readLine()) != null) {
                AnalyzedLine a = analyzer.analyzeLine(line);
                if (a!= null)
                    data.addData(a);
            }
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        data.createTotalDaysData();
        data.createMonthsData();
        return data;
    }
}