package com.example.myapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.LinearLayout
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.viewmodel.AlarmScheduler


/**
 * Adapter class for managing and displaying a list of alarms in a RecyclerView.
 * 
 * @param alarmList The list of alarms to be displayed in the RecyclerView.
 */
class AlarmAdapter(private val alarmList: List<Alarm>) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    // Needed to update and prime alarms
    private lateinit var alarmScheduler: AlarmScheduler

    /**
     * Nested class ViewHolder that holds references to the views for each alarm item in the RecyclerView.
     * It binds the data from an Alarm object to the views.
     */
    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPeriodicity: TextView = itemView.findViewById(R.id.textViewPeriodicity)
        val switchToggle: Switch = itemView.findViewById(R.id.switchToggle)
        val rootLayout: LinearLayout = itemView.findViewById(R.id.rootLinearLayout)
    }

    /**
     * Inflates the layout for each alarm item and returns a ViewHolder that will display it.
     * 
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType An integer representing the type of view (not used in this case).
     * @return An instance of AlarmViewHolder that holds the inflated view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    /**
     * Binds data from an Alarm object to the views in the ViewHolder.
     * It handles setting the text, periodicity, and toggle status of the alarm, 
     * and manages click listeners for the alarm and the toggle switch.
     * 
     * @param holder The ViewHolder that will display the data.
     * @param position The position of the alarm in the list.
     */
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        // Set the alarm's time, name, peridicity and status (isActive) in the view
        val alarm = alarmList[position]
        holder.textViewTime.text = alarm.time
        holder.textViewName.text = alarm.name
        holder.textViewPeriodicity.text = alarm.periodicity
        holder.switchToggle.isChecked = alarm.isActive

        // Set a click listener on the root layout to open the EditAlarm activity for the alarm item
        holder.rootLayout.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditAlarmActivity::class.java)
            intent.putExtra("alarm_id", alarm.id)
            intent.putExtra("alarm_name", alarm.name)
            intent.putExtra("alarm_time", alarm.time)
            intent.putExtra("alarm_periodicity", alarm.periodicity)
            intent.putExtra("alarm_problem", alarm.problem)
            holder.itemView.context.startActivity(intent)
        }

         // Set a listener for the switch toggle to activate or deactivate the alarm
        holder.switchToggle.setOnCheckedChangeListener { _, isChecked ->
            alarm.isActive = isChecked
            val alarmPreferences = AlarmPreferences(holder.itemView.context)
            alarmPreferences.editAlarm(alarm)

            // Initialize alarmScheduler if it hasn't been already
            if (!::alarmScheduler.isInitialized) {
                alarmScheduler = AlarmScheduler(holder.itemView.context)
            }
            alarmScheduler.scheduleAlarm(alarm)
        }
    }

    /**
     * Returns the total number of alarm items to be displayed in the RecyclerView.
     * 
     * @return The size of the alarm list.
     */
    override fun getItemCount(): Int {
        return alarmList.size
    }
}
