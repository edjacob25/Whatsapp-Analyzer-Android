package com.example.jacob.myapplication;

import com.example.jacob.myapplication.logic.LineAnalyzer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals("afternoon", new LineAnalyzer().getTimeOfTheDay(12));
    }
}