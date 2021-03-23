package me.jacobrr.whatsappanalyzer

import me.jacobrr.whatsappanalyzer.logic.LineAnalyzer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.LocalTime

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun timeOfTheDayTest() {
        assertEquals("Afternoon", LineAnalyzer().getTimeOfTheDay(LocalTime.of(12, 0)))
    }

    @Test
    fun analyzedLineTest() {
        val message = "19/01/18 10:36 AM - Jacob Rivera: soccer - Match Thread: Toluca vs São Paulo [Copa Libertadores]"
        val analyzer = LineAnalyzer()
        val line = analyzer.analyzeLine(message)
        assertNotNull(line)
    }
}