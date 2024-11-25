package com.example.myapplication

import com.example.myapplication.Alarm
import com.example.myapplication.AlarmPreferences
import com.example.myapplication.EditAlarmViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.Calendar

class EditAlarmViewModelTest {

    private lateinit var viewModel: EditAlarmViewModel

    @Mock
    private lateinit var alarmPreferences: AlarmPreferences

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = EditAlarmViewModel(alarmPreferences)
    }

    @Test
    fun `loadAlarm should set alarm correctly from preferences`() {
        val testAlarm = Alarm(1, "07:00", "Morning Alarm", "Diaria", true, Calendar.getInstance())
        `when`(alarmPreferences.getAlarmById(1)).thenReturn(testAlarm)

        viewModel.loadAlarm(1)

        assert(viewModel.alarm == testAlarm)
        verify(alarmPreferences).getAlarmById(1)
    }

    @Test
    fun `updateAlarm should update alarm properties and save changes`() {
        val existingAlarm = Alarm(1, "07:00", "Alarm", "Lunes", true, Calendar.getInstance())
        viewModel.alarm = existingAlarm

        viewModel.updateAlarm("Updated Alarm", "08:30", "Diaria")

        assert(viewModel.alarm?.name == "Updated Alarm")
        assert(viewModel.alarm?.time == "08:30")
        assert(viewModel.alarm?.periodicity == "Diaria")

        val expectedTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        assert(viewModel.alarm?.ringTime?.timeInMillis == expectedTime.timeInMillis)

        verify(alarmPreferences).editAlarm(existingAlarm)
    }

    @Test
    fun `deleteAlarm should remove alarm by id`() {
        val testAlarm = Alarm(1, "07:00", "Test Alarm", "Diaria", true, Calendar.getInstance())
        viewModel.alarm = testAlarm

        viewModel.deleteAlarm()

        verify(alarmPreferences).deleteAlarm(testAlarm.id)
    }
}
