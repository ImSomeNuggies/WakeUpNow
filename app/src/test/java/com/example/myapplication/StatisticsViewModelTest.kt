package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.repositories.AlarmStatsRepository
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
    fun `calculateGeneralStatistics sets correct LiveData values`() {
        // Mock repository responses
        `when`(repository.getAverageTimeInRange(0, 23)).thenReturn(10.0)
        `when`(repository.getMaxTimeInRange(0, 23)).thenReturn(20.0)
        `when`(repository.getMinTimeInRange(0, 23)).thenReturn(5.0)
        `when`(repository.getMaxErrorsInRange(0, 23)).thenReturn(3)

        // Observe LiveData to verify changes
        val averageObserver = mock(Observer::class.java) as Observer<Double>
        val maxObserver = mock(Observer::class.java) as Observer<Double>
        val minObserver = mock(Observer::class.java) as Observer<Double>
        val errorsObserver = mock(Observer::class.java) as Observer<Int>

        viewModel.averageTimeToTurnOff.observeForever(averageObserver)
        viewModel.maxTimeToTurnOff.observeForever(maxObserver)
        viewModel.minTimeToTurnOff.observeForever(minObserver)
        viewModel.maxErrors.observeForever(errorsObserver)

        // Verify LiveData values
        verify(averageObserver).onChanged(10.0)
        verify(maxObserver).onChanged(20.0)
        verify(minObserver).onChanged(5.0)
        verify(errorsObserver).onChanged(3)
    }

    @Test
    fun `calculateHourlyData updates LiveData for specific ranges`() {
        // Mock repository responses
        `when`(repository.hasStatisticsInRange(5, 8)).thenReturn(true)
        `when`(repository.getAverageTimeInRange(5, 8)).thenReturn(15.5)
        `when`(repository.getMaxTimeInRange(5, 8)).thenReturn(25.0)
        `when`(repository.getMinTimeInRange(5, 8)).thenReturn(10.0)
        `when`(repository.getMaxErrorsInRange(5, 8)).thenReturn(2)

        // Observe LiveData to verify changes
        val averageHourlyObserver = mock(Observer::class.java) as Observer<Map<String, Float>>
        val maxHourlyObserver = mock(Observer::class.java) as Observer<Map<String, Float>>
        val minHourlyObserver = mock(Observer::class.java) as Observer<Map<String, Float>>
        val maxErrorsObserver = mock(Observer::class.java) as Observer<Map<String, Int>>

        viewModel.averageHourlyResponseTimeData.observeForever(averageHourlyObserver)
        viewModel.maxHourlyResponseTimeData.observeForever(maxHourlyObserver)
        viewModel.minHourlyResponseTimeData.observeForever(minHourlyObserver)
        viewModel.maxHourlyErrorsData.observeForever(maxErrorsObserver)

        // Verify LiveData values
        verify(averageHourlyObserver).onChanged(mapOf("5 - 8" to 15.5f))
        verify(maxHourlyObserver).onChanged(mapOf("5 - 8" to 25.0f))
        verify(minHourlyObserver).onChanged(mapOf("5 - 8" to 10.0f))
        verify(maxErrorsObserver).onChanged(mapOf("5 - 8" to 2))
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
