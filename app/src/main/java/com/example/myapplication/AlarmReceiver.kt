package com.example.myapplication.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast
import com.example.myapplication.helpers.NotificationHelper
import com.example.myapplication.schedulers.AlarmScheduler
import com.example.myapplication.Alarm
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var alarmScheduler: AlarmScheduler

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

        val alarmName = intent.getStringExtra("alarm_name")?: "noName"
        val alarmIdString = intent.getStringExtra("alarm_id")?: "noId"
        val alarmActive = intent.getBooleanExtra("alarm_isActive", false)
        val alarmPeridiocity = intent.getStringExtra("alarm_periodicity")?: "noPeriodicity"
        val alarmId: Int = alarmIdString.toIntOrNull() ?: System.currentTimeMillis().toInt()
        val alarmTime: String = intent.getStringExtra("alarm_ringTime")?: "00:00"
        val timeParts = alarmTime.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        // Crear un objeto Calendar y configurar la hora y los minutos
        val alarmRingTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        Log.d("AlarmaDatos", "Id: ${alarmId}")
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        // Si la alarma es de una vez o diaria se permite que suene
        if (alarmActive && (alarmPeridiocity == "Una vez" || alarmPeridiocity == "Diaria")) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri)
            // Llama a la interfaz para que suene y se vea la alamra
            ringtone?.play()
            // Muestra un mensaje cuando suena la alarma
            Toast.makeText(context, "¡Alarma sonando!", Toast.LENGTH_LONG).show()

            if (alarmPeridiocity == "Una vez"){
                //TODO Comunicar con shared preferences para que se elimine la alarma (No debería sonar en principio)
            }
            NotificationHelper.showHighPriorityNotification(context, alarmName, alarmId)
        }
        else if (alarmActive){ // Si es de un dia concreto de la semana, se comprueba que sea dicho dia, si no lo es se hace un re-schedule
            // Obtener el día actual de la semana
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_WEEK)

            // Mapear el valor de alarmPeridiocity a un número de día de la semana
            val dayOfWeekMap = mapOf(
                "Lunes" to Calendar.MONDAY,
                "Martes" to Calendar.TUESDAY,
                "Miércoles" to Calendar.WEDNESDAY,
                "Jueves" to Calendar.THURSDAY,
                "Viernes" to Calendar.FRIDAY,
                "Sábado" to Calendar.SATURDAY,
                "Domingo" to Calendar.SUNDAY
            )

            // Verificar si alarmPeridiocity coincide con el día actual
            val alarmDay = dayOfWeekMap[alarmPeridiocity]
            if (alarmDay == currentDay) {
                ringtone = RingtoneManager.getRingtone(context, alarmUri)
                ringtone?.play()
                Toast.makeText(context, "¡Alarma sonando!", Toast.LENGTH_LONG).show()
                NotificationHelper.showHighPriorityNotification(context, alarmName, alarmId)
            } else {
                var alarm = Alarm(
                    id = alarmId,
                    time = alarmTime,
                    name = alarmName,
                    periodicity = alarmPeridiocity,
                    isActive = alarmActive,
                    ringTime = alarmRingTime
                )
                Log.d("AlarmaDatos", "Reprogramando alarma para $alarmPeridiocity")
                alarmScheduler.scheduleAlarm(alarm) // Reprograma la alarma para el próximo día configurado
            }
        }
    }
}


//TODO: Despues de poner dos alarmas en 2 dias diferentes ninguna ha sonado, haz logcat y descubre porque