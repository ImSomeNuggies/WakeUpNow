package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.R
import com.example.myapplication.model.AlarmStatistic
import com.example.myapplication.model.Problema
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.AlarmSoundingViewModel
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class AlarmSoundingActivityTest {

    private lateinit var context: Context
    private lateinit var statsRepository: AlarmStatsRepository

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPreferences = context.getSharedPreferences("alarm_statistics", Context.MODE_PRIVATE)
        statsRepository = AlarmStatsRepository(sharedPreferences)

        // Limpia las estadísticas antes de cada prueba
        sharedPreferences.edit().clear().apply()
    }

    @Test
    fun testInitialUIState() {
        val intent = Intent(context, AlarmSoundingActivity::class.java)
        intent.putExtra("alarm_name", "Morning Alarm")

        ActivityScenario.launch<AlarmSoundingActivity>(intent).use {
            // Verifica que el nombre de la alarma se muestra correctamente
            onView(withId(R.id.textViewNombreAlarma)).check(matches(withText("Morning Alarm")))

            // Verifica que la hora actual no está vacía
            onView(withId(R.id.textViewHoraActual)).check(matches(not(withText(""))))

            // Verifica que el problema inicial se muestra
            onView(withId(R.id.problemaTextView)).check(matches(isDisplayed()))
            onView(withId(R.id.opcion1Button)).check(matches(isDisplayed()))
            onView(withId(R.id.opcion2Button)).check(matches(isDisplayed()))
            onView(withId(R.id.opcion3Button)).check(matches(isDisplayed()))
            onView(withId(R.id.opcion4Button)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testCorrectAnswerEndsActivity() {
        // Lanza la actividad con un Intent preconfigurado
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, AlarmSoundingActivity::class.java).apply {
            putExtra("alarm_name", "Test Alarm")
        }
        val scenario = ActivityScenario.launch<AlarmSoundingActivity>(intent)

        scenario.onActivity { activity ->
            // Configura un problema simulado para el ViewModel
            val problemaSimulado = Problema(
                enunciado = "¿Cuál es 2 + 2?",
                opciones = listOf("3", "4", "5", "6"),
                respuestaCorrecta = "4"
            )
            activity.viewModel.setProblemaForTesting(problemaSimulado)
        }

        // Verifica que la actividad aún está activa
        onView(withId(R.id.problemaTextView)).check(matches(isDisplayed()))

        // Responde correctamente
        onView(withId(R.id.opcion2Button)).perform(click())

        // Espera a que la actividad termine
        scenario.onActivity {
            assertTrue(it.isFinishing)
        }
    }

    @Test
    fun testStatisticsSavedAfterCorrectAnswer() {
        val intent = Intent(context, AlarmSoundingActivity::class.java).apply {
            putExtra("alarm_name", "Test Alarm")
        }
        val scenario = ActivityScenario.launch<AlarmSoundingActivity>(intent)

        scenario.onActivity { activity ->
            // Configura un problema simulado para el ViewModel
            val problemaSimulado = Problema(
                enunciado = "¿Cuál es la capital de Francia?",
                opciones = listOf("Madrid", "Londres", "París", "Roma"),
                respuestaCorrecta = "París"
            )
            activity.viewModel.setProblemaForTesting(problemaSimulado)
        }

        // Responde correctamente
        onView(withId(R.id.opcion3Button)).perform(click())

        // Verifica que las estadísticas se han guardado
        val statistics = statsRepository.getAllStatistics()
        assertEquals(1, statistics.size)
    }

    @Test
    fun testUIUpdatesAfterIncorrectAnswer() {
        val intent = Intent(context, AlarmSoundingActivity::class.java).apply {
            putExtra("alarm_name", "Test Alarm")
        }
        val scenario = ActivityScenario.launch<AlarmSoundingActivity>(intent)

        scenario.onActivity { activity ->
            // Configura un problema simulado para el ViewModel
            val problemaSimulado = Problema(
                enunciado = "¿Cuánto es 3 x 3?",
                opciones = listOf("6", "9", "12", "15"),
                respuestaCorrecta = "9"
            )
            activity.viewModel.setProblemaForTesting(problemaSimulado)
        }

        // Responde incorrectamente
        onView(withId(R.id.opcion1Button)).perform(click())

        // Verifica que los botones muestran opciones nuevas
        onView(withId(R.id.opcion1Button)).check(matches(not(withText("6"))))
        onView(withId(R.id.opcion2Button)).check(matches(not(withText("9"))))
        onView(withId(R.id.opcion3Button)).check(matches(not(withText("12"))))
        onView(withId(R.id.opcion4Button)).check(matches(not(withText("15"))))
    }

    @Test
    fun testAlarmNameDisplayedCorrectly() {
        val intent = Intent(context, AlarmSoundingActivity::class.java).apply {
            putExtra("alarm_name", "Wake Up Alarm")
        }
        ActivityScenario.launch<AlarmSoundingActivity>(intent).use {
            // Verifica que el nombre de la alarma se muestra correctamente
            onView(withId(R.id.textViewNombreAlarma)).check(matches(withText("Wake Up Alarm")))
        }
    }

}
