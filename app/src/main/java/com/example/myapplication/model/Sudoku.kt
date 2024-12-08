package com.example.myapplication.model

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
        // Intentar llenar el tablero varias veces si es necesario
        for (attempt in 1..10) { // Intentar hasta 10 veces
            clearBoard() // Limpia el tablero antes de cada intento
            if (tryFillBoard()) {
                return // Si se llena exitosamente, terminamos
            }
        }
        throw IllegalStateException("No se pudo generar un Sudoku válido después de varios intentos")
    }

    // Intenta llenar el tablero respetando las reglas del Sudoku
    private fun tryFillBoard(): Boolean {
        val numbers = listOf(1, 2, 3, 4)
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val validNumbers = getValidNumbers(i, j)
                if (validNumbers.isEmpty()) {
                    return false // Si no hay números válidos, fallamos
                }
                sudokuBoard[i][j] = validNumbers.random()
                editableCells[i][j] = false // Marcar la celda como no editable
            }
        }
        return true // Tablero llenado exitosamente
    }

    // Limpia el tablero y marca todas las celdas como no editables
    private fun clearBoard() {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                sudokuBoard[i][j] = 0
                editableCells[i][j] = false
            }
        }
    }

    // Devuelve una lista de números válidos para colocar en la posición (row, col)
    private fun getValidNumbers(row: Int, col: Int): List<Int> {
        val usedNumbers = mutableSetOf<Int>()
        val numbers = listOf(1, 2, 3, 4)

        // Verificar la fila
        for (j in 0 until 4) {
            if (sudokuBoard[row][j] != 0) {
                usedNumbers.add(sudokuBoard[row][j])
            }
        }

        // Verificar la columna
        for (i in 0 until 4) {
            if (sudokuBoard[i][col] != 0) {
                usedNumbers.add(sudokuBoard[i][col])
            }
        }

        // Verificar el subcuadro 2x2
        val boxRowStart = (row / 2) * 2
        val boxColStart = (col / 2) * 2
        for (i in boxRowStart until boxRowStart + 2) {
            for (j in boxColStart until boxColStart + 2) {
                if (sudokuBoard[i][j] != 0) {
                    usedNumbers.add(sudokuBoard[i][j])
                }
            }
        }

        // Los números disponibles son los que no están en la fila, columna ni subcuadro
        return numbers.filter { it !in usedNumbers }
    }

    //Elimina el número de 6 celdas (editable para más dificultad) de manera aleatoria
    private fun removeCells() {
        val cellsToRemove = mutableListOf<Pair<Int, Int>>()

        // Creamos una lista de todas las posiciones posibles (0 a 3 para filas y columnas)
        val allCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                allCells.add(Pair(i, j))
            }
        }

        // Barajamos las celdas aleatoriamente
        allCells.shuffle()

        // Seleccionamos las primeras 6 celdas aleatorias para eliminar
        for (i in 0 until 6) {
            cellsToRemove.add(allCells[i])
        }

        // Copiar el tablero inicial al tablero del usuario para iniciar el juego
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                userBoard[i][j] = sudokuBoard[i][j]
                editableCells[i][j] = false // Marcamos todas las celdas como no editables inicialmente
            }
        }

        // Eliminar las celdas seleccionadas
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

    // Devuelve el tablero actual del usuario
    fun getSolution(): Array<IntArray> {
        return sudokuBoard
    }

    // Verifica si una celda es editable
    fun isEditable(row: Int, col: Int): Boolean {
        return editableCells[row][col]
    }
}
