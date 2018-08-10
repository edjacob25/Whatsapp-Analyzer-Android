package com.example.jacob.myapplication.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jacob on 20/11/2015.
 */
public class LineAnalyzer {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    public String getTimeOfTheDay(int hour) {
        String s = "";
        if (hour >= 1 && hour < 6)
            s = "Early morning";
        else if (hour >= 6 && hour < 12)
            s = "Morning";
        else if (hour >= 12 && hour < 19)
            s = "Afternoon";
        else
            s = "Night";

        return s;
    }

    public AnalyzedLine analyzeLine(String line) {
        try {

            String[] firstDivision = line.split("-");
            String[] dateDivision = firstDivision[0].split(" ");
            String[] textDivision = firstDivision[1].split(":");
            String name = textDivision[0].substring(1, textDivision[0].length());

            String message = textDivision[1];
            Date date = sdf.parse(dateDivision[0]);
            int hour = Integer.parseInt(dateDivision[1].split(":")[0]);


            return new AnalyzedLine(name, message, date, getTimeOfTheDay(hour));

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error: " + line);
            return null;
        }
    }
}
