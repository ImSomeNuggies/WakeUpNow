package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.widget.TimePicker
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.UiController
import android.view.View
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.R
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.repository.AlarmRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.util.*

@RunWith(AndroidJUnit4::class)
class CreateAlarmActivityTest {

    private lateinit var context: Context
    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var alarmRepository: AlarmRepository

    @Before
    fun setUp() {
        context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
        alarmPreferences = AlarmPreferences(sharedPreferences)
        alarmRepository = AlarmRepository(alarmPreferences)

        // Limpia todas las alarmas antes de las pruebas
        sharedPreferences.edit().clear().apply()
    }

    @Test
    fun testInitialUIState() {
        val scenario = ActivityScenario.launch(CreateAlarmActivity::class.java)

        scenario.use {
            onView(withId(R.id.editTextNombre)).check(matches(isDisplayed()))
            onView(withId(R.id.periodicidadTypeSpinner)).check(matches(isDisplayed()))
            onView(withId(R.id.problemaTypeSpinner)).check(matches(isDisplayed()))
            onView(withId(R.id.timePicker)).check(matches(isDisplayed()))
            onView(withId(R.id.buttonConfirmar)).check(matches(isDisplayed()))
            onView(withId(R.id.buttonBack)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testCreateAlarmAndVerifySavedData() {
        val scenario = ActivityScenario.launch(CreateAlarmActivity::class.java)

        scenario.use {
            // Introduce un nombre para la alarma
            onView(withId(R.id.editTextNombre)).perform(typeText("Wake Up Alarm"), closeSoftKeyboard())

            // Selecciona un valor en el spinner de periodicidad
            onView(withId(R.id.periodicidadTypeSpinner)).perform(click())
            onView(withText("Diaria")).perform(click())

            // Selecciona un valor en el spinner de problema
            onView(withId(R.id.problemaTypeSpinner)).perform(click())
            onView(withText("Problema corto")).perform(click())

            // Configura la hora del TimePicker
            setTimeOnTimePicker(7, 30)

            // Confirma la alarma
            onView(withId(R.id.buttonConfirmar)).perform(click())

            // Verifica que la alarma se ha guardado correctamente en el repositorio
            val alarms = alarmRepository.getAlarms()
            assertEquals(1, alarms.size)

            val savedAlarm = alarms[0]
            assertEquals("Wake Up Alarm", savedAlarm.name)
            assertEquals("07:30", savedAlarm.time)
            assertEquals("Diaria", savedAlarm.periodicity)
            assertEquals("Problema corto", savedAlarm.problem)
        }
    }

    @Test
    fun testBackButtonNavigatesToMainActivity() {
        // Lanza la actividad de CreateAlarmActivity
        val intent = Intent(context, CreateAlarmActivity::class.java)
        val scenario = ActivityScenario.launch<CreateAlarmActivity>(intent)

        // Verifica que el botón de "atrás" está visible
        onView(withId(R.id.buttonBack))
            .check(matches(isDisplayed()))

        // Haz clic en el botón de "atrás"
        onView(withId(R.id.buttonBack)).perform(click())

        // Verifica que se ha regresado a MainActivity
        scenario.onActivity { activity ->
            assertTrue(activity.isFinishing)
        }

        // Verifica que MainActivity está visible
        onView(withId(R.id.buttonCreateAlarm)).check(matches(isDisplayed()))
    }


    private fun setTimeOnTimePicker(hour: Int, minute: Int) {
        onView(withId(R.id.timePicker)).perform(object : ViewAction {
            override fun getConstraints() = allOf(isAssignableFrom(TimePicker::class.java))
            override fun getDescription() = "Set time on TimePicker to $hour:$minute"
            override fun perform(uiController: androidx.test.espresso.UiController?, view: android.view.View?) {
                val timePicker = view as TimePicker
                timePicker.hour = hour
                timePicker.minute = minute
            }
        })
    }
}
