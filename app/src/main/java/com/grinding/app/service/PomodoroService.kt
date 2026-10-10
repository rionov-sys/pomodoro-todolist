package com.grinding.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.grinding.app.MainActivity
import com.grinding.app.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class PomodoroService : Service() {

    @Inject
    lateinit var pomodoroManager: PomodoroManager

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var wakeLock: PowerManager.WakeLock? = null
    private var stateCollectJob: Job? = null

    companion object {
        const val NOTIFICATION_ID = 1001
        const val FINISH_NOTIFICATION_ID = 1002
        const val CHANNEL_ID = "pomodoro_timer_channel"
        const val FINISH_CHANNEL_ID = "pomodoro_finish_channel"

        const val ACTION_START = "com.grinding.app.ACTION_START"
        const val ACTION_PAUSE = "com.grinding.app.ACTION_PAUSE"
        const val ACTION_RESUME = "com.grinding.app.ACTION_RESUME"
        const val ACTION_STOP = "com.grinding.app.ACTION_STOP"
        const val ACTION_FINISH = "com.grinding.app.ACTION_FINISH"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        acquireWakeLock()
        observeManagerState()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startInForeground()
            }
            ACTION_PAUSE -> {
                pomodoroManager.pauseTimer(this)
                updateNotification()
            }
            ACTION_RESUME -> {
                pomodoroManager.resumeTimer(this)
                updateNotification()
            }
            ACTION_STOP -> {
                pomodoroManager.resetTimer(this)
                stopForegroundService()
            }
            ACTION_FINISH -> {
                showFinishedNotification()
                stopForegroundService()
            }
        }
        return START_NOT_STICKY
    }

    private fun startInForeground() {
        val notification = buildTimerNotification(pomodoroManager.state.value)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun observeManagerState() {
        stateCollectJob?.cancel()
        stateCollectJob = serviceScope.launch {
            pomodoroManager.state.collect { state ->
                if (state.isRunning || state.isPaused) {
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(NOTIFICATION_ID, buildTimerNotification(state))
                }
            }
        }
    }

    private fun updateNotification() {
        val state = pomodoroManager.state.value
        if (state.isRunning || state.isPaused) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, buildTimerNotification(state))
        }
    }

    private fun buildTimerNotification(state: PomodoroState): Notification {
        val minutes = state.remainingSeconds / 60
        val seconds = state.remainingSeconds % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(if (state.isRunning) getString(R.string.timer_notification_running) else getString(R.string.timer_notification_paused))
            .setContentText("$timeFormatted • ${state.taskTitle}")
            .setContentIntent(mainPendingIntent)
            .setOngoing(state.isRunning)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        // Notification Action: Jeda / Lanjutkan
        if (state.isRunning) {
            val pauseIntent = Intent(this, PomodoroService::class.java).apply {
                action = ACTION_PAUSE
            }
            val pausePendingIntent = PendingIntent.getService(
                this,
                1,
                pauseIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.addAction(
                android.R.drawable.ic_media_pause,
                getString(R.string.action_jeda),
                pausePendingIntent
            )
        } else {
            val resumeIntent = Intent(this, PomodoroService::class.java).apply {
                action = ACTION_RESUME
            }
            val resumePendingIntent = PendingIntent.getService(
                this,
                2,
                resumeIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.addAction(
                android.R.drawable.ic_media_play,
                getString(R.string.action_lanjut),
                resumePendingIntent
            )
        }

        // Notification Action: Selesai
        val stopIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            3,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.addAction(
            android.R.drawable.ic_menu_close_clear_cancel,
            getString(R.string.action_selesai),
            stopPendingIntent
        )

        return builder.build()
    }

    private fun showFinishedNotification() {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            4,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val finishNotification = NotificationCompat.Builder(this, FINISH_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.timer_notification_finished))
            .setContentText(getString(R.string.timer_notification_finished_content))
            .setContentIntent(mainPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(FINISH_NOTIFICATION_ID, finishNotification)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timerChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.timer_notification_channel),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifikasi status timer Pomodoro yang sedang berjalan"
                setShowBadge(false)
            }

            val finishChannel = NotificationChannel(
                FINISH_CHANNEL_ID,
                getString(R.string.timer_notification_finished),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi saat sesi Pomodoro telah selesai"
                enableVibration(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(timerChannel)
            notificationManager?.createNotificationChannel(finishChannel)
        }
    }

    private fun acquireWakeLock() {
        try {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Grinding::PomodoroWakeLock").apply {
                setReferenceCounted(false)
                acquire(45 * 60 * 1000L) // Safe max timeout 45 minutes
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releaseWakeLock() {
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopForegroundService() {
        stateCollectJob?.cancel()
        releaseWakeLock()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    override fun onDestroy() {
        serviceScope.cancel()
        releaseWakeLock()
        super.onDestroy()
    }
}
