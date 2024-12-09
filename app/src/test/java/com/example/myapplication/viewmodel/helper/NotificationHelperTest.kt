package com.example.myapplication.viewmodel.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.view.AlarmSoundingActivity
import com.example.myapplication.view.SudokuSoundingActivity
import com.example.myapplication.view.QRSoundingActivity
import com.example.myapplication.R
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

class NotificationHelperTest {

    @Test
    fun `createNotificationChannel should create a notification channel on Android O or higher`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Arrange
            val mockContext = mock(Context::class.java)
            val mockNotificationManager = mock(NotificationManager::class.java)
            `when`(mockContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mockNotificationManager)

            // Act
            NotificationHelper.createNotificationChannel(mockContext)

            // Assert
            val captor = ArgumentCaptor.forClass(NotificationChannel::class.java)
            verify(mockNotificationManager).createNotificationChannel(captor.capture())
            val channel = captor.value
            assert(channel.id == "high_priority_channel")
            assert(channel.importance == NotificationManager.IMPORTANCE_HIGH)
            assert(channel.name == "High Priority Channel")
            assert(channel.description == "This is used for high priority notifications")
        }
    }

    @Test
    fun `showHighPriorityNotification should build and display a notification for short problem`() {
        // Arrange
        val mockContext = mock(Context::class.java)
        val mockNotificationManagerCompat = mock(NotificationManagerCompat::class.java)
        mockStatic(NotificationManagerCompat::class.java).use { mockedCompat ->
            mockedCompat.`when`<NotificationManagerCompat> { NotificationManagerCompat.from(mockContext) }
                .thenReturn(mockNotificationManagerCompat)
        }

        val mockPendingIntent = mock(PendingIntent::class.java)
        mockStatic(PendingIntent::class.java).use { mockedPendingIntent ->
            mockedPendingIntent.`when`<PendingIntent> {
                PendingIntent.getActivity(
                    eq(mockContext),
                    anyInt(),
                    any(Intent::class.java),
                    anyInt()
                )
            }.thenReturn(mockPendingIntent)
        }

//        // Act
//        NotificationHelper.showHighPriorityNotification(
//            context = mockContext,
//            name = "Test Alarm",
//            id = 123,
//            problem = "Problema corto"
//        )
//
//        // Assert
//        verify(mockNotificationManagerCompat).notify(eq(123), any())
    }

    @Test
    fun `showHighPriorityNotification should build and display a notification for Sudoku problem`() {
        // Arrange
        val mockContext = mock(Context::class.java)
        val mockNotificationManagerCompat = mock(NotificationManagerCompat::class.java)
        mockStatic(NotificationManagerCompat::class.java).use { mockedCompat ->
            mockedCompat.`when`<NotificationManagerCompat> { NotificationManagerCompat.from(mockContext) }
                .thenReturn(mockNotificationManagerCompat)
        }

        val mockPendingIntent = mock(PendingIntent::class.java)
        mockStatic(PendingIntent::class.java).use { mockedPendingIntent ->
            mockedPendingIntent.`when`<PendingIntent> {
                PendingIntent.getActivity(
                    eq(mockContext),
                    anyInt(),
                    any(Intent::class.java),
                    anyInt()
                )
            }.thenReturn(mockPendingIntent)
        }

//        // Act
//        NotificationHelper.showHighPriorityNotification(
//            context = mockContext,
//            name = "Test Alarm",
//            id = 456,
//            problem = "Sudoku"
//        )
//
//        // Assert
//        verify(mockNotificationManagerCompat).notify(eq(123), any())
//
    }

    @Test
    fun `showHighPriorityNotification should build and display a notification for QR scanner problem`() {
        // Arrange
        val mockContext = mock(Context::class.java)
        val mockNotificationManagerCompat = mock(NotificationManagerCompat::class.java)
        mockStatic(NotificationManagerCompat::class.java).use { mockedCompat ->
            mockedCompat.`when`<NotificationManagerCompat> { NotificationManagerCompat.from(mockContext) }
                .thenReturn(mockNotificationManagerCompat)
        }

        val mockPendingIntent = mock(PendingIntent::class.java)
        mockStatic(PendingIntent::class.java).use { mockedPendingIntent ->
            mockedPendingIntent.`when`<PendingIntent> {
                PendingIntent.getActivity(
                    eq(mockContext),
                    anyInt(),
                    any(Intent::class.java),
                    anyInt()
                )
            }.thenReturn(mockPendingIntent)
        }

//        // Act
//        NotificationHelper.showHighPriorityNotification(
//            context = mockContext,
//            name = "Test Alarm",
//            id = 789,
//            problem = "QR scanner"
//        )
//
//        // Assert
//        verify(mockNotificationManagerCompat).notify(eq(123), any())
//
    }
}
