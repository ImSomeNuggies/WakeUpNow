package com.example.myapplication.viewmodel

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.model.Problema
import com.example.myapplication.repository.AlarmStatsRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmSoundingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Para pruebas con LiveData

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var statsRepository: AlarmStatsRepository
    private lateinit var viewModel: AlarmSoundingViewModel

    private val mockTimeProvider: () -> Long = { 1672531200000L } // Fecha fija: 2023-01-01 00:00:00 UTC
    private val mockDateFormatter: () -> String = {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.UK)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.format(Date(mockTimeProvider()))
    }

    @Before
    fun setUp() {
        // Mock de SharedPreferences y su Editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        // Configuración de los mocks
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Inicializar el repositorio con SharedPreferences mockeado
        statsRepository = AlarmStatsRepository(sharedPreferences)

        // Inicializar ViewModel con el repositorio mockeado
        viewModel = AlarmSoundingViewModel(
            statsRepository = statsRepository,
            timeProvider = mockTimeProvider,
            dateFormatter = mockDateFormatter
        )
    }

    @Test
    fun `getCurrentTime devuelve hora formateada correctamente`() {
        val currentTime = viewModel.getCurrentTime()
        assertEquals("00:00", currentTime)
    }

    @Test
    fun `setAlarmName actualiza correctamente el nombre de la alarma`() {
        viewModel.setAlarmName("Morning Alarm")
        assertEquals("Morning Alarm", viewModel.alarmName.value)

        viewModel.setAlarmName(null)
        assertEquals("", viewModel.alarmName.value)
    }

    @Test
    fun `verificarRespuesta correcta guarda estadísticas y finaliza actividad`() {
        val problema = Problema(
            enunciado = "Enunciado",
            opciones = listOf("Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4"),
            respuestaCorrecta = "Respuesta1"
        )

        viewModel.verificarRespuesta("Respuesta1", problema.respuestaCorrecta)

        // Verificar que se llama a editor con los argumentos esperados
        verify(editor).putString(eq("statistics"), contains("failures\":0"))
        verify(editor).apply()

        // Verificar que shouldFinish se establece en true
        assertTrue(viewModel.shouldFinish.value == true)
    }

    @Test
    fun `verificarRespuesta incorrecta incrementa fallos y genera nuevo problema`() {
        val problema = Problema(
            enunciado = "Enunciado",
            opciones = listOf("Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4"),
            respuestaCorrecta = "Respuesta1"
        )

        viewModel.verificarRespuesta("Respuesta2", problema.respuestaCorrecta)

        // Verificar que el contador de fallos incrementa
        val errorMessage = viewModel.errorMessage.value
        assertEquals("Respuesta incorrecta. Inténtalo de nuevo.", errorMessage)

        // Verificar que se genera un nuevo problema
        assertNotNull(viewModel.problema.value)
        assertNotEquals(problema, viewModel.problema.value)
    }

    @Test
    fun `crearProblemaAleatorio genera problema válido`() {
        val problema = viewModel.crearProblemaAleatorio()
        assertNotNull(problema.enunciado)
        assertEquals(4, problema.opciones.size)
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    @Test
    fun `shouldFinish inicia como false`() {
        assertFalse(viewModel.shouldFinish.value ?: true)
    }
}
