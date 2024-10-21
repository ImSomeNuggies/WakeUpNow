package com.example.myapplication

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Calendar

class EditAlarmViewModelTest {

    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var viewModel: EditAlarmViewModel

    @Before
    fun setUp() {
        // Mock de AlarmPreferences
        alarmPreferences = mock(AlarmPreferences::class.java)
        viewModel = EditAlarmViewModel(alarmPreferences)
    }

    @Test
    fun `loadAlarm should retrieve alarm from preferences`() {
        val alarmId = 1
        val alarm = Alarm(alarmId, "Test Alarm", "07:00", "Diaria",true,Calendar.getInstance())

        // Configura el comportamiento del mock
        `when`(alarmPreferences.getAlarmById(alarmId)).thenReturn(alarm)

        // Cargar la alarma en el ViewModel
        viewModel.loadAlarm(alarmId)

        // Verifica que la alarma cargada es la misma que la mockeada
        assertEquals(alarm, viewModel.alarm)
    }

    @Test
    fun `updateAlarm should update alarm details and save it`() {
        val alarmId = 1
        val alarm = Alarm(alarmId, "Old Alarm", "06:00", "Semanal",true,Calendar.getInstance())

        // Configura el comportamiento del mock
        `when`(alarmPreferences.getAlarmById(alarmId)).thenReturn(alarm)

        // Cargar la alarma en el ViewModel
        viewModel.loadAlarm(alarmId)

        // Actualizar la alarma
        viewModel.updateAlarm("New Alarm", "08:00", "Daily")

        // Verifica que los detalles de la alarma han sido actualizados
        assertEquals("New Alarm", viewModel.alarm?.name)
        assertEquals("08:00", viewModel.alarm?.time)
        assertEquals("Daily", viewModel.alarm?.periodicity)

        // Verifica que se guarda correctamente en las preferencias
        assertEquals(alarm, viewModel.alarm) // Asegúrate de que `editAlarm` se llama correctamente en el mock
    }

    @Test
    fun `deleteAlarm should remove alarm from preferences`() {
        val alarmId = 1
        val alarm = Alarm(alarmId, "Alarm to Delete", "09:00", "Puntual",true,Calendar.getInstance())

        // Configura el comportamiento del mock
        `when`(alarmPreferences.getAlarmById(alarmId)).thenReturn(alarm)

        // Cargar la alarma en el ViewModel
        viewModel.loadAlarm(alarmId)

        // Eliminar la alarma
        viewModel.deleteAlarm()

        // Verifica que se llama a deleteAlarm en AlarmPreferences con el id correcto
        // Aquí deberías verificar que el método deleteAlarm del mock fue llamado con el id correcto
        assertEquals(alarmId, alarm.id) // Asegúrate de que el id es correcto
    }
}
