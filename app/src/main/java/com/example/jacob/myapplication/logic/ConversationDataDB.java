package com.example.jacob.myapplication.logic;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jacob on 22/11/2015.
 * Sister class of ConversationData, it's used to store part of the data to the local DB
 */
public class ConversationDataDB implements IConversationData {
    //<Name, <messages, MessageShare, WordsAvg>>
    private int id;
    private Map<String, Triplet> participants;
    private TupleB mostTalkedDay;
    private TupleA mostTalkedMonth;
    private int totalMessages;
    private int totalDaysTalked;
    private float dailyAvg;
    private float realDailyAvg;
    private String conversationName;

    public ConversationDataDB(int id, float dailyAvg, String mostTalkedDay, String mostTalkedMonth, String participants, float realDailyAvg, int totalDaysTalked, int totalMessages, String conversationName) {
        this.id = id;
        this.dailyAvg = dailyAvg;
        this.mostTalkedDay = new TupleB(mostTalkedDay);
        this.mostTalkedMonth = new TupleA(mostTalkedMonth);
        Map<String,Triplet> a = new HashMap<String,Triplet>();
        String[] parts = participants.split("\\|");
        for (String part : parts) {
            if (!part.equals("")) {
                String[] minipart = part.split("\\-");
                a.put(minipart[0], new Triplet(minipart[1]));
            }
        }
        this.participants = a;
        this.conversationName = conversationName;
        this.realDailyAvg = realDailyAvg;
        this.totalDaysTalked = totalDaysTalked;
        this.totalMessages = totalMessages;
    }

    public ConversationDataDB(IConversationData cv){
        this.mostTalkedDay = new TupleB(cv.getMostTalkedDay(),cv.getDayData(cv.getMostTalkedDay()));
        this.mostTalkedMonth = new TupleA(cv.getMostTalkedMonth(),cv.getMonthData(cv.getMostTalkedMonth()));
        HashMap<String,Triplet> n = new HashMap<String,Triplet>();
        for (String s : cv.getParticipants()) {
            n.put(s, new Triplet(cv.getParticipantCount(s), cv.getParticipantShare(s), cv.getWordsAvg(s)));
        }
        this.participants = n;
        this.totalMessages = cv.getTotalMessages();
        this.totalDaysTalked = cv.getTotalDaysTalked();
        this.dailyAvg = cv.getDailyAvg();
        this.realDailyAvg = cv.getRealDailyAvg();
        this.conversationName = cv.getConversationName();
    }

    @Override
    public List<String> getParticipants() {
        return new ArrayList<String>(participants.keySet());
    }

    @Override
    public int getParticipantCount(String pt) {
        return participants.get(pt).x;
    }

    @Override
    public float getParticipantShare(String pt){
        return participants.get(pt).y;
    }

    @Override
    public float getWordsAvg(String pt){
        return participants.get(pt).z;
    }

    @Override
    public int getTotalDaysTalked() {
        return totalDaysTalked;
    }

    @Override
    public int getTotalMessages() {
        return totalMessages;
    }

    @Override
    public float getDailyAvg() {
        return dailyAvg;
    }

    @Override
    public float getRealDailyAvg() {
        return realDailyAvg;
    }

    @Override
    public String getMostTalkedMonth() {
        return mostTalkedMonth.x;
    }

    @Override
    public Date getMostTalkedDay() {
        return mostTalkedDay.x;
    }

    @Override
    public int getMonthData(String month) {
        return mostTalkedMonth.y;
    }

    @Override
    public String getConversationName() {
        return conversationName;
    }

    @Override
    public int getDayData(Date date) {
        return mostTalkedDay.y;
    }

    public String getMonthDB(){
        return mostTalkedMonth.toString();
    }

    public String getDayDB(){
        return mostTalkedDay.toString();
    }

    public String getParticipantsDB(){
        String res = "";
        for (String s : participants.keySet()) {
            res = res + "|"+ s + "-" + participants.get(s).toString();
        }
        return res;
    }
}

class TupleA implements Serializable{
    public String x;
    public int y;

    public TupleA(String s){
        String[] parts = s.split(",");
        this.x = parts[0];
        this.y = Integer.parseInt(parts[1]);
    }

    public TupleA(String x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return  x +"," + y ;
    }
}

class TupleB implements Serializable{
    public Date x;
    public int y;
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public TupleB(String s) {
        String[] parts = s.split(",");
        try {
            this.x = sdf.parse(parts[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.y = Integer.parseInt(parts[1]);
    }

    public TupleB(Date x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return  sdf.format(x) +"," + y ;
    }
}

class Triplet implements Serializable{
    public final int x;
    public final float y;
    public final float z;
    public Triplet(int x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Triplet(String s){
        String[] parts = s.split(",");
        this.x = Integer.parseInt(parts[0]);
        this.y = Float.parseFloat(parts[1]);
        this.z = Float.parseFloat(parts[2]);
    }

    @Override
    public String toString() {
        return  x + "," + y + "," + z;
    }
}