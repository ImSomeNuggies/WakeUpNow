package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.StatisticsViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class StatisticsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule() // Required to test LiveData updates

    private lateinit var repository: AlarmStatsRepository
    private lateinit var viewModel: StatisticsViewModel

    @Before
    fun setup() {
        repository = mock(AlarmStatsRepository::class.java)
        viewModel = StatisticsViewModel(repository)
    }

    @Test
    fun `getGraphData returns correct LiveData based on type`() {
        // Verify correct LiveData is returned for each type
        assertEquals(viewModel.averageHourlyResponseTimeData, viewModel.getGraphData("Tiempo medio (seg)"))
        assertEquals(viewModel.maxHourlyResponseTimeData, viewModel.getGraphData("Tiempo máx. (seg)"))
        assertEquals(viewModel.minHourlyResponseTimeData, viewModel.getGraphData("Tiempo mín. (seg)"))
        assertEquals(viewModel.maxHourlyErrorsData, viewModel.getGraphData("Máx. errores"))
    }

    @Test
    fun `roundTwoDecimalsDouble rounds correctly`() {
        val result = viewModel.javaClass.getDeclaredMethod("roundTwoDecimalsDouble", Double::class.java)
        result.isAccessible = true
        assertEquals(12.35, result.invoke(viewModel, 12.3456))
        assertEquals(12.34, result.invoke(viewModel, 12.3444))
    }

    @Test
    fun `roundTwoDecimalsFloat rounds correctly`() {
        val result = viewModel.javaClass.getDeclaredMethod("roundTwoDecimalsFloat", Double::class.java)
        result.isAccessible = true
        assertEquals(12.35f, result.invoke(viewModel, 12.3456))
        assertEquals(12.34f, result.invoke(viewModel, 12.3444))
    }
}
