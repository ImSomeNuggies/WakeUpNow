package com.example.myapplication.viewmodel.factory

import android.app.Application
import com.example.myapplication.viewmodel.QrGeneratorViewModel
import com.example.myapplication.viewmodel.factory.AlarmSoundingViewModelFactoryTest.UnknownViewModel
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito.mock

class QrGeneratorViewModelFactoryTest {

    @Test
    fun `create should return an instance of QrGeneratorViewModel when correct ViewModel class is provided`() {
        // Arrange
        val mockApplication = mock(Application::class.java)
        val factory = QrGeneratorViewModelFactory(mockApplication)

        // Act
        val viewModel = factory.create(QrGeneratorViewModel::class.java)

        // Assert
        assertNotNull("ViewModel should not be null", viewModel)
        //assertTrue("ViewModel should be an instance of QrGeneratorViewModel", viewModel is QrGeneratorViewModel)
    }

    @Test
    fun `create should throw IllegalArgumentException when incorrect ViewModel class is provided`() {
        // Arrange
        val mockApplication = mock(Application::class.java)
        val factory = QrGeneratorViewModelFactory(mockApplication)


        // Act & Assert
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            factory.create(UnknownViewModel::class.java)
        }
    }
}
