package com.example.myapplication.view

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.R
import com.example.myapplication.viewmodel.StatisticsViewModel
import com.example.myapplication.repository.AlarmStatsRepository
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class StatisticsActivityTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testInitialUIState() {
        val intent = Intent(context, StatisticsActivity::class.java)

        ActivityScenario.launch<StatisticsActivity>(intent).use {
            // Verificar que los TextViews están inicializados correctamente
            onView(withId(R.id.avgTimeValue)).check(matches(isDisplayed()))
            onView(withId(R.id.maxTimeValue)).check(matches(isDisplayed()))
            onView(withId(R.id.minTimeValue)).check(matches(isDisplayed()))
            onView(withId(R.id.maxErrorsValue)).check(matches(isDisplayed()))

            // Verificar que el botón de regreso está visible
            onView(withId(R.id.buttonBack)).check(matches(isDisplayed()))

            // Verificar que el spinner está visible
            onView(withId(R.id.graphTypeSpinner)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testChartViewDisplaysDefaultData() {
        val intent = Intent(context, StatisticsActivity::class.java)

        ActivityScenario.launch<StatisticsActivity>(intent).use { _ ->
            // Verificar que el `BarChartView` muestra los datos correctamente
            onView(withId(R.id.barChartView)).check(matches(isDisplayed()))
            // Nota: Para verificar los datos dentro del gráfico, necesitarías interactuar con su lógica.
        }
    }



    @Test
    fun testBackButtonClosesActivity() {
        val intent = Intent(context, StatisticsActivity::class.java)

        ActivityScenario.launch<StatisticsActivity>(intent).use { scenario ->
            // Haz clic en el botón de regreso
            onView(withId(R.id.buttonBack)).perform(click())

            // Verifica que la actividad se cierra
            scenario.onActivity {
                assertTrue(it.isFinishing)
            }
        }
    }
}
