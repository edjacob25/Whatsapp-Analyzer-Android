package com.example.jacob.myapplication.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.jacob.myapplication.Constants;
import com.example.jacob.myapplication.R;
import com.example.jacob.myapplication.activities.PersonActivity;
import com.example.jacob.myapplication.logic.ConversationData;

/**
 * Created by jacob on 27/11/2015.
 */
public class AnalyzePeopleTask extends AsyncTask<Void, Void, Void> {
    private Activity myAct;

    public AnalyzePeopleTask(Activity myAct) {
        this.myAct = myAct;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ((ConversationData) Constants.conversationData).createPeopleData();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ListView l = (ListView) myAct.findViewById(R.id.people_list);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "item", Toast.LENGTH_SHORT).show();

                Constants.person = ((ConversationData) Constants.conversationData).getPersonData(position);
                Intent intent = new Intent(view.getContext(), PersonActivity.class);
                myAct.startActivity(intent);
            }
        });
        Toast.makeText(myAct, "Se hizo", Toast.LENGTH_SHORT).show();
    }
}
