package com.example.myapplication.model

import org.junit.Assert.*
import org.junit.Test

class AlarmStatisticTest {

    @Test
    fun `test toJson converts object to JSON correctly`() {
        val statistic = AlarmStatistic(
            id = "1234",
            alarmSetTime = "07:30",
            timeToTurnOff = 5000L,
            failures = 2
        )

        val json = AlarmStatistic.toJson(statistic)

        val expectedJson = """{"id":"1234","alarmSetTime":"07:30","timeToTurnOff":5000,"failures":2}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun `test fromJson converts JSON to object correctly`() {
        val json = """{"id":"1234","alarmSetTime":"07:30","timeToTurnOff":5000,"failures":2}"""

        val statistic = AlarmStatistic.fromJson(json)

        assertNotNull(statistic)
        assertEquals("1234", statistic.id)
        assertEquals("07:30", statistic.alarmSetTime)
        assertEquals(5000L, statistic.timeToTurnOff)
        assertEquals(2, statistic.failures)
    }

    @Test
    fun `test toJson and fromJson are consistent`() {
        val statistic = AlarmStatistic(
            id = "5678",
            alarmSetTime = "08:15",
            timeToTurnOff = 10000L,
            failures = 1
        )

        val json = AlarmStatistic.toJson(statistic)
        val recreatedStatistic = AlarmStatistic.fromJson(json)

        assertEquals(statistic, recreatedStatistic)
    }
}
