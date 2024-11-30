package com.example.myapplication

import kotlin.random.Random

class Sudoku {

    // Tablero completo generado de Sudoku
    private val sudokuBoard: Array<IntArray> = Array(4) { IntArray(4) { 0 } }

    // Tablero actual del usuario
    private val userBoard: Array<IntArray> = Array(4) { IntArray(4) { 0 } }

    // Celdas editables
    private val editableCells: Array<BooleanArray> = Array(4) { BooleanArray(4) { false } }

    init {
        initializeSudoku()
    }

    /* Inicializa el Sudoku generando el tablero completo, uno para rellanar 
    por el usuario y marca las celdas editables */
    fun initializeSudoku() {
        fillSudoku()
        removeCells()
    }

    // Genera un sudoku con valores aleatorios
    private fun fillSudoku() {
        // Sudoku 4x4 de prueba para probar las conexiones con la interfaz
        val exampleBoard = arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(2, 3, 4, 1),
            intArrayOf(4, 1, 2, 3)
        )

        // Copiar el tablero resuelto al tablero de Sudoku y marcar todas las celdas como no editables
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                sudokuBoard[i][j] = exampleBoard[i][j]
                editableCells[i][j] = false // Todas las celdas son no editables por defecto
            }
        }
    }

    // Elimina celdas específicas del Sudoku de ejemplo para probar la lógica de edición
    private fun removeCells() {
        // Celdas a eliminar (marcar como vacías y editables) para crear el rompecabezas
        val cellsToRemove = listOf(
            Pair(0, 0), Pair(1, 2), Pair(2, 1), Pair(3, 3),
            Pair(1, 0), Pair(2, 2)
        )

        // Copiar el tablero inicial al tablero del usuario para iniciar el juego
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                userBoard[i][j] = sudokuBoard[i][j]
            }
        }

        // Eliminar celdas en las coordenadas especificadas
        for ((row, col) in cellsToRemove) {
            userBoard[row][col] = 0 // Eliminar el número
            editableCells[row][col] = true // Marcar como editable
        }
    }


    // Coloca un número en el tablero del usuario
    fun placeNumber(row: Int, col: Int, num: Int): Boolean {
        if (!editableCells[row][col]) return false // No permite editar celdas no editables
        userBoard[row][col] = num // Actualiza el tablero del usuario
        return checkNumber(row, col, num) // Devuelve si el número es correcto
    }

    // Verifica si un número ingresado por el usuario es correcto
    private fun checkNumber(row: Int, col: Int, num: Int): Boolean {
        return sudokuBoard[row][col] == num
    }

    // Verifica si el Sudoku está completo y correcto
    fun isSudokuCompleted(): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (userBoard[i][j] == 0 || userBoard[i][j] != sudokuBoard[i][j]) {
                    return false // Si hay celdas vacías o números incorrectos, no está completo
                }
            }
        }
        return true // Si todo coincide, el Sudoku está completo
    }

    // Devuelve el tablero actual del usuario
    fun getUserBoard(): Array<IntArray> {
        return userBoard
    }

    // Verifica si una celda es editable
    fun isEditable(row: Int, col: Int): Boolean {
        return editableCells[row][col]
    }
}
