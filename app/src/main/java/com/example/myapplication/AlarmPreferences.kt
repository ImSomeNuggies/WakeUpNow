import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.Alarm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    fun getAlarmById(alarmId: Int): Alarm? {
        // Usamos el sharedPreferences y gson ya inicializados en la clase
        val alarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val alarmList: MutableList<Alarm> = if (alarmsJson != null) {
            gson.fromJson(alarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Buscar la alarma por ID
        return alarmList.find { it.id == alarmId }
    }

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

    fun editAlarm(editedAlarm: Alarm) {
        val editor = sharedPreferences.edit()

        // Obtener la lista actual de alarmas
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val existingAlarms: MutableList<Alarm> = if (existingAlarmsJson != null) {
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Buscar la alarma por ID y actualizarla
        val alarmIndex = existingAlarms.indexOfFirst { it.id == editedAlarm.id }
        if (alarmIndex != -1) {
            existingAlarms[alarmIndex] = editedAlarm
        }

        // Guardar la lista actualizada de alarmas en SharedPreferences
        val newAlarmsJson = gson.toJson(existingAlarms)
        editor.putString("alarms_list", newAlarmsJson)
        editor.apply()
    }

    fun deleteAlarm(alarmId: Int) {
        val editor = sharedPreferences.edit()

        // Obtener la lista actual de alarmas
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val existingAlarms: MutableList<Alarm> = if (existingAlarmsJson != null) {
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Eliminar la alarma por ID
        val alarmToRemove = existingAlarms.find { it.id == alarmId }
        if (alarmToRemove != null) {
            existingAlarms.remove(alarmToRemove)
        }

        // Guardar la lista actualizada de alarmas en SharedPreferences
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
