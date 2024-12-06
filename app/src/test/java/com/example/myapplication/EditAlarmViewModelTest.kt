package com.example.myapplication

import android.content.SharedPreferences
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.viewmodel.EditAlarmViewModel
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.Calendar

class EditAlarmViewModelTest {

    private lateinit var viewModel: EditAlarmViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var alarmPreferences: AlarmPreferences
    private val gson = Gson()

    @Before
    fun setUp() {
        // Mockear SharedPreferences y su Editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        // Configurar comportamiento de SharedPreferences
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Crear una instancia real de AlarmPreferences con los mocks
        alarmPreferences = AlarmPreferences(sharedPreferences)

        // Inicializar el ViewModel con la instancia de AlarmPreferences
        viewModel = EditAlarmViewModel(alarmPreferences)
    }

    @Test
    fun `loadAlarm should set alarm correctly from preferences`() {
        val testAlarm = Alarm(
            id = 1,
            time = "07:00",
            name = "Morning Alarm",
            periodicity = "Diaria",
            problem = "Sudoku",
            isActive = true,
            ringTime = Calendar.getInstance()
        )
        val alarmJson = gson.toJson(testAlarm)

        `when`(sharedPreferences.getString(eq("alarms_list"), isNull())).thenReturn("[$alarmJson]")

        viewModel.loadAlarm(1)

        val loadedAlarm = viewModel.alarm
        assert(loadedAlarm != null)
        assert(loadedAlarm?.id == testAlarm.id)
        assert(loadedAlarm?.time == testAlarm.time)
        assert(loadedAlarm?.name == testAlarm.name)
        assert(loadedAlarm?.periodicity == testAlarm.periodicity)
        assert(loadedAlarm?.problem == testAlarm.problem)
        assert(loadedAlarm?.isActive == testAlarm.isActive)
    }

    @Test
    fun `updateAlarm should update alarm properties and save changes`() {
        val existingAlarm = Alarm(
            id = 1,
            time = "07:00",
            name = "Test Alarm",
            periodicity = "Lunes",
            problem = "Sudoku",
            isActive = true,
            ringTime = Calendar.getInstance()
        )
        val alarmJson = gson.toJson(listOf(existingAlarm))
        `when`(sharedPreferences.getString(eq("alarms_list"), isNull())).thenReturn(alarmJson)

        viewModel.loadAlarm(1)

        val updatedName = "Updated Alarm"
        val updatedTime = "08:30"
        val updatedPeriodicity = "Diaria"
        val updatedProblem = "Problema corto"

        viewModel.updateAlarm(updatedName, updatedTime, updatedPeriodicity, updatedProblem)

        val updatedAlarm = viewModel.alarm
        assert(updatedAlarm != null)
        assert(updatedAlarm?.name == updatedName)
        assert(updatedAlarm?.time == updatedTime)
        assert(updatedAlarm?.periodicity == updatedPeriodicity)
        assert(updatedAlarm?.problem == updatedProblem)

        val expectedTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        assert(updatedAlarm?.ringTime?.timeInMillis == expectedTime.timeInMillis)

        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }

    @Test
    fun `deleteAlarm should remove alarm by id`() {
        val testAlarm = Alarm(
            id = 1,
            time = "07:00",
            name = "Test Alarm",
            periodicity = "Diaria",
            problem = "Sudoku",
            isActive = true,
            ringTime = Calendar.getInstance()
        )
        val alarmJson = gson.toJson(listOf(testAlarm))
        `when`(sharedPreferences.getString(eq("alarms_list"), isNull())).thenReturn(alarmJson)

        viewModel.loadAlarm(1)
        viewModel.deleteAlarm()

        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }
}
