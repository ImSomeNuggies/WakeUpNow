package com.example.myapplication

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import com.example.myapplication.receivers.AlarmReceiver
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
        val mockAlarms = mutableListOf(
            Alarm(1, "07:00", "", "Una vez", true, Calendar.getInstance()),
            Alarm(2, "18:00", "Siesta", "Diaria", false, Calendar.getInstance())
        )

        `when`(alarmPreferences.loadAlarms()).thenReturn(mockAlarms)
        val alarms = alarmRepository.getAlarms()

        assert(alarms == mockAlarms)
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

        verify(alarmPreferences).saveAlarm(newAlarm)
    }

    /**
    * Test for the getAlarmById() method in AlarmRepository.
    * Verifies that AlarmRepository retrieves the correct alarm by ID from
    * AlarmPreferences when the alarm is found.
    */
    @Test
    fun testGetAlarmById_found() {
        val mockAlarm = Alarm(1, "07:00", "Morning Alarm", "Diaria", true, Calendar.getInstance())
        `when`(alarmPreferences.getAlarmById(1)).thenReturn(mockAlarm)

        val alarm = alarmRepository.getAlarmById(1)

        assert(alarm == mockAlarm)
        verify(alarmPreferences).getAlarmById(1)
    }

    /**
    * Test for the getAlarmById() method in AlarmRepository.
    * Verifies that AlarmRepository returns null if the alarm with the specified ID
    * does not exist in AlarmPreferences.
    */
    @Test
    fun testGetAlarmById_notFound() {
        `when`(alarmPreferences.getAlarmById(999)).thenReturn(null)

        val alarm = alarmRepository.getAlarmById(999)

        assert(alarm == null)
        verify(alarmPreferences).getAlarmById(999)
    }


    /**
     * Test for the editAlarm() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the editing of an alarm
     * to AlarmPreferences.
     */
    @Test
    fun testEditAlarm() {
        val editedAlarm = Alarm(2, "09:00", "Updated Alarm", "Lunes", false, Calendar.getInstance())
        alarmRepository.editAlarm(editedAlarm)

        verify(alarmPreferences).editAlarm(editedAlarm)
    }

    /**
     * Test for the deleteAlarm() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the deletion of an alarm
     * by its ID to AlarmPreferences.
     */
    @Test
    fun testDeleteAlarm() {
        alarmRepository.deleteAlarm(2)

        verify(alarmPreferences).deleteAlarm(2)
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

        assert(newAlarmId == 3)
        verify(alarmPreferences).loadAlarms()
    }
}
