// Importa las librerías necesarias
import android.content.Context
import android.media.audiofx.BassBoost
import android.view.View
import android.widget.TimePicker
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.Alarm
import com.example.myapplication.AlarmPreferences
import com.example.myapplication.AlarmRepository
import com.example.myapplication.CreateAlarm
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.Calendar
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import java.util.regex.Matcher


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun testButtonCreateAlarmOpensCreateAlarmActivity() {
        // Inicializa Espresso Intents para verificar la navegación
        Intents.init()
        // Realiza clic en el botón para crear alarma
        onView(withId(R.id.buttonCreateAlarm)).perform(click())
        // Verifica que el intent para abrir CreateAlarm se ha lanzado
        Intents.intended(hasComponent(CreateAlarm::class.java.name))
        // Libera Espresso Intents después de la prueba
        Intents.release()
    }

    @Test
    fun alarmCreationFlowShouldResultInValidAlarm() {
        // Inicializa un contexto aislado para SharedPreferences de pruebas
        val testContext = ApplicationProvider.getApplicationContext<Context>()
        val testPreferences = AlarmPreferences(testContext)
        val alarmRepository = AlarmRepository(testPreferences)

        // Configurar el flujo de creación de una alarma
        onView(withId(R.id.buttonCreateAlarm)).perform(click())

        // Configurar el TimePicker directamente
        //onView(withId(R.id.timePicker)).perform(setTime(8, 0))
        //onView(withId(R.id.timePicker)).perform(click())

        // Espera que el TimePicker se abra
        //device.wait(Until.hasObject(By.desc("TimePicker")), 5000)

        // Interactuar con el TimePicker para seleccionar 08:00
        //device.findObject(By.res("android:id/hours")).text = "08"
        //device.findObject(By.res("android:id/minutes")).text = "00"

        // Clic en el botón "OK" para confirmar la hora
        device.findObject(By.res("android:id/button1")).click()
        //onView(withId(R.id.selectedTimeText)).perform(typeText("08:00"))
        onView(withId(R.id.editTextNombre)).perform(typeText("Wake Up"))
        onView(withId(R.id.buttonConfirmar)).perform(click())

        // Valida que la alarma se guardó en las preferencias
        val savedAlarms = alarmRepository.getAlarms()
        assertTrue(savedAlarms.isNotEmpty())

        val lastAlarm = savedAlarms.last()
        assertEquals("08:00", lastAlarm.time)
        assertEquals("Wake Up", lastAlarm.name)
    }

}
