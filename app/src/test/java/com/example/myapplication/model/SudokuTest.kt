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

        // Verificar que el tablero no tiene números fuera de rango
        userBoard.forEach { row ->
            row.forEach { cell ->
                assertTrue(cell in 0..4) // Los números están en el rango 1-4 o 0 (vacío)
            }
        }
    }

    @Test
    fun `test removeCells crea exactamente 6 celdas editables`() {
        val editableCount = (0 until 4).sumBy { row ->
            (0 until 4).count { col -> sudoku.isEditable(row, col) }
        }
        assertEquals(6, editableCount) // Debe haber exactamente 6 celdas editables
    }

    @Test
    fun `test placeNumber actualiza celda editable correctamente`() {
        val editableCell = findEditableCell()
        assertNotNull(editableCell)

        val (row, col) = editableCell!!
        val correctNumber = sudoku.getSolution()[row][col]
        val result = sudoku.placeNumber(row, col, correctNumber)

        assertTrue(result) // El número es correcto
        assertEquals(correctNumber, sudoku.getUserBoard()[row][col]) // El tablero se actualizó correctamente
    }

    @Test
    fun `test placeNumber no permite modificar celdas no editables`() {
        val nonEditableCell = findNonEditableCell()
        assertNotNull(nonEditableCell)

        val (row, col) = nonEditableCell!!
        val result = sudoku.placeNumber(row, col, 1) // Intentar modificar una celda no editable

        assertFalse(result) // No debería permitir la edición
        assertEquals(sudoku.getSolution()[row][col], sudoku.getUserBoard()[row][col]) // El número original no cambia
    }

    @Test
    fun `test isSudokuCompleted devuelve true cuando está completo y correcto`() {
        // Llenar todas las celdas editables con números correctos
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                if (sudoku.isEditable(row, col)) {
                    val correctNumber = sudoku.getSolution()[row][col]
                    sudoku.placeNumber(row, col, correctNumber)
                }
            }
        }

        assertTrue(sudoku.isSudokuCompleted()) // El tablero está completo y correcto
    }

    @Test
    fun `test isSudokuCompleted devuelve false si hay celdas vacías`() {
        assertFalse(sudoku.isSudokuCompleted()) // El tablero inicial tiene celdas vacías
    }

    @Test
    fun `test isSudokuCompleted devuelve false si hay errores`() {
        val editableCell = findEditableCell()
        assertNotNull(editableCell)

        val (row, col) = editableCell!!
        sudoku.placeNumber(row, col, (sudoku.getSolution()[row][col] % 4) + 1) // Colocar un número incorrecto

        assertFalse(sudoku.isSudokuCompleted()) // El tablero no es válido
    }

    @Test
    fun `test getUserBoard devuelve tablero del usuario correctamente`() {
        val userBoard = sudoku.getUserBoard()
        assertNotNull(userBoard)

        // El tamaño del tablero debe ser 4x4
        assertEquals(4, userBoard.size)
        userBoard.forEach { row ->
            assertEquals(4, row.size)
        }
    }

    @Test
    fun `test getSolution devuelve tablero completo correctamente`() {
        val solution = sudoku.getSolution()
        assertNotNull(solution)

        // El tamaño del tablero debe ser 4x4
        assertEquals(4, solution.size)
        solution.forEach { row ->
            assertEquals(4, row.size)
        }

        // La solución debe cumplir las reglas del Sudoku
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                val value = solution[row][col]
                assertTrue(value in 1..4) // Los valores están dentro del rango
            }
        }
    }

    // Métodos auxiliares para encontrar celdas editables y no editables
    private fun findEditableCell(): Pair<Int, Int>? {
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                if (sudoku.isEditable(row, col)) {
                    return Pair(row, col)
                }
            }
        }
        return null
    }

    private fun findNonEditableCell(): Pair<Int, Int>? {
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                if (!sudoku.isEditable(row, col)) {
                    return Pair(row, col)
                }
            }
        }
        return null
    }
}
