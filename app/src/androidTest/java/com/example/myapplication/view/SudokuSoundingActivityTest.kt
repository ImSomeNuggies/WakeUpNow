package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.Description
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class SudokuSoundingActivityTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testInitialUIState() {
        val intent = Intent(context, SudokuSoundingActivity::class.java).apply {
            putExtra("alarm_name", "Sudoku Alarm")
        }

        ActivityScenario.launch<SudokuSoundingActivity>(intent).use {
            // Verifica que el nombre de la alarma es correcto
            onView(withId(R.id.textViewNombreAlarma)).check(matches(withText("Sudoku Alarm")))

            // Verifica que la hora actual no está vacía
            onView(withId(R.id.textViewHoraActual)).check(matches(not(withText(""))))

            // Verifica que los botones están presentes
            onView(withId(R.id.button_1)).check(matches(isDisplayed()))
            onView(withId(R.id.button_2)).check(matches(isDisplayed()))
            onView(withId(R.id.button_3)).check(matches(isDisplayed()))
            onView(withId(R.id.button_4)).check(matches(isDisplayed()))
            onView(withId(R.id.button_erase)).check(matches(isDisplayed()))

            // Verifica que las celdas del tablero están presentes
            onView(withId(R.id.cell_00)).check(matches(isDisplayed()))
            onView(withId(R.id.cell_33)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testSelectNumberUpdatesButtonState() {
        val intent = Intent(context, SudokuSoundingActivity::class.java)

        ActivityScenario.launch<SudokuSoundingActivity>(intent).use {
            // Selecciona un número
            onView(withId(R.id.button_1)).perform(click())

            // Verifica que el botón seleccionado tiene el fondo esperado
            onView(withId(R.id.button_1)).check(matches(withBackground(R.drawable.selected_sudoku_button)))

            // Verifica que otros botones no tienen el fondo seleccionado
            onView(withId(R.id.button_2)).check(matches(withBackground(R.drawable.sudoku_button)))
            onView(withId(R.id.button_3)).check(matches(withBackground(R.drawable.sudoku_button)))
            onView(withId(R.id.button_4)).check(matches(withBackground(R.drawable.sudoku_button)))
        }
    }

    @Test
    fun testPlaceCorrectNumberInCell() {
        val intent = Intent(context, SudokuSoundingActivity::class.java)

        ActivityScenario.launch<SudokuSoundingActivity>(intent).use { scenario ->
            var correctRow = -1
            var correctCol = -1
            var correctNumber = -1
            var cellId = -1

            // Ejecuta lógica en el contexto de la actividad
            scenario.onActivity { activity ->
                val board = activity.viewModel.getBoard()
                val solution = activity.viewModel.getSolution()

                // Busca una celda editable y su solución correcta
                loop@ for (row in 0 until 4) {
                    for (col in 0 until 4) {
                        if (board[row][col] == 0) {
                            correctRow = row
                            correctCol = col
                            correctNumber = solution[row][col]
                            cellId = getCellId(row, col)
                            break@loop
                        }
                    }
                }
            }

            // Validamos que se encontró una celda editable
            assert(correctRow != -1 && correctCol != -1) { "No editable cell found" }

            // Selecciona el número correcto
            onView(withId(getButtonId(correctNumber))).perform(click())

            // Haz clic en la celda editable
            onView(withId(cellId)).perform(click())

            // Verifica que la celda muestra el número seleccionado
            onView(withId(cellId)).check(matches(withText(correctNumber.toString())))

            // Verifica que el texto está en color lila (correcto)
            onView(withId(cellId)).check(matches(hasTextColor(R.color.lila)))
        }
    }

    @Test
    fun testPlaceIncorrectNumberInCell() {
        val intent = Intent(context, SudokuSoundingActivity::class.java)

        ActivityScenario.launch<SudokuSoundingActivity>(intent).use { scenario ->
            var editableRow = -1
            var editableCol = -1
            var incorrectNumber = -1
            var cellId = -1

            // Ejecuta lógica en el contexto de la actividad
            scenario.onActivity { activity ->
                val board = activity.viewModel.getBoard()
                val solution = activity.viewModel.getSolution()

                // Busca una celda editable
                loop@ for (row in 0 until 4) {
                    for (col in 0 until 4) {
                        if (board[row][col] == 0) {
                            editableRow = row
                            editableCol = col
                            incorrectNumber = (1..4).first { it != solution[row][col] } // Número incorrecto
                            cellId = getCellId(row, col)
                            break@loop
                        }
                    }
                }
            }

            // Validamos que se encontró una celda editable
            assert(editableRow != -1 && editableCol != -1) { "No editable cell found" }

            // Selecciona un número incorrecto
            onView(withId(getButtonId(incorrectNumber))).perform(click())

            // Haz clic en la celda editable
            onView(withId(cellId)).perform(click())

            // Verifica que la celda muestra el número seleccionado
            onView(withId(cellId)).check(matches(withText(incorrectNumber.toString())))

            // Verifica que el texto está en color rojo (incorrecto)
            onView(withId(cellId)).check(matches(hasTextColor(R.color.red)))
        }
    }

    @Test
    fun testEraseNumberFromCell() {
        val intent = Intent(context, SudokuSoundingActivity::class.java)

        ActivityScenario.launch<SudokuSoundingActivity>(intent).use { scenario ->
            var editableRow = -1
            var editableCol = -1
            var numberToPlace = 3
            var cellId = -1

            // Ejecuta lógica en el contexto de la actividad
            scenario.onActivity { activity ->
                val board = activity.viewModel.getBoard()

                // Busca una celda editable
                loop@ for (row in 0 until 4) {
                    for (col in 0 until 4) {
                        if (board[row][col] == 0) {
                            editableRow = row
                            editableCol = col
                            cellId = getCellId(row, col)
                            break@loop
                        }
                    }
                }
            }

            // Validamos que se encontró una celda editable
            assert(editableRow != -1 && editableCol != -1) { "No editable cell found" }

            // Selecciona un número
            onView(withId(getButtonId(numberToPlace))).perform(click())

            // Coloca el número en la celda
            onView(withId(cellId)).perform(click())
            onView(withId(cellId)).check(matches(withText(numberToPlace.toString())))

            // Selecciona borrar
            onView(withId(R.id.button_erase)).perform(click())

            // Borra el número de la celda
            onView(withId(cellId)).perform(click())

            // Verifica que la celda está vacía
            onView(withId(cellId)).check(matches(withText("")))
        }
    }


    // Helper para obtener el ID del botón basado en el número
    private fun getButtonId(number: Int): Int {
        return when (number) {
            1 -> R.id.button_1
            2 -> R.id.button_2
            3 -> R.id.button_3
            4 -> R.id.button_4
            else -> throw IllegalArgumentException("Invalid number")
        }
    }

    @Test
    fun testFinishActivityWhenShouldFinishTrue() {
        val intent = Intent(context, SudokuSoundingActivity::class.java)

        ActivityScenario.launch<SudokuSoundingActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                // Simula que el ViewModel emite shouldFinish como true
                activity.viewModel.setShouldFinishForTesting(true)
            }

            // Verifica que la actividad se cierra
            scenario.onActivity {
                assertTrue(it.isFinishing)
            }
        }
    }

    // Helper para obtener el ID de la celda basado en la fila y columna
    private fun getCellId(row: Int, col: Int): Int {
        return context.resources.getIdentifier("cell_${row}${col}", "id", context.packageName)
    }

    // Matcher para verificar el color del texto
    private fun hasTextColor(expectedColorId: Int): Matcher<View> {
        return object : BoundedMatcher<View, TextView>(TextView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has text color with ID: $expectedColorId")
            }

            override fun matchesSafely(textView: TextView): Boolean {
                val expectedColor = ContextCompat.getColor(textView.context, expectedColorId)
                return textView.currentTextColor == expectedColor
            }
        }
    }

    // Matcher para verificar el fondo del botón
    private fun withBackground(expectedDrawableId: Int): Matcher<View> {
        return object : BoundedMatcher<View, Button>(Button::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has background with drawable ID: $expectedDrawableId")
            }

            override fun matchesSafely(button: Button): Boolean {
                val expectedDrawable = ContextCompat.getDrawable(button.context, expectedDrawableId)
                return button.background.constantState == expectedDrawable?.constantState
            }
        }
    }
}
