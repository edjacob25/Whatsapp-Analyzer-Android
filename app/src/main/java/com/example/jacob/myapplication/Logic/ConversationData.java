package com.example.jacob.myapplication.Logic;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by jacob on 20/11/2015.
 */
public class ConversationData implements IConversationData {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Map<String, Integer> participants = new HashMap<String, Integer>();
    private Map<String, Integer> participantsWords = new HashMap<String, Integer>();
    //private List<String> messages = new ArrayList<String>();
    private SortedMap<Date,Integer> days = new TreeMap<Date, Integer>();
    private SortedMap<Date,Integer> totalDays = new TreeMap<Date, Integer>();
    private Map<String, Integer> months = new HashMap<String, Integer>();
    private Map<String, Integer> timeofDay = new HashMap<String, Integer>();
    private String conversationName;

    public ConversationData(String conversationName) {
        this.conversationName = conversationName;
    }

    public void addData(String participant, String message, Date date, String time){
        Integer numMess = participants.get(participant);
        participants.put(participant,(numMess == null) ? 1: numMess + 1);

        Integer numDay = days.get(date);
        days.put(date,(numDay == null) ? 1: numDay + 1);

        /* -1 added for the first space at the start */
        Integer wordsAcc = participantsWords.get(participant);
        int words = message.split(" ").length - 1;
        participantsWords.put(participant,(wordsAcc == null) ? words : wordsAcc + words);

        /*if (!message.equals(" <Archivo omitido>"))
            messages.add(message);*/

        Integer timeCount = timeofDay.get(time);
        timeofDay.put(time, (timeCount == null) ? 1 : timeCount + 1);
    }

    public void addData(AnalyzedLine a){
        addData(a.getParticipant(), a.getMessage(), a.getDate(), a.getHours());
    }

    public void createMonthsData() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        Integer numMonth;
        for (Date iterator : days.keySet()){
            numMonth = months.get(sdf.format(iterator));
            months.put(sdf.format(iterator),(numMonth == null) ? days.get(iterator) : numMonth + days.get(iterator));
        }
    }

    public void createTotalDaysData() {
        Date iterator = new Date(days.firstKey().getTime());
        Date last = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(iterator);
        for (; iterator.before(last); calendar.add(Calendar.DAY_OF_YEAR, 1)) {
            iterator = calendar.getTime();
            totalDays.put(iterator, (days.containsKey(iterator)) ? days.get(iterator) : 0);
        }
    }

    @Override
    public List<String> getParticipants(){
        return new ArrayList<String>(participants.keySet());
    }

    @Override
    public Date getMostTalkedDay(){
        int msg = 0;
        Date date = null;
        for (Date iterator : days.keySet()){
            if (days.get(iterator) > msg) {
                msg = days.get(iterator);
                date = iterator;
            }
        }
        return date;
    }

    @Override
    public String getMostTalkedMonth() {
        String mostalkedMonth = "";
        int msg = 0;

        for (String mt : months.keySet()) {
            if (months.get(mt) > msg) {
                msg = months.get(mt);
                mostalkedMonth = mt;
            }
        }
        return mostalkedMonth;
    }

    @Override
    public String getConversationName() {
        return conversationName;
    }

    @Override
    public int getDayData(Date date) {
        return days.get(date);
    }

    @Override
    public int getMonthData(String month) {
        return months.get(month);
    }

    @Override
    public int getTotalMessages(){
        int total = 0;
        for (String iterator : participants.keySet())
            total = total + participants.get(iterator);
        return total;
    }

    @Override
    public int getTotalDaysTalked() {
        return totalDays.size();
    }

    @Override
    public int getParticipantCount(String pt) {
        return participants.get(pt);
    }

    @Override
    public float getParticipantShare(String pt) {
        float avg;
        int tot = getTotalMessages();
        avg =(float) (participants.get(pt)*100.0)/tot;
        return avg;
    }

    @Override
    public float getWordsAvg(String pt) {
        return (float) participantsWords.get(pt) / participants.get(pt);
    }

    @Override
    public float getDailyAvg() {
        return (float) getTotalMessages() / days.size();
    }

    @Override
    public float getRealDailyAvg() {
        return (float) getTotalMessages() / totalDays.size();
    }

   /* public List<String> getMessagesContent(){
        return messages;
    }*/

    public LineData getChartDaysData(){


        int i = 0;
        ArrayList<Entry> vals = new ArrayList<Entry>();
        ArrayList<String> axis = new ArrayList<String>();

        for (Date date : days.keySet()) {
            Entry a = new Entry(days.get(date),i);
            vals.add(a);
            axis.add(sdf.format(date));
            i++;
        }
        LineDataSet dt = new LineDataSet(vals, "Messages");
        dt.setAxisDependency(YAxis.AxisDependency.LEFT);
        return new LineData(axis,dt);
    }

    public LineData getAllDaysChartData(){
        int i = 0;
        ArrayList<Entry> vals = new ArrayList<Entry>();
        ArrayList<String> axis = new ArrayList<String>();

        for (Date date : totalDays.keySet()) {
            Entry a = new Entry(totalDays.get(date),i);
            vals.add(a);
            axis.add(sdf.format(date));
            i++;
        }
        LineDataSet dt = new LineDataSet(vals, "Messages");
        dt.setAxisDependency(YAxis.AxisDependency.LEFT);
        return new LineData(axis,dt);
    }
    /*
    public List<PieChart.Data> getChartParticipantsData(){
        List<PieChart.Data> data = new ArrayList<>();
        for (String s : participants.keySet()) {
            data.add(new PieChart.Data(s, getParticipantShare(s)));
        }
        return data;
    }*/

    public PieData getTimeChartData(){
        int i = 0;
        ArrayList<Entry> vals = new ArrayList<Entry>();
        ArrayList<String> axis = new ArrayList<String>();
        for (String s : timeofDay.keySet()) {
            Entry a = new Entry(timeofDay.get(s),i);
            vals.add(a);
            axis.add(s);
            i++;
        }
        PieDataSet dt = new PieDataSet(vals, "Time of the day");
        dt.setAxisDependency(YAxis.AxisDependency.LEFT);
        return new PieData(axis,dt);
    }
    /*
    public Dataset getDaysDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (Date iterator : days.keySet()) {
            dataset.addValue(days.get(iterator), "Mensajes por dia", sdf.format(iterator));
        }
        return dataset;
    }

    public Dataset getTotalDaysDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (Date iterator : totalDays.keySet()) {
            dataset.addValue(totalDays.get(iterator),"Mensajes por dia",sdf.format(iterator));
        }
        return dataset;
    }*/
}
