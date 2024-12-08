package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.R
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.repository.AlarmRepository
import com.example.myapplication.viewmodel.EditAlarmViewModel
import com.example.myapplication.viewmodel.factory.EditAlarmViewModelFactory
import org.hamcrest.Matchers.allOf
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.util.*

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class EditAlarmActivityTest {

    private lateinit var context: Context
    private lateinit var alarmRepository: AlarmRepository
    private lateinit var alarmPreferences: AlarmPreferences

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val alarmPreferences = AlarmPreferences(sharedPreferences)
        alarmRepository = AlarmRepository(alarmPreferences)

        // Prepara una alarma para la prueba
        val ringTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val alarm = Alarm(
            id = 1,
            name = "Test Alarm",
            time = "08:30",
            periodicity = "Diaria",
            problem = "QR scanner",
            isActive = true,
            ringTime = ringTime
        )

        alarmRepository.saveAlarm(alarm)
    }


    @Test
    fun testUIInitialization() {
        val intent = Intent(context, EditAlarmActivity::class.java).apply {
            putExtra("alarm_id", 1)
        }
        ActivityScenario.launch<EditAlarmActivity>(intent).use {

            // Verifica los elementos de la UI
            onView(withId(R.id.editTextNombre)).check(matches(withText("Test Alarm")))

            onView(withId(R.id.timePicker)).check { view, _ ->
                val timePicker = view as TimePicker
                assertEquals(8, timePicker.hour)
                assertEquals(30, timePicker.minute)
            }

            onView(withId(R.id.periodicidadTypeSpinner)).check(matches(withSpinnerText("Diaria")))
            onView(withId(R.id.problemaTypeSpinner)).check(matches(withSpinnerText("QR scanner")))
        }
    }


    @Test
    fun testUpdateAlarm() {
        val intent = Intent(context, EditAlarmActivity::class.java).apply {
            putExtra("alarm_id", 1) // ID de la alarma cargada en el ViewModel
        }
        ActivityScenario.launch<EditAlarmActivity>(intent).use {
            // Cambia el nombre de la alarma
            onView(withId(R.id.editTextNombre)).perform(clearText(), typeText("Updated Alarm"), closeSoftKeyboard())

            // Cambia la hora en el TimePicker
            setTimeOnTimePicker(10, 15)

            // Cambia la periodicidad
            onView(withId(R.id.periodicidadTypeSpinner)).perform(click())
            onView(withText("Una vez")).perform(click())

            // Cambia el tipo de problema
            onView(withId(R.id.problemaTypeSpinner)).perform(click())
            onView(withText("Sudoku")).perform(click())

            // Verifica que el botón de confirmar está visible
            onView(withId(R.id.buttonConfirmar)).check(matches(isDisplayed()))

            // Haz clic en "Confirmar"
            onView(withId(R.id.buttonConfirmar)).perform(click())

            // Verifica que la alarma fue actualizada en el repositorio
            val updatedAlarm = alarmRepository.getAlarmById(1)
            assertNotNull(updatedAlarm)
            assertEquals("Updated Alarm", updatedAlarm!!.name)
            assertEquals("10:15", updatedAlarm.time)
            assertEquals("Una vez", updatedAlarm.periodicity)
            assertEquals("Sudoku", updatedAlarm.problem)
        }
    }

    @Test
    fun testBackButtonNavigatesToMainActivity() {
        val intent = Intent(context, EditAlarmActivity::class.java).apply {
            putExtra("alarm_id", 1) // ID de la alarma cargada en el ViewModel
        }
        ActivityScenario.launch<EditAlarmActivity>(intent).use {
            // Haz clic en el botón "Atrás"
            onView(withId(R.id.buttonBack)).perform(click())

            // Verifica que la actividad actual ha finalizado
            it.onActivity { activity ->
                assert(activity.isFinishing)
            }
        }
    }

    // Helper para establecer el tiempo en el TimePicker
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
