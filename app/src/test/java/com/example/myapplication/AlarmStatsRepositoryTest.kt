package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.model.AlarmStatistic
import com.example.myapplication.repository.AlarmStatsRepository
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AlarmStatsRepositoryTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var alarmStatsRepository: AlarmStatsRepository
    private val gson = Gson()

    @Before
    fun setup() {
        // Mock SharedPreferences and its editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        // Configure mock behavior for SharedPreferences and editor
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Initialize AlarmStatsRepository with mocked SharedPreferences
        alarmStatsRepository = AlarmStatsRepository(sharedPreferences)
    }

    @Test
    fun testSaveStatistic() {
        val statistic = AlarmStatistic("1", "08:00", 5000, 2)

        `when`(sharedPreferences.getString("statistics", null)).thenReturn(null)

        alarmStatsRepository.saveStatistic(statistic)

        verify(editor).putString(eq("statistics"), anyString())
        verify(editor).apply()
    }

    @Test
    fun testGetAllStatistics_NoData_ReturnsEmptyList() {
        `when`(sharedPreferences.getString("statistics", null)).thenReturn(null)

        val result = alarmStatsRepository.getAllStatistics()

        assertTrue(result.isEmpty())
    }

    @Test
    fun testGetAllStatistics_WithData_ReturnsDeserializedList() {
        val statistics = listOf(
            AlarmStatistic("1", "08:00", 5000, 2),
            AlarmStatistic("2", "09:00", 6000, 1)
        )
        `when`(sharedPreferences.getString("statistics", null))
            .thenReturn(gson.toJson(statistics))

        val result = alarmStatsRepository.getAllStatistics()

        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals(5000, result[0].timeToTurnOff)
    }

    @Test
    fun testHasStatisticsInRange_True() {
        val statistics = listOf(
            AlarmStatistic("1", "08:00", 5000, 2),
            AlarmStatistic("2", "09:00", 6000, 1)
        )
        `when`(sharedPreferences.getString("statistics", null))
            .thenReturn(gson.toJson(statistics))

        val result = alarmStatsRepository.hasStatisticsInRange(7, 9)

        assertTrue(result)
    }

    @Test
    fun testHasStatisticsInRange_False() {
        val statistics = listOf(
            AlarmStatistic("1", "22:00", 5000, 2)
        )
        `when`(sharedPreferences.getString("statistics", null))
            .thenReturn(gson.toJson(statistics))

        val result = alarmStatsRepository.hasStatisticsInRange(10, 12)

        assertFalse(result)
    }

    @Test
    fun testGetAverageTimeInRange() {
        val statistics = listOf(
            AlarmStatistic("1", "08:00", 5000, 2),
            AlarmStatistic("2", "09:00", 10000, 1)
        )
        `when`(sharedPreferences.getString("statistics", null))
            .thenReturn(gson.toJson(statistics))

        val result = alarmStatsRepository.getAverageTimeInRange(7, 10)

        assertEquals(7.5, result, 0.1) // Average of 5 and 10 seconds
    }

    @Test
    fun testGetMinTimeInRange() {
        val statistics = listOf(
            AlarmStatistic("1", "08:00", 5000, 2),
            AlarmStatistic("2", "09:00", 10000, 1)
        )
        `when`(sharedPreferences.getString("statistics", null))
            .thenReturn(gson.toJson(statistics))

        val result = alarmStatsRepository.getMinTimeInRange(7, 10)

        assertEquals(5.0, result, 0.1) // Minimum is 5 seconds
    }

    @Test
    fun testGetMaxTimeInRange() {
        val statistics = listOf(
            AlarmStatistic("1", "08:00", 5000, 2),
            AlarmStatistic("2", "09:00", 10000, 1)
        )
        `when`(sharedPreferences.getString("statistics", null))
            .thenReturn(gson.toJson(statistics))

        val result = alarmStatsRepository.getMaxTimeInRange(7, 10)

        assertEquals(10.0, result, 0.1) // Maximum is 10 seconds
    }

    @Test
    fun testGetMaxErrorsInRange() {
        val statistics = listOf(
            AlarmStatistic("1", "08:00", 5000, 2),
            AlarmStatistic("2", "09:00", 10000, 5)
        )
        `when`(sharedPreferences.getString("statistics", null))
            .thenReturn(gson.toJson(statistics))

        val result = alarmStatsRepository.getMaxErrorsInRange(7, 10)

        assertEquals(5.0f, result) // Maximum is 5 errors
    }
}
