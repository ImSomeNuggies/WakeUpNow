package com.example.myapplication.viewmodel.factory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.example.myapplication.viewmodel.CreateAlarmViewModel
import com.example.myapplication.repository.AlarmRepository
import com.example.myapplication.viewmodel.factory.AlarmSoundingViewModelFactoryTest.UnknownViewModel
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock

class CreateAlarmViewModelFactoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `create should return an instance of CreateAlarmViewModel when correct ViewModel class is provided`() {
        // Arrange
        val mockRepository = mock(AlarmRepository::class.java)
        val factory = CreateAlarmViewModelFactory(mockRepository)

        // Act
        val viewModel = factory.create(CreateAlarmViewModel::class.java)

        // Assert
        assertNotNull("ViewModel should not be null", viewModel)
    }

    @Test
    fun `create should throw IllegalArgumentException when incorrect ViewModel class is provided`() {
        // Arrange
        val mockRepository = mock(AlarmRepository::class.java)
        val factory = CreateAlarmViewModelFactory(mockRepository)

        // Act
        // Act & Assert
        assertThrows<IllegalArgumentException> {
            factory.create(UnknownViewModel::class.java)
        }
    }
}
