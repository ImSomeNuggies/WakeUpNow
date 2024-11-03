package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.viewmodels.AlarmSoundingViewModel
import com.example.myapplication.viewmodels.AlarmSoundingViewModelFactory
import com.example.myapplication.receivers.AlarmReceiver
import kotlin.random.Random
import kotlin.math.*

class AlarmSoundingActivity : AppCompatActivity() {

    lateinit var problemaTextView: TextView
    lateinit var opcion1Button: Button
    lateinit var opcion2Button: Button
    lateinit var opcion3Button: Button
    lateinit var opcion4Button: Button
    lateinit var problemaFalloTextView: TextView
    private lateinit var textViewNombreAlarma: TextView
    private lateinit var textViewHoraActual: TextView

    // Usamos el ViewModel con un ViewModelFactory
    private val viewModel: AlarmSoundingViewModel by viewModels { AlarmSoundingViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_sounding)

        // Inicializar los elementos de la UI
        problemaTextView = findViewById(R.id.problemaTextView)
        opcion1Button = findViewById(R.id.opcion1Button)
        opcion2Button = findViewById(R.id.opcion2Button)
        opcion3Button = findViewById(R.id.opcion3Button)
        opcion4Button = findViewById(R.id.opcion4Button)
        problemaFalloTextView = findViewById(R.id.problemaFallo)
        textViewNombreAlarma = findViewById(R.id.textViewNombreAlarma)
        textViewHoraActual = findViewById(R.id.textViewHoraActual)

        // Obtener el nombre de la alarma desde el Intent y asignarlo al TextView
        val alarmName = intent.getStringExtra("alarm_name")
        textViewNombreAlarma.text = alarmName ?: ""

        // Mostrar la hora actual
        val currentTime = viewModel.getCurrentTime()
        textViewHoraActual.text = currentTime

        // Generar y mostrar un problema aleatorio
        generarProblemaAleatorio()
    }


    // Generar un problema aleatorio (matemático, lógica matemática o acertijo)
    fun generarProblemaAleatorio() {
        val problema = crearProblemaAleatorio()

        // Mostrar el problema en la UI
        problemaTextView.text = problema.enunciado
        opcion1Button.text = problema.opciones[0]
        opcion2Button.text = problema.opciones[1]
        opcion3Button.text = problema.opciones[2]
        opcion4Button.text = problema.opciones[3]

        // Asignar las acciones de los botones
        opcion1Button.setOnClickListener { verificarRespuesta(problema.opciones[0], problema.respuestaCorrecta) }
        opcion2Button.setOnClickListener { verificarRespuesta(problema.opciones[1], problema.respuestaCorrecta) }
        opcion3Button.setOnClickListener { verificarRespuesta(problema.opciones[2], problema.respuestaCorrecta) }
        opcion4Button.setOnClickListener { verificarRespuesta(problema.opciones[3], problema.respuestaCorrecta) }
    }

    // Verificar si la respuesta seleccionada es correcta
    fun verificarRespuesta(respuestaSeleccionada: String, respuestaCorrecta: String) {
        if (respuestaSeleccionada == respuestaCorrecta) {
            // Mostrar que la respuesta es correcta y terminar la actividad
            AlarmReceiver.stopAlarm()
            finish()
        } else {
            // Mostrar que la respuesta es incorrecta
            problemaFalloTextView.text = "Respuesta incorrecta. Inténtalo de nuevo."
        }
    }

    // Crear un problema aleatorio: matemático, lógica matemática, o acertijo
    fun crearProblemaAleatorio(): Problema {
        val tipoProblema = Random.nextInt(1, 4) // 1 para matemático, 2 para lógica matemática, 3 para acertijo

        return when (tipoProblema) {
            1 -> crearProblemaMatematico()
            2 -> crearProblemaLogicaMatematica()
            else -> crearAcertijo()
        }
    }

    // Generar un problema matemático con varias operaciones
    fun crearProblemaMatematico(): Problema {
        val tipoOperacion = Random.nextInt(1, 6)
        val numero1 = Random.nextInt(1, 20)
        val numero2 = Random.nextInt(1, 10) // En operaciones como raíz o logaritmo se usará solo numero1

        val (enunciado, resultadoCorrecto) = when (tipoOperacion) {
            1 -> "¿Cuánto es $numero1 + $numero2?" to (numero1 + numero2)
            2 -> "¿Cuánto es $numero1 - $numero2?" to (numero1 - numero2)
            3 -> "¿Cuánto es $numero1 * $numero2?" to (numero1 * numero2)
            4 -> {
                val num1 = numero1 * numero2 // Aseguramos divisibilidad
                "¿Cuánto es $num1 / $numero2?" to (num1 / numero2)
            }
            else -> "¿Cuánto es $numero1 % $numero2?" to (numero1 % numero2) // Módulo
        }

        val opciones = generarOpciones(resultadoCorrecto.toString())
        return Problema(enunciado, opciones, resultadoCorrecto.toString())
    }

    // Generar un problema de lógica matemática
    fun crearProblemaLogicaMatematica(): Problema {
        val problemasLogica = listOf(
            Problema(
                "Si en una granja hay 10 gallinas y cada una pone 2 huevos al día, ¿cuántos huevos pondrán en total en 3 días?",
                listOf("60", "30", "20", "50"),
                "60"
            ),
            Problema(
                "Un tren viaja a una velocidad de 80 km/h. ¿Cuánto tiempo tardará en recorrer 160 km?",
                listOf("1 hora", "2 horas", "3 horas", "4 horas"),
                "2 horas"
            ),
            Problema(
                "En una caja hay 15 manzanas y 9 personas. Si cada persona toma 1 manzana, ¿cuántas manzanas quedan en la caja?",
                listOf("5", "6", "7", "8"),
                "6"
            ),
            Problema(
                "Una escalera tiene 15 peldaños. Si subes 3 peldaños y bajas 2, ¿en cuántos movimientos llegarás al último peldaño?",
                listOf("12", "15", "14", "13"),
                "13"
            ),
            Problema(
                "Si tienes 5 bolsas y cada bolsa contiene 4 canicas, ¿cuántas canicas tienes en total?",
                listOf("10", "20", "25", "30"),
                "20"
            ),

            Problema(
                "En un zoológico hay 12 jaulas con 4 animales en cada una. Si 8 animales son trasladados, ¿cuántos quedan?",
                listOf("40", "44", "48", "46"),
                "44"
            ),
            Problema(
                "Un coche viaja a 60 km/h y otro a 80 km/h. Si salen al mismo tiempo, ¿cuánto tiempo después estarán a 40 km de distancia?",
                listOf("2 horas", "1 hora", "30 minutos", "3 horas"),
                "2 horas"
            ),
            Problema(
                "Tienes 3 bolsas con 5 canicas en cada una. Si pierdes 4 canicas, ¿cuántas te quedan?",
                listOf("11", "10", "15", "12"),
                "11"
            ),
            Problema(
                "En una clase hay 24 estudiantes y cada uno tiene 2 lápices. Si se pierden 10 lápices, ¿cuántos quedan?",
                listOf("38", "44", "40", "34"),
                "38"
            ),
            Problema(
                "Si un tren recorre 150 km en 3 horas, ¿a qué velocidad va en promedio?",
                listOf("50 km/h", "60 km/h", "70 km/h", "45 km/h"),
                "50 km/h"
            ),
            Problema(
                "Si tienes 8 manzanas y das la mitad, ¿cuántas te quedan?",
                listOf("4", "6", "2", "8"),
                "4"
            ),
            Problema(
                "Si cada hoja de un libro tiene 2 caras, ¿cuántas caras tiene un libro de 100 hojas?",
                listOf("200", "100", "150", "50"),
                "200"
            ),
            Problema(
                "Si un camión carga 25 cajas y cada caja pesa 4 kg, ¿cuál es el peso total de las cajas?",
                listOf("100 kg", "120 kg", "80 kg", "90 kg"),
                "100 kg"
            ),
            Problema(
                "Si tienes 6 cajas y cada una tiene 7 botellas, ¿cuántas botellas tienes en total?",
                listOf("42", "36", "48", "45"),
                "42"
            ),
            Problema(
                "Un jardín tiene 8 filas de 4 plantas cada una. Si se quitan 5 plantas, ¿cuántas quedan?",
                listOf("27", "32", "28", "30"),
                "27"
            ),
            Problema(
                "Si un reloj avanza 10 minutos cada hora, ¿cuánto adelantará en un día completo?",
                listOf("4 horas", "2 horas", "3 horas", "5 horas"),
                "4 horas"
            ),
            Problema(
                "Un grupo de 10 personas planea dividirse en parejas. ¿Cuántas parejas se pueden formar?",
                listOf("5", "6", "4", "10"),
                "5"
            ),
            Problema(
                "Si necesitas 12 horas para completar 4 tareas, ¿cuántas horas necesitas para 10 tareas?",
                listOf("30", "20", "25", "15"),
                "30"
            ),
            Problema(
                "Una piscina se llena con 5 litros de agua por minuto. ¿Cuántos litros se llenarán en 3 horas?",
                listOf("900", "300", "1200", "1500"),
                "900"
            ),
            Problema(
                "Si tienes 15 globos y 8 explotan, ¿cuántos globos te quedan?",
                listOf("7", "8", "10", "5"),
                "7"
            ),
            Problema(
                "Si hay 9 estudiantes y cada uno recibe 3 galletas, ¿cuántas galletas se necesitan en total?",
                listOf("27", "24", "30", "18"),
                "27"
            ),
            Problema(
                "Una caja contiene 20 lápices. Si regalas 8, ¿cuántos lápices te quedan?",
                listOf("12", "15", "10", "8"),
                "12"
            ),
            Problema(
                "Si un tren viaja a 90 km/h y el viaje es de 360 km, ¿cuánto tardará en llegar?",
                listOf("4 horas", "5 horas", "3 horas", "6 horas"),
                "4 horas"
            ),
            Problema(
                "Un reloj avanza 15 minutos cada hora. ¿Cuánto adelantará en un periodo de 12 horas?",
                listOf("3 horas", "4 horas", "2 horas", "6 horas"),
                "3 horas"
            )
        )
        return problemasLogica.random()
    }

    // Generar un acertijo
    fun crearAcertijo(): Problema {
        val acertijos = listOf(
            Problema(
                "Cuanto más grande, menos se ve. ¿Qué es?",
                listOf("La oscuridad", "El amor", "La luna", "El miedo"),
                "La oscuridad"
            ),
            Problema(
                "No es planta ni animal, y muere en el agua y vive en el aire. ¿Qué es?",
                listOf("El humo", "El fuego", "El vapor", "El trueno"),
                "El fuego"
            ),
            Problema(
                "Si me nombras, desaparezco. ¿Qué soy?",
                listOf("El viento", "La oscuridad", "El silencio", "La sombra"),
                "El silencio"
            ),
            Problema(
                "No tengo pies, pero corro. ¿Qué soy?",
                listOf("El viento", "El río", "El tiempo", "Una nube"),
                "El río"
            ),
            Problema(
                "Es tuyo, pero tus amigos lo usan más que tú. ¿Qué es?",
                listOf("El tiempo", "Tu voz", "Tu nombre", "Tu dinero"),
                "Tu nombre"
            ),
            Problema(
                "Mientras más quitas, más grande se vuelve. ¿Qué es?",
                listOf("Un agujero", "El fuego", "Una sombra", "El agua"),
                "Un agujero"
            ),
            Problema(
                "Es algo que todos pueden abrir fácilmente, pero nadie puede cerrar. ¿Qué es?",
                listOf("La mente", "Una puerta", "Una botella", "Un regalo"),
                "La mente"
            ),
            Problema(
                "Va y viene, pero nunca se mueve. ¿Qué es?",
                listOf("El sol", "El mar", "El tiempo", "El viento"),
                "El tiempo"
            ),
            Problema(
                "Tiene ojos pero no puede ver. ¿Qué es?",
                listOf("Una tormenta", "Un pez", "Un libro", "Un árbol"),
                "Una tormenta"
            ),
            Problema(
                "Tiene cuello, pero no cabeza. ¿Qué es?",
                listOf("Una botella", "Un reloj", "Un cisne", "Un árbol"),
                "Una botella"
            ),
            Problema(
                "Tiene muchas teclas, pero no puede abrir ninguna puerta. ¿Qué es?",
                listOf("Un piano", "Un teléfono", "Un coche", "Una cerradura"),
                "Un piano"
            ),
            Problema(
                "Tengo ciudades pero no casas, montañas pero no árboles, y agua pero no peces. ¿Qué soy?",
                listOf("Un mapa", "Un libro", "Un cuadro", "Un videojuego"),
                "Un mapa"
            ),
            Problema(
                "Cuanto más seco, más moja. ¿Qué es?",
                listOf("Una toalla", "Un lago", "La esponja", "Una nube"),
                "Una toalla"
            ),
            Problema(
                "Tiene patas pero no puede caminar. ¿Qué es?",
                listOf("Una mesa", "Un perro", "Una silla", "Un lápiz"),
                "Una mesa"
            ),
            Problema(
                "Todo el mundo lo puede ver, pero nadie lo puede tocar. ¿Qué es?",
                listOf("El cielo", "La sombra", "El tiempo", "El sol"),
                "El sol"
            ),
            Problema(
                "Vivo solo en la oscuridad, pero muero si la luz me toca. ¿Qué soy?",
                listOf("La sombra", "El viento", "Un murciélago", "La luna"),
                "La sombra"
            ),
            Problema(
                "Cuanto más tienes, menos ves. ¿Qué es?",
                listOf("La oscuridad", "La luz", "El agua", "El amor"),
                "La oscuridad"
            ),
            Problema(
                "No tengo vida, pero puedo crecer. No tengo pulmones, pero necesito aire. ¿Qué soy?",
                listOf("El fuego", "El árbol", "El agua", "El viento"),
                "El fuego"
            ),
            Problema(
                "Puede viajar por el mundo sin salir de su rincón. ¿Qué es?",
                listOf("Un sello", "El internet", "El reloj", "La luna"),
                "Un sello"
            ),
            Problema(
                "Tiene raíces invisibles, es más alta que un árbol, y nunca crece. ¿Qué es?",
                listOf("Una montaña", "Un río", "Una nube", "Un lago"),
                "Una montaña"
            ),
            Problema(
                "A veces está arriba, a veces está abajo, pero no se mueve. ¿Qué es?",
                listOf("La temperatura", "El río", "La luna", "El mar"),
                "La temperatura"
            ),
            Problema(
                "Tiene agua, pero no es un río. Tiene picos, pero no es una montaña. ¿Qué es?",
                listOf("Un iceberg", "Una ola", "Una nube", "Un desierto"),
                "Un iceberg"
            ),
            Problema(
                "Tiene dientes pero no puede comer. ¿Qué es?",
                listOf("Un peine", "Un cepillo", "Una sierra", "Un cuchillo"),
                "Un peine"
            ),
            Problema(
                "Es ligero como una pluma, pero ni el hombre más fuerte del mundo puede sostenerlo por mucho tiempo. ¿Qué es?",
                listOf("El aliento", "El aire", "La sombra", "El agua"),
                "El aliento"
            ),
            Problema(
                "No se puede ver, pero puede hacer volar casas y destruir montañas. ¿Qué es?",
                listOf("El viento", "El fuego", "El tiempo", "La tormenta"),
                "El viento"
            )
        )

        return acertijos.random()
    }

    // Generar opciones de respuesta con valores aleatorios alrededor de la respuesta correcta
    fun generarOpciones(respuestaCorrecta: String): List<String> {
        val opciones = mutableListOf(respuestaCorrecta.toInt())
        while (opciones.size < 4) {
            val opcionAleatoria = respuestaCorrecta.toInt() + Random.nextInt(-10, 10)
            if (opcionAleatoria !in opciones) {
                opciones.add(opcionAleatoria)
            }
        }
        opciones.shuffle()
        return opciones.map { it.toString() }
    }


}
