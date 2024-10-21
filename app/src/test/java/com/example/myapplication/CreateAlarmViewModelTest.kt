package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.Calendar

class CreateAlarmViewModelTest {

    private lateinit var repository: AlarmRepository
    private lateinit var viewModel: CreateAlarmViewModel

    @Before
    fun setUp() {
        // Mock del AlarmRepository
        repository = mock(AlarmRepository::class.java)
        viewModel = CreateAlarmViewModel(repository)
    }

    @Test
    fun `saveAlarm should throw exception when selectedTime is empty`() {
        // Arrange
        viewModel.selectedTime = ""

        // Assert
        assertThrows(IllegalArgumentException::class.java) {
            viewModel.saveAlarm("Test Alarm")
        }
    }

    @Test
    fun `saveAlarm should create and save a new alarm`() {
        // Arrange
        viewModel.selectedTime = "08:30"
        viewModel.selectedPeriodicity = "Diaria"
        val alarmId = 1

        // Configura el comportamiento del mock
        `when`(repository.getNewAlarmId()).thenReturn(alarmId)

        // Act
        val newAlarm = viewModel.saveAlarm("Test Alarm")

        // Assert
        assertEquals(alarmId, newAlarm.id)
        assertEquals("08:30", newAlarm.time)
        assertEquals("Test Alarm", newAlarm.name)
        assertEquals("Diaria", newAlarm.periodicity)
        assertEquals(true, newAlarm.isActive)
        assertEquals(8, newAlarm.ringTime.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, newAlarm.ringTime.get(Calendar.MINUTE))

        // Verifica que se llama a saveAlarm en el repositorio
        verify(repository).saveAlarm(newAlarm)
    }
}
