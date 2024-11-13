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

    // Generar un problema de lógica matemática dinámico
    fun crearProblemaLogicaMatematica(): Problema {
        return when (Random.nextInt(1, 11)) {
            1 -> {
                val gallinas = Random.nextInt(5, 15)
                val huevosPorDia = Random.nextInt(1, 4)
                val dias = Random.nextInt(2, 5)
                val totalHuevos = gallinas * huevosPorDia * dias
                Problema(
                    enunciado = "Si en una granja hay $gallinas gallinas y cada una pone $huevosPorDia huevos al día, ¿cuántos huevos pondrán en total en $dias días?",
                    opciones = generarOpciones(totalHuevos.toString()),
                    respuestaCorrecta = totalHuevos.toString()
                )
            }
            2 -> {
                val velocidad = Random.nextInt(40, 100)
                val distancia = velocidad * Random.nextInt(1, 5)
                val tiempo = distancia / velocidad
                Problema(
                    enunciado = "Un tren viaja a una velocidad de $velocidad km/h. ¿Cuánto tiempo tardará en recorrer $distancia km?",
                    opciones = generarOpciones(tiempo.toString()),
                    respuestaCorrecta = "$tiempo horas"
                )
            }
            3 -> {
                val manzanas = Random.nextInt(10, 20)
                val personas = Random.nextInt(5, manzanas)
                val manzanasRestantes = manzanas - personas
                Problema(
                    enunciado = "En una caja hay $manzanas manzanas y $personas personas. Si cada persona toma 1 manzana, ¿cuántas manzanas quedan en la caja?",
                    opciones = generarOpciones(manzanasRestantes.toString()),
                    respuestaCorrecta = manzanasRestantes.toString()
                )
            }
            4 -> {
                val peldaños = Random.nextInt(10, 20)
                val sube = Random.nextInt(2, 5)
                val baja = Random.nextInt(1, sube)
                val movimientos = peldaños / (sube - baja)
                Problema(
                    enunciado = "Una escalera tiene $peldaños peldaños. Si subes $sube peldaños y bajas $baja, ¿en cuántos movimientos llegarás al último peldaño?",
                    opciones = generarOpciones(movimientos.toString()),
                    respuestaCorrecta = movimientos.toString()
                )
            }
            5 -> {
                val bolsas = Random.nextInt(3, 10)
                val canicasPorBolsa = Random.nextInt(2, 6)
                val totalCanicas = bolsas * canicasPorBolsa
                Problema(
                    enunciado = "Si tienes $bolsas bolsas y cada bolsa contiene $canicasPorBolsa canicas, ¿cuántas canicas tienes en total?",
                    opciones = generarOpciones(totalCanicas.toString()),
                    respuestaCorrecta = totalCanicas.toString()
                )
            }
            6 -> {
                val estudiantes = Random.nextInt(20, 30)
                val lapicesPorEstudiante = Random.nextInt(1, 3)
                val perdidos = Random.nextInt(1, estudiantes * lapicesPorEstudiante / 2)
                val lapicesRestantes = estudiantes * lapicesPorEstudiante - perdidos
                Problema(
                    enunciado = "En una clase hay $estudiantes estudiantes y cada uno tiene $lapicesPorEstudiante lápices. Si se pierden $perdidos lápices, ¿cuántos quedan?",
                    opciones = generarOpciones(lapicesRestantes.toString()),
                    respuestaCorrecta = lapicesRestantes.toString()
                )
            }
            7 -> {
                val horasPara4 = Random.nextInt(10, 20)
                val tareas = Random.nextInt(5, 15)
                val horasParaTareas = horasPara4 / 4 * tareas
                Problema(
                    enunciado = "Si necesitas $horasPara4 horas para completar 4 tareas, ¿cuántas horas necesitas para $tareas tareas?",
                    opciones = generarOpciones(horasParaTareas.toString()),
                    respuestaCorrecta = horasParaTareas.toString()
                )
            }
            8 -> {
                val globos = Random.nextInt(10, 20)
                val explotan = Random.nextInt(1, globos)
                val restantes = globos - explotan
                Problema(
                    enunciado = "Si tienes $globos globos y $explotan explotan, ¿cuántos globos te quedan?",
                    opciones = generarOpciones(restantes.toString()),
                    respuestaCorrecta = restantes.toString()
                )
            }
            9 -> {
                val velocidad = Random.nextInt(30, 70)
                val tiempoHoras = Random.nextInt(1, 4)
                val distancia = velocidad * tiempoHoras
                Problema(
                    enunciado = "Un tren viaja a $velocidad km/h. ¿Cuánto tardará en recorrer $distancia km?",
                    opciones = generarOpciones(tiempoHoras.toString()),
                    respuestaCorrecta = "$tiempoHoras horas"
                )
            }
            10 -> {
                val piscinaPorMinuto = Random.nextInt(3, 10)
                val horas = Random.nextInt(1, 5)
                val totalLitros = piscinaPorMinuto * 60 * horas
                Problema(
                    enunciado = "Una piscina se llena con $piscinaPorMinuto litros de agua por minuto. ¿Cuántos litros se llenarán en $horas horas?",
                    opciones = generarOpciones(totalLitros.toString()),
                    respuestaCorrecta = totalLitros.toString()
                )
            }
            else -> throw IllegalStateException("No se pudo generar un problema")
        }
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
            val opcionAleatoria = (respuestaCorrecta.toInt() + Random.nextInt(-5, 6)).coerceAtLeast(0)
            if (opcionAleatoria !in opciones) {
                opciones.add(opcionAleatoria)
            }
        }
        opciones.shuffle()
        return opciones.map { it.toString() }
    }
}
