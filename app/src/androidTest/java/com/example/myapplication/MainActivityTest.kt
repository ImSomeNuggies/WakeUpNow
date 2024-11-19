// Importa las librerías necesarias
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.myapplication.CreateAlarm
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

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
}
