package me.jacobrr.whatsappanalyzer

import me.jacobrr.whatsappanalyzer.logic.LineAnalyzer

import org.junit.Test

import org.junit.Assert.*

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        assertEquals("Afternoon", LineAnalyzer().getTimeOfTheDay(12))
    }
}