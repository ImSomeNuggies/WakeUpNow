package com.example.myapplication.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast
import com.example.myapplication.viewmodel.helper.NotificationHelper
import com.example.myapplication.model.Alarm
import java.util.Calendar
import com.example.myapplication.repository.AlarmPreferences


class AlarmReceiver : BroadcastReceiver() {
    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var alarmPreferences: AlarmPreferences


    companion object {
        var ringtone: Ringtone? = null

        // Método para detener la alarma accesible desde toda la aplicación
        fun stopAlarm() {
            ringtone?.let {
                if (it.isPlaying) {
                    it.stop()
                    Log.d("AlarmaDatos", "Alarma detenida")
                }
                ringtone = null  // Liberar el recurso
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        alarmScheduler = AlarmScheduler(context)
        alarmPreferences = AlarmPreferences(context)


        val alarmName = intent.getStringExtra("alarm_name")?: "noName"

        val alarmIdS: Int = intent.getIntExtra("alarm_id", -1) // Recupera el ID como un Int
        val alarmIdString = alarmIdS.toString() // Si necesitas el ID como String
        Log.d("AlarmReceiver", "Alarm ID recibido: $alarmIdString")


        val alarmActive = intent.getBooleanExtra("alarm_isActive", false)
        val alarmPeriodicity = intent.getStringExtra("alarm_periodicity")?: "noPeriodicity"
        val alarmProblem: String = intent.getStringExtra("alarm_problem")?: "noProblem"
        val alarmId: Int = alarmIdString.toIntOrNull() ?: System.currentTimeMillis().toInt()



        val alarmMk2: Alarm? = alarmPreferences.getAlarmById(alarmId)

        Log.d("AlarmaDatos", "Id: $alarmId")
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)


        // Si la alarma es de una vez o diaria se permite que suene
        if (alarmActive && (alarmPeriodicity == "Una vez" || alarmPeriodicity == "Diaria")) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri)
            // Llama a la interfaz para que suene y se vea la alamra
            ringtone?.play()
            // Muestra un mensaje cuando suena la alarma
            Toast.makeText(context, "¡Alarma sonando!", Toast.LENGTH_LONG).show()

            if (alarmPeriodicity == "Una vez"){

                //Actualizamos los valores de la alarma
                alarmMk2?.let{
                    it.isActive = false

                    if (!::alarmPreferences.isInitialized) {
                        alarmPreferences = AlarmPreferences(context)
                    }

                    // Ensure alarmScheduler is initialized
                    if (!::alarmScheduler.isInitialized) {
                        alarmScheduler = AlarmScheduler(context)
                    }

                    alarmPreferences.editAlarm(alarmMk2)
                    alarmScheduler.scheduleAlarm(alarmMk2)
                    Log.d("AlarmaDatos", "Desactivando la alarma de una vez: $alarmMk2,")

                }


            }
            NotificationHelper.showHighPriorityNotification(context, alarmName, alarmId, alarmProblem)
        }
        else if (alarmActive){ // Si es de un dia concreto de la semana, se comprueba que sea dicho dia, si no lo es se hace un re-schedule
            // Obtener el día actual de la semana
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_WEEK)

            // Mapear el valor de alarmPeriodicity a un número de día de la semana
            val dayOfWeekMap = mapOf(
                "Lunes" to Calendar.MONDAY,
                "Martes" to Calendar.TUESDAY,
                "Miércoles" to Calendar.WEDNESDAY,
                "Jueves" to Calendar.THURSDAY,
                "Viernes" to Calendar.FRIDAY,
                "Sábado" to Calendar.SATURDAY,
                "Domingo" to Calendar.SUNDAY
            )


            // Verificar si alarmPeriodicity coincide con el día actual
            val alarmDay = dayOfWeekMap[alarmPeriodicity]
            if (alarmDay == currentDay) {
                ringtone = RingtoneManager.getRingtone(context, alarmUri)
                ringtone?.play()
                Toast.makeText(context, "¡Alarma sonando!", Toast.LENGTH_LONG).show()
                NotificationHelper.showHighPriorityNotification(context, alarmName, alarmId, alarmProblem)
            } else {
                // Recreación de la alarms

                Log.d("AlarmaDatos", "Reprogramando alarma para $alarmPeriodicity")
                alarmMk2?.let {
                    alarmScheduler.scheduleAlarm(alarmMk2) // Reprograma la alarma para el próximo día configurado
                }
            }
        }
    }
}

