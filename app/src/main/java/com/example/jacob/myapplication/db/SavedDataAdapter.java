package com.example.jacob.myapplication.db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jacob.myapplication.logic.ConversationDataDB;
import com.example.jacob.myapplication.R;

import java.util.ArrayList;

/**
 * Created by jacob on 19/11/15.
 */
public class SavedDataAdapter extends BaseAdapter {
    private ArrayList<ConversationDataDB> conversations = new ArrayList<ConversationDataDB>();
    private LayoutInflater inflater;

    public SavedDataAdapter(Context context, Cursor c) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (c.moveToFirst()) {
            do {
                ConversationDataDB conver = new ConversationDataDB(
                        c.getInt(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_ID)),
                        c.getFloat(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_DAILY_AVG)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TDAY)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_MOST_TMON)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_PARTICIPANTS)),
                        c.getFloat(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_REAL_DAILY_AVG)),
                        c.getInt(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_DAYS)),
                        c.getInt(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_TOTAL_MSGS)),
                        c.getString(c.getColumnIndex(DataReaderContract.DataEntry.COLUMN_NAME_NAME))
                );

                conversations.add(conver);

            } while (c.moveToNext());
        }

    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public ConversationDataDB getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.conversation_list_item, null);
        TextView conversationName = (TextView) convertView.findViewById(R.id.conversation_item_name);
        conversationName.setText(conversations.get(position).getConversationName());
        TextView conversationMessages = (TextView) convertView.findViewById(R.id.conversation_item_msg);
        String msgs = conversations.get(position).getTotalMessages() + " messages";
        conversationMessages.setText(msgs);
        return convertView;
    }
}