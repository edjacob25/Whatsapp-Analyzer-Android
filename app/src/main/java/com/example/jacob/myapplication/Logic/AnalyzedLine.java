package com.example.jacob.myapplication.Logic;

import java.util.Date;

/**
 * Created by jacob on 22/11/2015.
 */
public class AnalyzedLine {
    private String participant;
    private String message;
    private Date date;
    private String hours;

    public AnalyzedLine(String participant, String message, Date date, String hours) {
        this.participant = participant;
        this.message = message;
        this.date = date;
        this.hours = hours;
    }

    public String getParticipant() {
        return participant;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getHours() {
        return hours;
    }

    @Override

    public String toString() {
        return "LineAnalyzed{" +
                "participant='" + participant + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", hours='" + hours + '\'' +
                '}';
    }
}
