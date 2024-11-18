package com.example.myapplication.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.viewmodels.AlarmSoundingViewModel
import com.example.myapplication.Problema
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.text.SimpleDateFormat
import java.util.*

class AlarmSoundingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AlarmSoundingViewModel

    @Before
    fun setUp() {
        val mockApplication = mock(Application::class.java)
        val mockProblemas = listOf(
            Problema("Question 1", listOf("Option A", "Option B"), "Option A"),
            Problema("Question 2", listOf("Option C", "Option D"), "Option D")
        )
        
        // Pass the mock list directly to the viewModel for testing
        viewModel = AlarmSoundingViewModel(mockApplication, mockProblemas)
    }

    // Unit test cases for each method
    @Test
    fun `setAlarmName should update alarmName LiveData`() {
        val testName = "Morning Alarm"
        viewModel.setAlarmName(testName)

        assert(viewModel.alarmName.value == testName)
    }

    @Test
    fun `verificarRespuesta should set shouldFinish to true when answer is correct`() {
        val correctAnswer = "Option A"
        viewModel.verificarRespuesta("Option A", correctAnswer)

        assert(viewModel.shouldFinish.value == true)
        assert(viewModel.errorMessage.value == null)
    }

    @Test
    fun `verificarRespuesta should set errorMessage when answer is incorrect`() {
        val correctAnswer = "Option A"
        viewModel.verificarRespuesta("Option B", correctAnswer)

        assert(viewModel.shouldFinish.value == false)
        assert(viewModel.errorMessage.value == "Respuesta incorrecta. Inténtalo de nuevo.")
    }

    @Test
    fun `getCurrentTime should return current time in HH_mm format`() {
        val expectedFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        assert(viewModel.getCurrentTime() == expectedFormat)
    }

    @Test
    fun `seleccionarProblemaAleatorio should return a random Problema from list`() {
        val problemas = listOf(
            Problema("Question 1", listOf("Option A", "Option B"), "Option A"),
            Problema("Question 2", listOf("Option C", "Option D"), "Option D")
        )

        val selectedProblema = viewModel.seleccionarProblemaAleatorio(problemas)

        assert(selectedProblema != null && problemas.contains(selectedProblema))
    }

    @Test
    fun `seleccionarProblemaAleatorio should return null when list is empty`() {
        val problemas = emptyList<Problema>()

        val selectedProblema = viewModel.seleccionarProblemaAleatorio(problemas)

        assert(selectedProblema == null)
    }
}
