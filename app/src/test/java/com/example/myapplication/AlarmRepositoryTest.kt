package com.example.myapplication

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.Calendar

/**
 * Unit tests for the AlarmRepository class.
 * These tests mock the AlarmPreferences class to ensure that AlarmRepository
 * interacts with AlarmPreferences correctly and performs the expected actions.
 */
class AlarmRepositoryTest {

    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var alarmRepository: AlarmRepository

    /**
     * Sets up the test environment by initializing the mocks and creating
     * an instance of AlarmRepository with the mocked AlarmPreferences.
     */
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        alarmPreferences = mock(AlarmPreferences::class.java)
        //alarmRepository = AlarmRepository(alarmPreferences)
    }

    /**
     * Test for the getAlarms() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the loading of alarms
     * to AlarmPreferences and returns the expected result.
     */
    @Test
    fun testGetAlarms() {
        // Create a mock list of alarms
        val mockAlarms = mutableListOf(
            Alarm(1, "07:00", "", "Una vez", true, Calendar.getInstance()),
            Alarm(2, "18:00", "Siesta", "Diaria", false, Calendar.getInstance())
        )

        `when`(alarmPreferences.loadAlarms()).thenReturn(mockAlarms)
        val alarms = alarmRepository.getAlarms()

        // Verify that the returned alarms are the same as the mockAlarms
        assert(alarms == mockAlarms)
        // Verify that the loadAlarms method in AlarmPreferences was called
        verify(alarmPreferences).loadAlarms()
    }

    /**
     * Test for the saveAlarm() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the saving of an alarm
     * to AlarmPreferences.
     */
    @Test
    fun testSaveAlarm() {
        val newAlarm = Alarm(3, "10:00", "", "Domingo", true, Calendar.getInstance())
        alarmRepository.saveAlarm(newAlarm)

        // Verify that the saveAlarm method in AlarmPreferences was called with the correct alarm
        verify(alarmPreferences).saveAlarm(newAlarm)
    }

    /**
     * Test for the getNewAlarmId() method in AlarmRepository.
     * Verifies that AlarmRepository correctly calculates a new alarm ID based on
     * the size of the existing alarm list.
     */
    @Test
    fun testGetNewAlarmId() {
        `when`(alarmPreferences.loadAlarms()).thenReturn(mutableListOf(
            Alarm(1, "07:00", "", "Una vez", true, Calendar.getInstance()),
            Alarm(2, "18:00", "Siesta", "Diaria", false, Calendar.getInstance())
        ))

        val newAlarmId = alarmRepository.getNewAlarmId()

        // Verify that the new ID is 3
        assert(newAlarmId == 3)

        // Verify that the loadAlarms method in AlarmPreferences was called
        verify(alarmPreferences).loadAlarms()
    }
}
