package com.example.myapplication.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SudokuTest {

    private lateinit var sudoku: Sudoku

    @Before
    fun setUp() {
        sudoku = Sudoku()
    }

    @Test
    fun `test inicialización del Sudoku llena el tablero correctamente`() {
        val userBoard = sudoku.getUserBoard()
        assertNotNull(userBoard)

        // Verificar que el tablero tiene dimensiones correctas
        assertEquals(4, userBoard.size)
        userBoard.forEach { row ->
            assertEquals(4, row.size)
        }

        // Verificar que las celdas editables están marcadas correctamente
        assertTrue(sudoku.isEditable(0, 0))
        assertTrue(sudoku.isEditable(1, 2))
        assertFalse(sudoku.isEditable(0, 1))
        assertFalse(sudoku.isEditable(1, 1))
    }

    @Test
    fun `test placeNumber actualiza celda editable correctamente`() {
        assertTrue(sudoku.isEditable(0, 0))
        val result = sudoku.placeNumber(0, 0, 1) // 1 es el número correcto
        assertTrue(result) // El número es correcto
        assertEquals(1, sudoku.getUserBoard()[0][0]) // El tablero se actualizó
    }

    @Test
    fun `test placeNumber no permite modificar celdas no editables`() {
        assertFalse(sudoku.isEditable(0, 1)) // Celda no editable
        val result = sudoku.placeNumber(0, 1, 1) // Intentar editar una celda no editable
        assertFalse(result) // No debería permitir la edición
        assertEquals(2, sudoku.getUserBoard()[0][1]) // El número original no cambia
    }

    @Test
    fun `test placeNumber no permite colocar número incorrecto`() {
        assertTrue(sudoku.isEditable(0, 0))
        val result = sudoku.placeNumber(0, 0, 5) // Número incorrecto
        assertFalse(result) // No es el número correcto
        assertEquals(5, sudoku.getUserBoard()[0][0]) // El número se actualiza, pero es incorrecto
    }

    @Test
    fun `test isSudokuCompleted devuelve true cuando está completo`() {
        // Completar el tablero correctamente
        sudoku.placeNumber(0, 0, 1)
        sudoku.placeNumber(1, 0, 3)
        sudoku.placeNumber(1, 2, 1)
        sudoku.placeNumber(2, 1, 3)
        sudoku.placeNumber(2, 2, 4)
        sudoku.placeNumber(3, 3, 3)

        assertTrue(sudoku.isSudokuCompleted()) // Todo está completo y correcto
    }

    @Test
    fun `test isSudokuCompleted devuelve false si hay celdas vacías`() {
        assertFalse(sudoku.isSudokuCompleted()) // No está completo al inicio
    }

    @Test
    fun `test isSudokuCompleted devuelve false si hay errores`() {
        sudoku.placeNumber(0, 0, 2) // Número incorrecto
        assertFalse(sudoku.isSudokuCompleted()) // No está completo ni correcto
    }

    @Test
    fun `test celdas iniciales no son editables`() {
        assertFalse(sudoku.isEditable(0, 1)) // Celda no eliminada
        assertFalse(sudoku.isEditable(1, 1)) // Celda no eliminada
    }

    @Test
    fun `test celdas eliminadas son editables`() {
        assertTrue(sudoku.isEditable(0, 0)) // Celda eliminada
        assertTrue(sudoku.isEditable(1, 2)) // Celda eliminada
    }
}
