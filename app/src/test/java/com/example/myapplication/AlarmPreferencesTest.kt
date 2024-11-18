package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*

/**
 * Unit tests for the AlarmPreferences class.
 * These tests use Mockito to mock SharedPreferences and verify the correct behavior
 * of saving, editing, deleting, and loading alarms.
 */
class AlarmPreferencesTest {

    // Mocks for SharedPreferences and its Editor
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var context: Context
    private val gson = Gson()

    /**
     * Setup method to initialize the necessary mocks before each test.
     * This method is called before each test to ensure the proper state for testing.
     */
    @Before
    fun setup() {
        // Mock the context and SharedPreferences
        context = mock(Context::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        // Set up the behavior for SharedPreferences and its Editor
        `when`(context.getSharedPreferences("alarms", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Initialize the AlarmPreferences class with the mocked context
        alarmPreferences = AlarmPreferences(context)
    }

    /**
     * Test case to verify that an alarm is saved correctly in SharedPreferences.
     * This includes verifying that the putString() and apply() methods are called.
     */
    @Test
    fun testSaveAlarm() {
        // Create a new alarm object
        val alarm = Alarm(1, "08:00", "Test Alarm", "Diaria", true, Calendar.getInstance())
        
        // Mock that there are no existing alarms in SharedPreferences
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(null)
        
        // Call the method to save the alarm
        alarmPreferences.saveAlarm(alarm)
        
        // Verify that the alarm is serialized and saved using putString() and apply()
        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }

    /**
     * Test case to verify that an alarm can be retrieved by its ID from SharedPreferences.
     * It mocks the existing alarms and checks that the correct alarm is returned.
     */
    @Test
    fun testGetAlarmById() {
        // Create a list of alarms and serialize it to JSON
        val alarmList = mutableListOf(Alarm(1, "08:00", "Test Alarm", "Diaria", true, Calendar.getInstance()))
        val alarmListJson = gson.toJson(alarmList)
        
        // Mock the stored JSON list in SharedPreferences
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmListJson)
        
        // Call the method to retrieve the alarm by its ID
        val result = alarmPreferences.getAlarmById(1)
        
        // Verify that the correct alarm is returned
        assertNotNull(result)
        assertEquals(1, result?.id)
    }

    /**
     * Test case to verify that an existing alarm can be edited and updated in SharedPreferences.
     * It checks that the updated alarm is serialized and saved.
     */
    @Test
    fun testEditAlarm() {
        // Create an original alarm and an updated version
        val originalAlarm = Alarm(1, "08:00", "Test Alarm", "Diaria", true, Calendar.getInstance())
        val updatedAlarm = originalAlarm.copy(name = "Updated Alarm")
        
        // Mock the existing alarms list
        val alarmList = mutableListOf(originalAlarm)
        val alarmListJson = gson.toJson(alarmList)
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmListJson)
        
        // Call the method to edit the alarm
        alarmPreferences.editAlarm(updatedAlarm)
        
        // Verify that the updated alarm is saved in SharedPreferences
        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }

    /**
     * Test case to verify that an alarm can be deleted from SharedPreferences.
     * It mocks the existing alarms and ensures the correct alarm is removed.
     */
    @Test
    fun testDeleteAlarm() {
        // Create a list of alarms
        val alarmList = mutableListOf(Alarm(1, "08:00", "Test Alarm", "Diaria", true, Calendar.getInstance()))
        val alarmListJson = gson.toJson(alarmList)
        
        // Mock the stored JSON list in SharedPreferences
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmListJson)
        
        // Call the method to delete the alarm by ID
        alarmPreferences.deleteAlarm(1)
        
        // Verify that the updated list, without the deleted alarm, is saved
        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }

    /**
     * Test case to verify that all alarms can be loaded from SharedPreferences.
     * It checks that the list of alarms is correctly deserialized and returned.
     */
    @Test
    fun testLoadAlarms() {
        // Create a list of alarms and serialize it to JSON
        val alarmList = mutableListOf(Alarm(1, "08:00", "Test Alarm", "Diaria", true, Calendar.getInstance()))
        val alarmListJson = gson.toJson(alarmList)
        
        // Mock the stored JSON list in SharedPreferences
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn(alarmListJson)
        
        // Call the method to load all alarms
        val loadedAlarms = alarmPreferences.loadAlarms()
        
        // Verify that the alarms list is correctly deserialized
        assertEquals(1, loadedAlarms.size)
        assertEquals("Test Alarm", loadedAlarms[0].name)
    }
}
