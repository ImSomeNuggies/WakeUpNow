package com.example.myapplication.viewmodel.helper

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

class AlarmPermissionHelperTest {

    @Test
    fun `checkNotificationPermission should launch notification permission request if not granted on TIRAMISU or higher`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Arrange
            val mockActivity = mock(Activity::class.java)
            val mockLauncher = mock(ActivityResultLauncher::class.java) as ActivityResultLauncher<String>
            `when`(mockActivity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)).thenReturn(
                PackageManager.PERMISSION_DENIED
            )

            val helper = AlarmPermissionHelper(mockActivity, mockLauncher)

            // Act
            helper.checkNotificationPermission()

            // Assert
            verify(mockLauncher).launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    @Test
    fun `checkNotificationPermission should show toast if permission already granted on TIRAMISU or higher`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Arrange
            val mockActivity = mock(Activity::class.java)
            val mockLauncher = mock(ActivityResultLauncher::class.java) as ActivityResultLauncher<String>
            `when`(mockActivity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)).thenReturn(
                PackageManager.PERMISSION_GRANTED
            )

            val helper = AlarmPermissionHelper(mockActivity, mockLauncher)

            // Act
            helper.checkNotificationPermission()

            // Assert
            verify(mockActivity).run {
                Toast.makeText(mockActivity, "Notification permission already granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Test
    fun `checkAndRequestAlarmPermission should request exact alarm permission if not granted on S or higher`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Arrange
            val mockActivity = mock(Activity::class.java)
            val mockAlarmManager = mock(AlarmManager::class.java)
            `when`(mockActivity.getSystemService(Context.ALARM_SERVICE)).thenReturn(mockAlarmManager)
            `when`(mockAlarmManager.canScheduleExactAlarms()).thenReturn(false)

            val helper = AlarmPermissionHelper(mockActivity, mock())

            // Act
            val captor = ArgumentCaptor.forClass(Intent::class.java)
            helper.checkNotificationPermission()

            // Assert
            verify(mockActivity).startActivity(captor.capture())
            assert(captor.value.action == Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        }
    }

    @Test
    fun `checkAndRequestAlarmPermission should show toast if exact alarm permission already granted on S or higher`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Arrange
            val mockActivity = mock(Activity::class.java)
            val mockAlarmManager = mock(AlarmManager::class.java)
            `when`(mockActivity.getSystemService(Context.ALARM_SERVICE)).thenReturn(mockAlarmManager)
            `when`(mockAlarmManager.canScheduleExactAlarms()).thenReturn(true)

            val helper = AlarmPermissionHelper(mockActivity, mock())

            // Act
            helper.checkNotificationPermission()

            // Assert
            verify(mockActivity).run {
                Toast.makeText(mockActivity, "Alarm permission already accepted", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
