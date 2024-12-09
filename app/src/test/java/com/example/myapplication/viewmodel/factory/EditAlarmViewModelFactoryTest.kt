package com.example.myapplication.viewmodel.factory

import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.viewmodel.EditAlarmViewModel
import com.example.myapplication.viewmodel.factory.AlarmSoundingViewModelFactoryTest.UnknownViewModel
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock

class EditAlarmViewModelFactoryTest {

    @Test
    fun `create should return an instance of EditAlarmViewModel when correct ViewModel class is provided`() {
        // Arrange
        val mockAlarmPreferences = mock(AlarmPreferences::class.java)
        val factory = EditAlarmViewModelFactory(mockAlarmPreferences)

        // Act
        val viewModel = factory.create(EditAlarmViewModel::class.java)

        // Assert
        assertNotNull("ViewModel should not be null", viewModel)
    }

    @Test
    fun `create should throw IllegalArgumentException when incorrect ViewModel class is provided`() {
        // Arrange
        val mockAlarmPreferences = mock(AlarmPreferences::class.java)
        val factory = EditAlarmViewModelFactory(mockAlarmPreferences)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            factory.create(UnknownViewModel::class.java)
        }
    }
}
