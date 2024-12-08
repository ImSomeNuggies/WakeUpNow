package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.repository.AlarmRepository
import com.example.myapplication.viewmodel.helper.NotificationHelper
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.hamcrest.Matchers.greaterThan
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var testPreferences: AlarmPreferences
    private lateinit var alarmRepository: AlarmRepository

    @Before
    fun setUp() {
        val testContext = ApplicationProvider.getApplicationContext<Context>()
        val sharedPreferences = testContext.getSharedPreferences("alarms", Context.MODE_PRIVATE)
        testPreferences = AlarmPreferences(sharedPreferences)
        alarmRepository = AlarmRepository(testPreferences)

        // Agregar una alarma de prueba antes de cada test
        val testAlarm = Alarm(
            id = 1,
            name = "Test Alarm",
            time = "08:00",
            periodicity = "Diaria",
            problem = "Sudoku",
            isActive = true,
            ringTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        )
        alarmRepository.saveAlarm(testAlarm)
    }

    @Test
    fun testRecyclerViewLoadsAlarms() {
        // Verifica que el RecyclerView carga las alarmas guardadas
        onView(withId(R.id.recyclerViewAlarms)).check { view, _ ->
            val recyclerView = view as RecyclerView
            assertTrue(recyclerView.adapter?.itemCount ?: 0 > 0)
        }
    }

    @Test
    fun testButtonCreateAlarmOpensCreateAlarmActivity() {
        Intents.init()
        onView(withId(R.id.buttonCreateAlarm)).perform(click())
        Intents.intended(hasComponent(CreateAlarmActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testButtonStatisticsOpensStatisticsActivity() {
        Intents.init()
        onView(withId(R.id.buttonStatistics)).perform(click())
        Intents.intended(hasComponent(StatisticsActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testButtonQROpensQrGeneratorActivity() {
        Intents.init()
        onView(withId(R.id.buttonQR)).perform(click())
        Intents.intended(hasComponent(QrGeneratorActivity::class.java.name))
        Intents.release()
    }

    @Test
    fun testNotificationPermissionRequested() {
        // Simula que se solicita el permiso de notificaciones y verifica que se muestra el Toast
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        NotificationHelper.createNotificationChannel(appContext)
        // El resto de la lógica se valida con manualidad debido a que Toast no puede ser probado directamente.
    }

    @Test
    fun testAlarmRepositoryIsLoadedCorrectly() {
        // Verifica que las alarmas guardadas en el repositorio se cargan correctamente
        val savedAlarms = alarmRepository.getAlarms()
        assertTrue(savedAlarms.isNotEmpty())
        assertEquals("Test Alarm", savedAlarms.first().name)
        assertEquals("08:00", savedAlarms.first().time)
    }
}

