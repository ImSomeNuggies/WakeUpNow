package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.R
import com.journeyapps.barcodescanner.ScanOptions
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class QRSoundingActivityTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testInitialUIState() {
        val intent = Intent(context, QRSoundingActivity::class.java).apply {
            putExtra("alarm_name", "QR Alarm")
        }

        ActivityScenario.launch<QRSoundingActivity>(intent).use {
            // Verifica que el nombre de la alarma es correcto
            onView(withId(R.id.textViewNombreAlarma)).check(matches(withText("QR Alarm")))

            // Verifica que la hora actual no está vacía
            onView(withId(R.id.textViewHoraActual)).check(matches(not(withText(""))))

            // Verifica que el botón para detener está presente
            onView(withId(R.id.stopButton)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testShouldFinishClosesActivity() {
        val intent = Intent(context, QRSoundingActivity::class.java)

        ActivityScenario.launch<QRSoundingActivity>(intent).use { scenario ->
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
}
