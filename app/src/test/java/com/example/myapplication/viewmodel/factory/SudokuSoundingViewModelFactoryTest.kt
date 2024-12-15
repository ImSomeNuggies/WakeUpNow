package com.example.myapplication.viewmodel.factory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.SudokuSoundingViewModel
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class SudokuSoundingViewModelFactoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `create should return an instance of SudokuSoundingViewModel when correct ViewModel class is provided`() {
        // Arrange
        val mockSharedPreferences = mock(android.content.SharedPreferences::class.java)
        val mockStatsRepository = AlarmStatsRepository(mockSharedPreferences)
        val mockTimeProvider = { 123456789L }
        val mockDateFormatter = { "12:34" }
        val factory = SudokuSoundingViewModelFactory(
            statsRepository = mockStatsRepository,
            timeProvider = mockTimeProvider,
            dateFormatter = mockDateFormatter
        )

        // Act
        val viewModel = factory.create(SudokuSoundingViewModel::class.java)

        // Assert
        assertNotNull("ViewModel should not be null", viewModel)
    }

    @Test
    fun `create should throw IllegalArgumentException when incorrect ViewModel class is provided`() {
        // Arrange
        val mockSharedPreferences = mock(android.content.SharedPreferences::class.java)
        val mockStatsRepository = AlarmStatsRepository(mockSharedPreferences)
        val factory = SudokuSoundingViewModelFactory(statsRepository = mockStatsRepository)

        // Act
        // Act & Assert
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            factory.create(AlarmSoundingViewModelFactoryTest.UnknownViewModel::class.java)
        }    }
}
