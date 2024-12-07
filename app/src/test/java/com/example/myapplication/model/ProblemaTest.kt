package com.example.myapplication.model

import kotlin.random.Random
import org.junit.Assert.*
import org.junit.Test

class ProblemaTest {
    
    @Test
    fun `test generar opciones incluye la respuesta correcta`() {
        val respuestaCorrecta = "10"
        val opciones = Problema("", emptyList(), "").generarOpciones(respuestaCorrecta, Random(0))
        assertTrue(opciones.contains(respuestaCorrecta))
        assertEquals(4, opciones.size)
    }

    @Test
    fun `test generar opciones son únicas`() {
        val respuestaCorrecta = "15"
        val opciones = Problema("", emptyList(), "").generarOpciones(respuestaCorrecta, Random(0))
        assertEquals(4, opciones.distinct().size)
    }

    @Test
    fun `test generar opciones no contienen valores negativos`() {
        val respuestaCorrecta = "5"
        val opciones = Problema("", emptyList(), "").generarOpciones(respuestaCorrecta, Random(0))
        assertTrue(opciones.all { it.toInt() >= 0 })
    }

    @Test
    fun `test crearProblemaMatematico genera problemas válidos`() {
        val random = Random(0)
        val problema = Problema("", emptyList(), "").crearProblemaMatematico(random)
        assertNotNull(problema.enunciado)
        assertTrue(problema.opciones.isNotEmpty())
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    @Test
    fun `test crearProblemaLogicaMatematica genera problemas válidos`() {
        val random = Random(0)
        val problema = Problema("", emptyList(), "").crearProblemaLogicaMatematica(random)
        assertNotNull(problema.enunciado)
        assertTrue(problema.opciones.isNotEmpty())
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    @Test
    fun `test crearProblemaLogicaMatematica cubre todas las ramas`() {
        repeat(9) { tipoProblema ->
            val random = Random(tipoProblema.toLong())
            val problema = Problema("", emptyList(), "").crearProblemaLogicaMatematica(random)
            assertNotNull(problema.enunciado)
            assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
        }
    }

    @Test
    fun `test crearAcertijo selecciona acertijos válidos`() {
        val random = Random(0)
        val problema = Problema("", emptyList(), "").crearAcertijo(random)
        assertNotNull(problema.enunciado)
        assertTrue(problema.opciones.isNotEmpty())
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    @Test
    fun `test crearProblemaAleatorio genera diferentes tipos`() {
        val random = Random(0)
        val problema1 = Problema("", emptyList(), "").crearProblemaAleatorio(random)
        val problema2 = Problema("", emptyList(), "").crearProblemaAleatorio(random)
        assertNotEquals(problema1.enunciado, problema2.enunciado)
    }

    @Test
    fun `test opciones generadas son aleatorias`() {
        val opciones1 = Problema("", emptyList(), "").generarOpciones("10", Random(0))
        val opciones2 = Problema("", emptyList(), "").generarOpciones("10", Random(1))
        assertNotEquals(opciones1, opciones2) // Opciones deben diferir con distintas semillas
    }

    @Test
    fun `test valores extremos en problemas matemáticos`() {
        val random = Random(1) // Valores bajos para números
        val problema = Problema("", emptyList(), "").crearProblemaMatematico(random)
        assertNotNull(problema.enunciado)
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    @Test
    fun `test valores extremos en problemas lógicos`() {
        val random = Random(2) // Control para problemas simples
        val problema = Problema("", emptyList(), "").crearProblemaLogicaMatematica(random)
        assertNotNull(problema.enunciado)
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }

    @Test
    fun `test valores extremos en acertijos`() {
        val random = Random(3) // Simulación para acertijos
        val problema = Problema("", emptyList(), "").crearAcertijo(random)
        assertNotNull(problema.enunciado)
        assertTrue(problema.opciones.isNotEmpty())
        assertTrue(problema.opciones.contains(problema.respuestaCorrecta))
    }
}
