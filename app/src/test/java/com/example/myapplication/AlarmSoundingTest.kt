package com.example.myapplication

import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Unit tests for the AlarmSounding activity.
 * These tests verify the behavior of the activity, including response verification and UI updates.
 */
class AlarmSoundingTest {

    private lateinit var alarmSounding: AlarmSounding
    private lateinit var problemaTextView: TextView
    private lateinit var opcion1Button: Button
    private lateinit var opcion2Button: Button
    private lateinit var opcion3Button: Button
    private lateinit var opcion4Button: Button
    private lateinit var problemaFalloTextView: TextView

    @Before
    fun setUp() {
        // Simular la actividad AlarmSounding y los elementos de UI
        alarmSounding = mock(AlarmSounding::class.java)
        problemaTextView = mock(TextView::class.java)
        opcion1Button = mock(Button::class.java)
        opcion2Button = mock(Button::class.java)
        opcion3Button = mock(Button::class.java)
        opcion4Button = mock(Button::class.java)
        problemaFalloTextView = mock(TextView::class.java)

        // Simula la referencia de UI en la actividad
        alarmSounding.problemaTextView = problemaTextView
        alarmSounding.opcion1Button = opcion1Button
        alarmSounding.opcion2Button = opcion2Button
        alarmSounding.opcion3Button = opcion3Button
        alarmSounding.opcion4Button = opcion4Button
        alarmSounding.problemaFalloTextView = problemaFalloTextView
    }

    /**
     * Test para verificar si la respuesta es correcta.
     * Verifica que la actividad se cierre al dar una respuesta correcta.
     */
    @Test
    fun testVerificarRespuestaCorrecta() {
        val respuestaCorrecta = "8"
        alarmSounding.verificarRespuesta(respuestaCorrecta, respuestaCorrecta)

        // Verifica que la actividad termina
        verify(alarmSounding).finish()
    }

    /**
     * Test para verificar si la respuesta es incorrecta.
     * Verifica que se muestre el mensaje de error en el TextView correcto.
     */
    @Test
    fun testVerificarRespuestaIncorrecta() {
        val respuestaSeleccionada = "6"
        val respuestaCorrecta = "8"
        alarmSounding.verificarRespuesta(respuestaSeleccionada, respuestaCorrecta)

        // Verifica que el mensaje de error se actualiza en el TextView
        verify(alarmSounding.problemaFalloTextView).text = "Respuesta incorrecta. Inténtalo de nuevo."
    }

    /**
     * Test para verificar la carga de problemas desde el archivo JSON.
     */
    @Test
    fun testLeerProblemasDesdeArchivo() {
        val jsonString = """
            [
                {"problema": "¿Cuánto es 5 + 3?", "opciones": ["6", "7", "8", "9"], "correcta": "8"}
            ]
        """
        val gson = Gson()
        val listType = object : TypeToken<List<Problema>>() {}.type
        val problemas: List<Problema> = gson.fromJson(jsonString, listType)

        // Simular la carga de problemas desde un archivo (aquí simulamos un archivo de assets)
        assertNotNull(problemas)
        assertEquals(1, problemas.size)
        assertEquals("¿Cuánto es 5 + 3?", problemas[0].problema)
    }

    /**
     * Test para verificar que un problema aleatorio es seleccionado correctamente.
     */
    @Test
    fun testSeleccionarProblemaAleatorio() {
        val problemas = listOf(
            Problema("¿Cuánto es 5 + 3?", listOf("6", "7", "8", "9"), "8"),
            Problema("¿Cuánto es 10 - 2?", listOf("6", "8", "7", "9"), "8")
        )

        val problemaAleatorio = alarmSounding.seleccionarProblemaAleatorio(problemas)
        assertNotNull(problemaAleatorio)
        assertTrue(problemas.contains(problemaAleatorio))
    }
}