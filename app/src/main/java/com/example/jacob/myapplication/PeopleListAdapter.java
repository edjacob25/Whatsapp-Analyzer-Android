package com.example.jacob.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.jacob.myapplication.logic.IConversationData;

/**
 * Created by jacob on 21/11/2015.
 */
public class PeopleListAdapter extends BaseAdapter {

    private IConversationData cv;
    private LayoutInflater inflater;

    public PeopleListAdapter(Context context, IConversationData cv) {
        this.cv = cv;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cv.getParticipants().size();
    }

    @Override
    public Object getItem(int position) {
        return cv.getParticipants().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.people_list_item, null);
        TextView tName = (TextView) convertView.findViewById(R.id.people_name);
        TextView tPercentage = (TextView) convertView.findViewById(R.id.people_percentage);
        TextView tWords = (TextView) convertView.findViewById(R.id.words_per_message);
        String name = cv.getParticipants().get(position);
        tName.setText(name);
        String percentage = String.format("%.2f%%", cv.getParticipantShare(name));
        tPercentage.setText(percentage);
        tWords.setText(String.format("%.2f", cv.getWordsAvg(name)));

        return convertView;
    }
}
