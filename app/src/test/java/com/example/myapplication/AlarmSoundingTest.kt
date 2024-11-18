package com.example.myapplication

import android.widget.Button
import android.widget.TextView
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Unit tests for the AlarmSounding activity.
 * These tests verify the behavior of the activity, including response verification, UI updates,
 * and random problem generation across mathematical, logical, and riddle-based problems.
 */
class AlarmSoundingActivityTest {

    private lateinit var alarmSounding: AlarmSoundingActivity
    private lateinit var problemaTextView: TextView
    private lateinit var opcion1Button: Button
    private lateinit var opcion2Button: Button
    private lateinit var opcion3Button: Button
    private lateinit var opcion4Button: Button
    private lateinit var problemaFalloTextView: TextView

    @Before
    fun setUp() {
        // Mock the AlarmSounding activity and UI elements
        alarmSounding = mock(AlarmSoundingActivity::class.java)
        problemaTextView = mock(TextView::class.java)
        opcion1Button = mock(Button::class.java)
        opcion2Button = mock(Button::class.java)
        opcion3Button = mock(Button::class.java)
        opcion4Button = mock(Button::class.java)
        problemaFalloTextView = mock(TextView::class.java)

        // Set the mocked UI elements to the activity
        alarmSounding.problemaTextView = problemaTextView
        alarmSounding.opcion1Button = opcion1Button
        alarmSounding.opcion2Button = opcion2Button
        alarmSounding.opcion3Button = opcion3Button
        alarmSounding.opcion4Button = opcion4Button
        alarmSounding.problemaFalloTextView = problemaFalloTextView
    }

    /**
     * Test to verify that a correct answer finishes the activity.
     */
    @Test
    fun testVerificarRespuestaCorrecta() {
        val respuestaCorrecta = "8"
        alarmSounding.verificarRespuesta(respuestaCorrecta, respuestaCorrecta)

        // Verify that the activity finishes on a correct answer
        verify(alarmSounding).finish()
    }

    /**
     * Test to verify that an incorrect answer shows an error message in the TextView.
     */
    @Test
    fun testVerificarRespuestaIncorrecta() {
        val respuestaSeleccionada = "6"
        val respuestaCorrecta = "8"
        alarmSounding.verificarRespuesta(respuestaSeleccionada, respuestaCorrecta)

        // Verify that the error message updates in the TextView
        verify(alarmSounding.problemaFalloTextView).text = "Respuesta incorrecta. Inténtalo de nuevo."
    }

    /**
     * Test to verify that mathematical problems are generated correctly.
     */
    @Test
    fun testGenerarProblemaMatematico() {
        val problema = alarmSounding.crearProblemaMatematico()

        assertNotNull(problema)
        assertTrue(problema.enunciado.contains("¿Cuánto es") || problema.enunciado.contains("¿Cuál es"))
        assertEquals(4, problema.opciones.size)
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    /**
     * Test to verify that logic problems are generated correctly.
     */
    @Test
    fun testGenerarProblemaLogicaMatematica() {
        val problema = alarmSounding.crearProblemaLogicaMatematica()

        assertNotNull(problema)
        assertTrue(problema.enunciado.isNotEmpty())
        assertEquals(4, problema.opciones.size)
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    /**
     * Test to verify that riddles are generated correctly.
     */
    @Test
    fun testGenerarAcertijo() {
        val problema = alarmSounding.crearAcertijo()

        assertNotNull(problema)
        assertTrue(problema.enunciado.contains("?"))
        assertEquals(4, problema.opciones.size)
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    /**
     * Test to verify that a random problem is selected correctly from all types.
     */
    @Test
    fun testCrearProblemaAleatorio() {
        val problema = alarmSounding.crearProblemaAleatorio()

        assertNotNull(problema)
        assertEquals(4, problema.opciones.size)
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    /**
     * Test to verify the response options are generated correctly around a correct answer.
     */
    @Test
    fun testGenerarOpciones() {
        val respuestaCorrecta = "10"
        val opciones = alarmSounding.generarOpciones(respuestaCorrecta)

        assertEquals(4, opciones.size)
        assertTrue(opciones.contains(respuestaCorrecta))
        opciones.forEach { opcion ->
            val valor = opcion.toInt()
            assertTrue(valor in (respuestaCorrecta.toInt() - 5)..(respuestaCorrecta.toInt() + 5))
        }
    }

    /**
     * Test to verify that generated options are unique.
     */
    @Test
    fun testGenerarOpciones_UniqueOptions() {
        val respuestaCorrecta = 25
        val opciones = alarmSounding.generarOpciones(respuestaCorrecta.toString())

        val opcionesUnicas = opciones.toSet()
        assertEquals(4, opcionesUnicas.size) // Ensure all options are unique
        assertTrue(opciones.contains(respuestaCorrecta.toString()))
    }
}
