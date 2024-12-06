package com.example.myapplication

import android.content.SharedPreferences
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.repository.AlarmRepository
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

import java.util.Calendar

/**
 * Unit tests for the AlarmRepository class.
 * These tests mock SharedPreferences to ensure that AlarmPreferences
 * interacts with SharedPreferences correctly and performs the expected actions.
 */
class AlarmRepositoryTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var alarmRepository: AlarmRepository
    private val gson = Gson()

    /**
     * Sets up the test environment by initializing mocks and creating
     * instances of AlarmPreferences and AlarmRepository with the mocked SharedPreferences.
     */
    @Before
    fun setUp() {
        // Mock SharedPreferences and its editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        // Configure mock behavior
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Initialize AlarmPreferences with the mocked SharedPreferences
        alarmPreferences = AlarmPreferences(sharedPreferences)
        // Initialize AlarmRepository with AlarmPreferences
        alarmRepository = AlarmRepository(alarmPreferences)
    }

    /**
     * Test for the getAlarms() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the loading of alarms
     * to AlarmPreferences and returns the expected result.
     */
    @Test
    fun testGetAlarms() {
        val mockAlarms = mutableListOf(
            Alarm(1, "07:00", "", "Una vez", "Problema corto", true, Calendar.getInstance()),
            Alarm(2, "18:00", "Siesta", "Diaria", "Problema corto", false, Calendar.getInstance())
        )

        // Mock the stored JSON list of alarms
        val alarmsJson = gson.toJson(mockAlarms)
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmsJson)

        val alarms = alarmRepository.getAlarms()

        // Verify size
        assert(alarms.size == mockAlarms.size)

        // Compare each field of the alarms
        for (i in alarms.indices) {
            val mockAlarm = mockAlarms[i]
            val alarm = alarms[i]

            assert(alarm != null)
            assert(alarm?.id == mockAlarm.id)
            assert(alarm?.time == mockAlarm.time)
            assert(alarm?.name == mockAlarm.name)
            assert(alarm?.periodicity == mockAlarm.periodicity)
            assert(alarm?.problem == mockAlarm.problem)
            assert(alarm?.isActive == mockAlarm.isActive)
        }
        verify(sharedPreferences).getString("alarms_list", null)
    }

    /**
     * Test for the saveAlarm() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the saving of an alarm
     * to AlarmPreferences.
     */
    @Test
    fun testSaveAlarm() {
        val newAlarm = Alarm(3, "10:00", "", "Domingo", "Problema corto", true, Calendar.getInstance())
        alarmRepository.saveAlarm(newAlarm)

        // Verify that the alarm list was saved in SharedPreferences
        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }

    /**
    * Test for the getAlarmById() method in AlarmRepository.
    * Verifies that AlarmRepository retrieves the correct alarm by ID from
    * AlarmPreferences when the alarm is found.
    */
    @Test
    fun testGetAlarmById_found() {
        val mockAlarm = Alarm(1, "07:00", "Morning Alarm", "Diaria", "Problema corto", true, Calendar.getInstance())
        val alarmsJson = gson.toJson(listOf(mockAlarm))
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmsJson)

        val alarm = alarmRepository.getAlarmById(1)

        // Verificar cada propiedad en lugar de comparar referencias
        assert(alarm != null)
        assert(alarm?.id == mockAlarm.id)
        assert(alarm?.time == mockAlarm.time)
        assert(alarm?.name == mockAlarm.name)
        assert(alarm?.periodicity == mockAlarm.periodicity)
        assert(alarm?.problem == mockAlarm.problem)
        assert(alarm?.isActive == mockAlarm.isActive)

        verify(sharedPreferences).getString("alarms_list", null)
    }

    /**
    * Test for the getAlarmById() method in AlarmRepository.
    * Verifies that AlarmRepository returns null if the alarm with the specified ID
    * does not exist in AlarmPreferences.
    */
    @Test
    fun testGetAlarmById_notFound() {
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(null)

        val alarm = alarmRepository.getAlarmById(999)

        assert(alarm == null)
        verify(sharedPreferences).getString("alarms_list", null)
    }

    /**
     * Test for the editAlarm() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the editing of an alarm
     * to AlarmPreferences.
     */
    @Test
    fun testEditAlarm() {
        val originalAlarm = Alarm(2, "09:00", "Original Alarm", "Lunes", "Problema corto", false, Calendar.getInstance())
        val alarmsJson = gson.toJson(listOf(originalAlarm))
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmsJson)

        val editedAlarm = originalAlarm.copy(name = "Updated Alarm")
        alarmRepository.editAlarm(editedAlarm)

        // Verify that the updated alarm list was saved in SharedPreferences
        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }

    /**
     * Test for the deleteAlarm() method in AlarmRepository.
     * Verifies that AlarmRepository correctly delegates the deletion of an alarm
     * by its ID to AlarmPreferences.
     */
    @Test
    fun testDeleteAlarm() {
        val alarmToDelete = Alarm(2, "09:00", "Alarm to Delete", "Lunes", "Problema corto", false, Calendar.getInstance())
        val alarmsJson = gson.toJson(listOf(alarmToDelete))
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmsJson)

        alarmRepository.deleteAlarm(2)

        // Verify that the updated alarm list was saved in SharedPreferences
        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }

    /**
     * Test for the getNewAlarmId() method in AlarmRepository.
     * Verifies that AlarmRepository correctly calculates a new alarm ID based on
     * the size of the existing alarm list.
     */
    @Test
    fun testGetNewAlarmId() {
        val mockAlarms = listOf(
            Alarm(1, "07:00", "", "Una vez", "Problema corto", true, Calendar.getInstance()),
            Alarm(4, "18:00", "Siesta", "Diaria", "Problema corto", false, Calendar.getInstance())
        )
        val alarmsJson = gson.toJson(mockAlarms)
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmsJson)

        val newAlarmId = alarmRepository.getNewAlarmId()

        // Verificar que el nuevo ID es el esperado
        assert(newAlarmId == 5)
        verify(sharedPreferences).getString("alarms_list", null)
    }
}
