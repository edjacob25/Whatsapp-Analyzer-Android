package com.example.jacob.myapplication.logic;

import java.util.Date;
import java.util.List;

/**
 * Created by jacob on 22/11/2015.
 */
public interface IConversationData {
    List<String> getParticipants();

    Date getMostTalkedDay();

    int getTotalDaysTalked();

    String getMostTalkedMonth();

    int getTotalMessages();

    int getParticipantCount(String pt);

    float getParticipantShare(String pt);

    float getWordsAvg(String pt);

    float getDailyAvg();

    float getRealDailyAvg();

    int getDayData(Date date);

    int getMonthData(String month);

    String getConversationName();
}
