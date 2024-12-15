package com.example.myapplication.model

import kotlin.random.Random
import kotlin.math.ceil

data class Problema(
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrecta: String
) {
    // Crear un problema aleatorio: matemático, lógica matemática, o acertijo
    fun crearProblemaAleatorio(random: Random = Random, randomAcertijo: Random = Random): Problema {
        val tipoProblema = random.nextInt(1, 4) // 1 para matemático, 2 para lógica matemática, 3 para acertijo

        return when (tipoProblema) {
            1 -> crearProblemaMatematico(random)
            2 -> crearProblemaLogicaMatematica(random)
            else -> crearAcertijo(randomAcertijo)
        }
    }

    // Generar un problema matemático con varias operaciones
    fun crearProblemaMatematico(random: Random): Problema {
        val tipoOperacion = random.nextInt(1, 6)
        val numero1 = random.nextInt(2, 20)
        val numero2 = random.nextInt(2, 10)

        val (enunciado, resultadoCorrecto) = when (tipoOperacion) {
            1 -> "¿Cuánto es $numero1 + $numero2?" to (numero1 + numero2)
            2 -> "¿Cuánto es $numero1 - $numero2?" to (numero1 - numero2)
            3 -> "¿Cuánto es $numero1 x $numero2?" to (numero1 * numero2)
            4 -> {
                val num1 = numero1 * numero2 // Aseguramos divisibilidad
                "¿Cuánto es $num1 / $numero2?" to (num1 / numero2)
            }
            else -> "¿Cuánto es $numero1 mod $numero2?" to (numero1 % numero2) // Módulo
        }

        val opciones = generarOpciones(resultadoCorrecto.toString(), random)
        return Problema(enunciado, opciones, resultadoCorrecto.toString())
    }

    // Generar un problema de lógica matemática dinámica
    fun crearProblemaLogicaMatematica(random: Random): Problema {
        return when (random.nextInt(1, 10)) {
            1 -> {
                val gallinas = random.nextInt(5, 15)
                val huevosPorDia = random.nextInt(1, 4)
                val dias = random.nextInt(2, 5)
                val totalHuevos = gallinas * huevosPorDia * dias
                Problema(
                    enunciado = "Si en una granja hay $gallinas gallinas y cada una pone $huevosPorDia huevo(s) al día, ¿cuántos huevos pondrán en total en $dias días?",
                    opciones = generarOpciones(totalHuevos.toString(), random),
                    respuestaCorrecta = totalHuevos.toString()
                )
            }
            2 -> {
                val velocidad = random.nextInt(40, 100)
                val distancia = velocidad * random.nextInt(2, 5)
                val tiempo = distancia / velocidad
                Problema(
                    enunciado = "Un tren viaja a una velocidad de $velocidad km/h. ¿Cuántas horas tardará en recorrer $distancia km?",
                    opciones = generarOpciones(tiempo.toString(), random),
                    respuestaCorrecta = "$tiempo"
                )
            }
            3 -> {
                val manzanas = random.nextInt(10, 20)
                val personas = random.nextInt(5, manzanas)
                val manzanasRestantes = manzanas - personas
                Problema(
                    enunciado = "En una caja hay $manzanas manzanas y $personas personas. Si cada persona toma 1 manzana, ¿cuántas manzanas quedan en la caja?",
                    opciones = generarOpciones(manzanasRestantes.toString(), random),
                    respuestaCorrecta = manzanasRestantes.toString()
                )
            }
            4 -> {
                val peldaños = random.nextInt(10, 20)
                val sube = random.nextInt(2, 7)
                val baja = random.nextInt(1, sube - 1)
                val movimientos = ceil(peldaños.toDouble() / (sube - baja)).toInt()
                Problema(
                    enunciado = "Una escalera tiene $peldaños peldaños. Si subes $sube peldaños y bajas $baja, ¿en cuántos movimientos llegarás al último peldaño?",
                    opciones = generarOpciones(movimientos.toString(), random),
                    respuestaCorrecta = movimientos.toString()
                )
            }
            5 -> {
                val bolsas = random.nextInt(3, 10)
                val canicasPorBolsa = random.nextInt(2, 6)
                val totalCanicas = bolsas * canicasPorBolsa
                Problema(
                    enunciado = "Si tienes $bolsas bolsas y cada bolsa contiene $canicasPorBolsa canicas, ¿cuántas canicas tienes en total?",
                    opciones = generarOpciones(totalCanicas.toString(), random),
                    respuestaCorrecta = totalCanicas.toString()
                )
            }
            6 -> {
                val estudiantes = random.nextInt(20, 30)
                val lapicesPorEstudiante = random.nextInt(2, 4)
                val perdidos = random.nextInt(1, estudiantes * lapicesPorEstudiante / 2)
                val lapicesRestantes = estudiantes * lapicesPorEstudiante - perdidos
                Problema(
                    enunciado = "En una clase hay $estudiantes estudiantes y cada uno tiene $lapicesPorEstudiante lápices. Si se pierden $perdidos lápices, ¿cuántos quedan?",
                    opciones = generarOpciones(lapicesRestantes.toString(), random),
                    respuestaCorrecta = lapicesRestantes.toString()
                )
            }
            7 -> {
                val horasPara4 = random.nextInt(10, 20)
                val tareas = random.nextInt(5, 15)
                val horasParaTareas = ceil(horasPara4.toDouble() / 4 * tareas).toInt()
                Problema(
                    enunciado = "Si necesitas $horasPara4 horas para completar 4 tareas, ¿cuántas horas necesitas para $tareas tareas?",
                    opciones = generarOpciones(horasParaTareas.toString(), random),
                    respuestaCorrecta = horasParaTareas.toString()
                )
            }
            8 -> {
                val globos = random.nextInt(10, 20)
                val explotan = random.nextInt(1, globos)
                val restantes = globos - explotan
                Problema(
                    enunciado = "Si tienes $globos globos y $explotan explota(n), ¿cuántos globos te quedan?",
                    opciones = generarOpciones(restantes.toString(), random),
                    respuestaCorrecta = restantes.toString()
                )
            }
            9 -> {
                val piscinaPorMinuto = random.nextInt(3, 10)
                val horas = random.nextInt(2, 5)
                val totalLitros = piscinaPorMinuto * 60 * horas
                Problema(
                    enunciado = "Una piscina se llena con $piscinaPorMinuto litros de agua por minuto. ¿Cuántos litros se llenarán en $horas horas?",
                    opciones = generarOpciones(totalLitros.toString(), random),
                    respuestaCorrecta = totalLitros.toString()
                )
            }
            else -> throw IllegalStateException("No se pudo generar un problema")
        }
    }

    // Generar un acertijo
    fun crearAcertijo(random: Random): Problema {
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

        return acertijos[random.nextInt(acertijos.size)]
    }

    // Generar opciones de respuesta con valores aleatorios alrededor de la respuesta correcta
    fun generarOpciones(respuestaCorrecta: String, random: Random): List<String> {
        val opciones = mutableListOf(respuestaCorrecta.toInt())
        while (opciones.size < 4) {
            val opcionAleatoria = (respuestaCorrecta.toInt() + random.nextInt(-5, 6)).coerceAtLeast(0)
            if (opcionAleatoria !in opciones) {
                opciones.add(opcionAleatoria)
            }
        }
        opciones.shuffle()
        return opciones.map { it.toString() }
    }
}
