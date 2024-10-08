package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter class for displaying a list of alarms in a RecyclerView
class AlarmAdapter(private val alarmList: List<Alarm>) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    // ViewHolder class that holds the views for each alarm item
    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPeriodicity: TextView = itemView.findViewById(R.id.textViewPeriodicity)
        val switchToggle: Switch = itemView.findViewById(R.id.switchToggle)
    }

    // Called when the RecyclerView needs a new ViewHolder to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        // Inflate the layout for the alarm item and create a new ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    // Called to display data at a specified position
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.textViewTime.text = alarm.time
        holder.textViewName.text = alarm.name
        holder.textViewPeriodicity.text = alarm.periodicity
        holder.switchToggle.isChecked = alarm.isActive 

        // Add a listener to the switch to update the alarm's active state
        holder.switchToggle.setOnCheckedChangeListener { _, isChecked ->
            alarm.isActive = isChecked
        }
    }

    // Returns the total number of alarms in the list
    override fun getItemCount(): Int {
        return alarmList.size
    }
}
