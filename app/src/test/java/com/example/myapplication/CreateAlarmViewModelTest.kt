package com.example.myapplication

import com.example.myapplication.repository.AlarmRepository
import com.example.myapplication.viewmodel.CreateAlarmViewModel
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.rules.ExpectedException
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.Calendar

class CreateAlarmViewModelTest {

    private lateinit var viewModel: CreateAlarmViewModel

    @Mock
    private lateinit var repository: AlarmRepository

//    @get:Rule
//    val exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CreateAlarmViewModel(repository)
    }

    @Test
    fun `saveAlarm should create and save a new alarm with correct properties`() {
        // Set up the necessary data
        viewModel.selectedTime = "08:30"
        viewModel.selectedPeriodicity = "Diaria"
        `when`(repository.getNewAlarmId()).thenReturn(1)

        // Call saveAlarm and capture the result
        val result = viewModel.saveAlarm("Morning Alarm")

        // Verify that the alarm was created with the correct properties
        assert(result.id == 1)
        assert(result.time == "08:30")
        assert(result.name == "Morning Alarm")
        assert(result.periodicity == "Diaria")
        assert(result.isActive)
        
        // Verify that ringTime was set correctly
        val expectedTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        assert(result.ringTime.timeInMillis == expectedTime.timeInMillis)

        // Verify that saveAlarm was called on the repository with the correct alarm
        verify(repository).saveAlarm(result)
    }
}
