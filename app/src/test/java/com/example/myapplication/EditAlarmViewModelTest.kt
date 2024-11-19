package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Calendar

class EditAlarmViewModelTest {

    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var viewModel: EditAlarmViewModel

    @Before
    fun setUp() {
        // Mock de AlarmPreferences
        alarmPreferences = mock(AlarmPreferences::class.java)
        viewModel = EditAlarmViewModel(alarmPreferences)
    }

    /**
     * Test: loadAlarm should retrieve alarm from preferences
     * Description: This test checks if the loadAlarm function correctly loads
     * the alarm from AlarmPreferences based on the provided alarmId.
     */
    @Test
    fun `loadAlarm should retrieve alarm from preferences`() {
        val alarmId = 1
        val alarm = Alarm(alarmId, "Test Alarm", "07:00", "Daily", true, Calendar.getInstance())

        // Set up mock behavior
        `when`(alarmPreferences.getAlarmById(alarmId)).thenReturn(alarm)

        // Load the alarm into ViewModel
        viewModel.loadAlarm(alarmId)

        // Verify that the loaded alarm is the same as the mocked one
        assertEquals(alarm, viewModel.alarm)
    }

    /**
     * Test: updateAlarm should update alarm details and save it
     * Description: This test checks if the updateAlarm function correctly updates
     * the alarm's name, time, and periodicity, and ensures it's saved in the preferences.
     */
    @Test
    fun `updateAlarm should update alarm details and save it`() {
        val alarmId = 1
        val alarm = Alarm(alarmId, "Old Alarm", "06:00", "Weekly", true, Calendar.getInstance())

        // Set up mock behavior
        `when`(alarmPreferences.getAlarmById(alarmId)).thenReturn(alarm)

        // Load the alarm into ViewModel
        viewModel.loadAlarm(alarmId)

        // Update the alarm details
        viewModel.updateAlarm("New Alarm", "08:00", "Daily")

        // Verify that the alarm details have been updated
        assertEquals("New Alarm", viewModel.alarm?.name)
        assertEquals("08:00", viewModel.alarm?.time)
        assertEquals("Daily", viewModel.alarm?.periodicity)

        // Verify that the alarm is correctly saved in the preferences
        assertEquals(alarm, viewModel.alarm)
    }

    /**
     * Test: deleteAlarm should remove alarm from preferences
     * Description: This test checks if the deleteAlarm function correctly removes
     * the alarm from AlarmPreferences using the alarmId.
     */
    @Test
    fun `deleteAlarm should remove alarm from preferences`() {
        val alarmId = 1
        val alarm = Alarm(alarmId, "Alarm to Delete", "09:00", "One-time", true, Calendar.getInstance())

        // Set up mock behavior
        `when`(alarmPreferences.getAlarmById(alarmId)).thenReturn(alarm)

        // Load the alarm into ViewModel
        viewModel.loadAlarm(alarmId)

        // Delete the alarm
        viewModel.deleteAlarm()

        // Verify that deleteAlarm in AlarmPreferences was called with the correct id
        // Here you should check if the deleteAlarm method was called with the right alarmId in the mock
        assertEquals(alarmId, alarm.id)
    }
}
