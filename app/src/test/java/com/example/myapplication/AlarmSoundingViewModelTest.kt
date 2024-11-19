package com.example.myapplication.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.Problema
import com.example.myapplication.models.AlarmStatistic
import com.example.myapplication.repositories.AlarmStatsRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*

class AlarmSoundingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Needed to test LiveData updates

    private lateinit var application: Application
    private lateinit var repository: AlarmStatsRepository
    private lateinit var viewModel: AlarmSoundingViewModel

    @Before
    fun setUp() {
        application = mock(Application::class.java)
        repository = mock(AlarmStatsRepository::class.java)
        viewModel = AlarmSoundingViewModel(application)
    }

    @Test
    fun `setAlarmName sets correct value`() {
        val observer = mock(Observer::class.java) as Observer<String>
        viewModel.alarmName.observeForever(observer)

        viewModel.setAlarmName("Morning Alarm")
        assertEquals("Morning Alarm", viewModel.alarmName.value)

        viewModel.setAlarmName(null)
        assertEquals("", viewModel.alarmName.value)
    }

    @Test
    fun `getCurrentTime returns correctly formatted time`() {
        val currentTime = viewModel.getCurrentTime()
        assertTrue(currentTime.matches(Regex("\\d{2}:\\d{2}"))) // HH:mm format
    }

    @Test
    fun `verificarRespuesta updates statistics and LiveData on correct answer`() {
        val observerFinish = mock(Observer::class.java) as Observer<Boolean>
        val observerError = mock(Observer::class.java) as Observer<String>
        viewModel.shouldFinish.observeForever(observerFinish)
        viewModel.errorMessage.observeForever(observerError)

        val correctAnswer = "42"
        val incorrectAnswer = "24"

        // Verify correct answer
        viewModel.verificarRespuesta(correctAnswer, correctAnswer)
        assertTrue(viewModel.shouldFinish.value ?: false)
        verify(repository).saveStatistic(any(AlarmStatistic::class.java))

        // Verify incorrect answer
        viewModel.verificarRespuesta(incorrectAnswer, correctAnswer)
        assertEquals("Respuesta incorrecta. Inténtalo de nuevo.", viewModel.errorMessage.value)
    }

    @Test
    fun `crearProblemaAleatorio creates random problems`() {
        val problem = viewModel.crearProblemaAleatorio()
        assertNotNull(problem)
        assertTrue(problem.opciones.size == 4)
        assertTrue(problem.opciones.contains(problem.respuestaCorrecta))
    }

    @Test
    fun `crearProblemaMatematico creates valid math problems`() {
        val problem = viewModel.crearProblemaMatematico()
        assertNotNull(problem.enunciado)
        assertTrue(problem.opciones.size == 4)
        assertTrue(problem.opciones.contains(problem.respuestaCorrecta))
    }

    @Test
    fun `crearProblemaLogicaMatematica creates valid logic problems`() {
        val problem = viewModel.crearProblemaLogicaMatematica()
        assertNotNull(problem.enunciado)
        assertTrue(problem.opciones.size == 4)
        assertTrue(problem.opciones.contains(problem.respuestaCorrecta))
    }

    @Test
    fun `crearAcertijo creates valid riddles`() {
        val riddle = viewModel.crearAcertijo()
        assertNotNull(riddle.enunciado)
        assertTrue(riddle.opciones.size == 4)
        assertTrue(riddle.opciones.contains(riddle.respuestaCorrecta))
    }

    @Test
    fun `generarOpciones generates four unique options including the correct one`() {
        val correctAnswer = "50"
        val options = viewModel.generarOpciones(correctAnswer)
        assertEquals(4, options.size)
        assertTrue(options.contains(correctAnswer))
        assertTrue(options.all { it.toIntOrNull() != null })
    }
}
