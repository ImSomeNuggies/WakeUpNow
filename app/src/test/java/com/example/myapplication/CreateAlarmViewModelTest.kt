package com.example.myapplication

import android.content.SharedPreferences
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.repository.AlarmRepository
import com.example.myapplication.viewmodel.CreateAlarmViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*

class CreateAlarmViewModelTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var alarmRepository: AlarmRepository
    private lateinit var viewModel: CreateAlarmViewModel

    @Before
    fun setUp() {
        // Mock SharedPreferences y Editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        // Configurar comportamiento de los mocks
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Inicializar AlarmPreferences y AlarmRepository con SharedPreferences mockeadas
        alarmPreferences = AlarmPreferences(sharedPreferences)
        alarmRepository = AlarmRepository(alarmPreferences)

        // Inicializar ViewModel con AlarmRepository
        viewModel = CreateAlarmViewModel(alarmRepository)
    }

    @Test
    fun `saveAlarm should create and save a new alarm with correct properties`() {
        // Configurar datos necesarios
        viewModel.selectedTime = "08:30"
        viewModel.selectedPeriodicity = "Diaria"
        viewModel.selectedProblem = "Sudoku"

        // Configurar mock para generar un nuevo ID de alarma
        `when`(sharedPreferences.getString("alarms_list", null)).thenReturn("[]")

        // Llamar al método saveAlarm y capturar el resultado
        val result = viewModel.saveAlarm("Morning Alarm")

        // Verificar que la alarma fue creada con las propiedades correctas
        assert(result.id == 1)
        assert(result.time == "08:30")
        assert(result.name == "Morning Alarm")
        assert(result.periodicity == "Diaria")
        assert(result.problem == "Sudoku")
        assert(result.isActive)

        // Verificar que ringTime fue configurado correctamente
        val expectedTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        assert(result.ringTime.timeInMillis == expectedTime.timeInMillis)

        // Verificar que saveAlarm fue llamado en el repositorio
        verify(editor).putString(eq("alarms_list"), anyString())
        verify(editor).apply()
    }
}
