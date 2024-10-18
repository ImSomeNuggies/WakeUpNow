import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.Alarm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Método para guardar una alarma en SharedPreferences
    fun saveAlarm(alarm: Alarm) {
        val editor = sharedPreferences.edit()

        // Obtener la lista actual de alarmas
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val existingAlarms: MutableList<Alarm> = if (existingAlarmsJson != null) {
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Agregar la nueva alarma a la lista
        existingAlarms.add(alarm)

        // Guardar la lista de alarmas de nuevo en SharedPreferences
        val newAlarmsJson = gson.toJson(existingAlarms)
        editor.putString("alarms_list", newAlarmsJson)
        editor.apply()
    }

    // Método para cargar las alarmas desde SharedPreferences
    fun loadAlarms(): MutableList<Alarm> {
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type

        return if (existingAlarmsJson != null) {
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }
}
