package com.example.jacob.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jacob.myapplication.Constants;
import com.example.jacob.myapplication.R;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Log.i("Persona", Constants.person.toString());
        TextView aggrTV = (TextView) findViewById(R.id.person_view_aggressiveness);
        aggrTV.setText(Constants.person.getAggresiveness() * 100 + "%");
        TextView happTV = (TextView) findViewById(R.id.person_view_happiness);
        happTV.setText(Constants.person.getHappiness()*100 + "%");
        TextView fearTV = (TextView) findViewById(R.id.person_view_fear);
        fearTV.setText(Constants.person.getFear()*100 + "%");
        TextView loveTV = (TextView) findViewById(R.id.person_view_love);
        loveTV.setText(Constants.person.getLove()*100 + "%");
        TextView sadTV = (TextView) findViewById(R.id.person_view_sadness);
        sadTV.setText(Constants.person.getSadness()*100 + "%");
    }
}
