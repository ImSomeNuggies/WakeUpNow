package com.example.myapplication

import AlarmPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView

// Adapter class for displaying a list of alarms in a RecyclerView
class AlarmAdapter(private val alarmList: List<Alarm>) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    // ViewHolder class that holds the views for each alarm item
    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPeriodicity: TextView = itemView.findViewById(R.id.textViewPeriodicity)
        val switchToggle: Switch = itemView.findViewById(R.id.switchToggle)
        val rootLayout: LinearLayout = itemView.findViewById(R.id.rootLinearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.textViewTime.text = alarm.time
        holder.textViewName.text = alarm.name
        holder.textViewPeriodicity.text = alarm.periodicity
        holder.switchToggle.isChecked = alarm.isActive

        holder.rootLayout.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditAlarm::class.java)
            intent.putExtra("alarm_id", alarm.id) // Pasa el ID de la alarma
            intent.putExtra("alarm_name", alarm.name)
            intent.putExtra("alarm_time", alarm.time)
            intent.putExtra("alarm_periodicity", alarm.periodicity)
            holder.itemView.context.startActivity(intent)
        }

        // OnClickListener independiente para el Switch (no será afectado por el clic en el cuerpo)
        holder.switchToggle.setOnCheckedChangeListener { _, isChecked ->
            alarm.isActive = isChecked
            val alarmPreferences = AlarmPreferences(holder.itemView.context)
            alarmPreferences.editAlarm(alarm)
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}
