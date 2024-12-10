package com.example.myapplication.viewmodel.factory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.AlarmSoundingViewModel
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock

class AlarmSoundingViewModelFactoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `create should return an instance of AlarmSoundingViewModel when correct ViewModel class is provided`() {
        // Arrange
        val mockSharedPreferences = mock(android.content.SharedPreferences::class.java)
        val mockStatsRepository = AlarmStatsRepository(mockSharedPreferences)
        val factory = AlarmSoundingViewModelFactory(mockStatsRepository)

        // Act
        val viewModel = factory.create(modelClass = AlarmSoundingViewModel::class.java)
//
//        // Assert
          assertNotNull("ViewModel should not be null", viewModel)
    }

    @Test
    fun `create should throw IllegalArgumentException when an unknown ViewModel class is provided`() {
        // Arrange
        val mockStatsRepository = mock(AlarmStatsRepository::class.java)
        val factory = AlarmSoundingViewModelFactory(mockStatsRepository)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            factory.create(UnknownViewModel::class.java)
        }
    }

    // Define a dummy ViewModel class to simulate an unknown type
    class UnknownViewModel : ViewModel()
}
