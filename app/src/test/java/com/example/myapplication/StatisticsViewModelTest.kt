package com.example.myapplication

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.StatisticsViewModel
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class StatisticsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule() // Para LiveData

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repository: AlarmStatsRepository
    private lateinit var viewModel: StatisticsViewModel
    private val gson = Gson()

    @Before
    fun setup() {
        // Mock de SharedPreferences y su editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Crear repositorio con el mock de SharedPreferences
        repository = AlarmStatsRepository(sharedPreferences)

        // Crear instancia del ViewModel
        viewModel = StatisticsViewModel(repository)
    }

    @Test
    fun `calculateGeneralStatistics initializes general statistics correctly`() {
        // Crear estadísticas de prueba y serializarlas en JSON
        val statsJson = """
            [
                {"id":"1","alarmSetTime":"07:00","timeToTurnOff":12000,"failures":2},
                {"id":"2","alarmSetTime":"15:00","timeToTurnOff":6000,"failures":1}
            ]
        """.trimIndent()
        `when`(sharedPreferences.getString("statistics", null)).thenReturn(statsJson)

        // Forzar inicialización de estadísticas generales
        viewModel = StatisticsViewModel(repository)

        // Verificar valores en LiveData asegurándote de que no son nulos
        val averageTime = viewModel.averageTimeToTurnOff.value ?: 0.0
        val maxTime = viewModel.maxTimeToTurnOff.value ?: 0.0
        val minTime = viewModel.minTimeToTurnOff.value ?: 0.0
        val maxErrors = viewModel.maxErrors.value ?: 0f

        assertEquals(9.0, averageTime, 0.001) // Promedio
        assertEquals(12.0, maxTime, 0.001) // Máximo
        assertEquals(6.0, minTime, 0.001) // Mínimo
        assertEquals(2, maxErrors) // Máximo errores
    }

    @Test
    fun `calculateHourlyData initializes hourly data correctly`() {
        // Crear estadísticas de prueba y serializarlas en JSON
        val statsJson = """
            [
                {"id":"1","alarmSetTime":"07:00","timeToTurnOff":12000,"failures":2},
                {"id":"2","alarmSetTime":"15:00","timeToTurnOff":6000,"failures":1}
            ]
        """.trimIndent()
        `when`(sharedPreferences.getString("statistics", null)).thenReturn(statsJson)

        // Forzar inicialización de estadísticas horarias
        viewModel = StatisticsViewModel(repository)

        val averageData = viewModel.averageHourlyResponseTimeData.value ?: emptyMap()
        val maxData = viewModel.maxHourlyResponseTimeData.value ?: emptyMap()
        val minData = viewModel.minHourlyResponseTimeData.value ?: emptyMap()
        val errorsData = viewModel.maxHourlyErrorsData.value ?: emptyMap()

        // Verificar datos inicializados correctamente
        assertEquals(2, averageData.size)
        assertEquals(12.0f, averageData["5 - 8"])
        assertEquals(6.0f, minData["15 - 19"])
        assertEquals(6.0f, maxData["15 - 19"])
        assertEquals(1f, errorsData["15 - 19"])
    }

    @Test
    fun `getGraphData returns correct LiveData based on type`() {
        // Verificar que se retorna el LiveData adecuado para cada tipo
        assertEquals(viewModel.averageHourlyResponseTimeData, viewModel.getGraphData("Tiempo medio (seg)"))
        assertEquals(viewModel.maxHourlyResponseTimeData, viewModel.getGraphData("Tiempo máx. (seg)"))
        assertEquals(viewModel.minHourlyResponseTimeData, viewModel.getGraphData("Tiempo mín. (seg)"))
        assertEquals(viewModel.maxHourlyErrorsData, viewModel.getGraphData("Máx. errores"))
    }

    @Test
    fun `roundTwoDecimalsDouble rounds correctly`() {
        // Acceder al método privado mediante reflexión
        val method = viewModel.javaClass.getDeclaredMethod("roundTwoDecimalsDouble", Double::class.java)
        method.isAccessible = true

        // Verificar redondeo
        assertEquals(12.35, method.invoke(viewModel, 12.3456))
        assertEquals(12.34, method.invoke(viewModel, 12.3444))
    }

    @Test
    fun `roundTwoDecimalsFloat rounds correctly`() {
        // Acceder al método privado mediante reflexión
        val method = viewModel.javaClass.getDeclaredMethod("roundTwoDecimalsFloat", Double::class.java)
        method.isAccessible = true

        // Verificar redondeo
        assertEquals(12.35f, method.invoke(viewModel, 12.3456))
        assertEquals(12.34f, method.invoke(viewModel, 12.3444))
    }
}

