package com.example.myapplication.viewmodel.factory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.QRSoundingViewModel
import com.example.myapplication.viewmodel.factory.AlarmSoundingViewModelFactoryTest.UnknownViewModel
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class QRSoundingViewModelFactoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `create should return an instance of QRSoundingViewModel when correct ViewModel class is provided`() {
        // Arrange
        val mockSharedPreferences = mock(android.content.SharedPreferences::class.java)
        val mockStatsRepository = AlarmStatsRepository(mockSharedPreferences)
        val factory = QRSoundingViewModelFactory(mockStatsRepository)

        // Act
        val viewModel = factory.create(QRSoundingViewModel::class.java)

        // Assert
        assertNotNull("ViewModel should not be null", viewModel)
    }

    @Test
    fun `create should throw IllegalArgumentException when incorrect ViewModel class is provided`() {
        // Arrange
        val mockSharedPreferences = mock(android.content.SharedPreferences::class.java)
        val mockStatsRepository = AlarmStatsRepository(mockSharedPreferences)
        val factory = QRSoundingViewModelFactory(mockStatsRepository)

        // Act
        // Act & Assert
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            factory.create(UnknownViewModel::class.java)
        }    }
}
