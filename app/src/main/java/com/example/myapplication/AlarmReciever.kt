package com.example.myapplication.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.app.PendingIntent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.AlarmSoundingActivity
import com.example.myapplication.helpers.NotificationHelper
import com.example.myapplication.R

class AlarmReceiver : BroadcastReceiver() {

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

        val alarmName = intent.getStringExtra("alarm_name")
        val alarmIdString: String? = intent.getStringExtra("alarm_id")
        val alarmActive = intent.getBooleanExtra("alarm_isActive", false)
        val alarmPeridiocity = intent.getStringExtra("alarm_periodicity")
        val alarmId: Int = alarmIdString?.toIntOrNull() ?: System.currentTimeMillis().toInt()
        Log.d("AlarmaDatos", "Id: ${alarmId}")
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        if (alarmActive) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri)
            // Llama a la interfaz para que suene y se vea la alamra
            ringtone?.play()
            // Muestra un mensaje cuando suena la alarma
            Toast.makeText(context, "¡Alarma sonando!", Toast.LENGTH_LONG).show()

            if (alarmPeridiocity == "Una vez"){
                //TODO Comunicar con shared preferences para que se elimine la alarma (No debería sonar en principio)
            }
            /* val alarmSoundingIntent = Intent(context, AlarmSounding::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("alarm_name", alarmName)  // Puedes pasar datos adicionales si es necesario
        }
        context.startActivity(alarmSoundingIntent)*/
            NotificationHelper.showHighPriorityNotification(context, alarmName, alarmId)
        }
        else{
            Log.d("AlarmaDatos","Alarma inactiva")
        }
    }
}
