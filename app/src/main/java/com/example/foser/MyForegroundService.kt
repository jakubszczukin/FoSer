package com.example.foser

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import java.util.*


class MyForegroundService : Service() {

    //1. Kanał notyfikacji
    val CHANNEL_ID = "MyForegroundServiceChannel"
    val CHANNEL_NAME = "FoSer service channel"

    //2. Odczyt danych zapisanych w Intent
    val MESSAGE = "message"
    val TIME = "time"
    val WORK = "work"
    val WORK_DOUBLE = "work_double"

    //3. Wartości ustawień
    private var message: String? = null
    private var show_time: Boolean? = null
    private  var do_work:kotlin.Boolean? = null
    private  var double_speed:kotlin.Boolean? = null
    private val period: Long = 2000 //2s

    //4.
    private var ctx: Context? = null
    private var notificationIntent: Intent? = null
    private var pendingIntent: PendingIntent? = null

    //5.
    private var counter = 0
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    val handler: Handler = Handler()
    val runnable = Runnable { }

    override fun onCreate() {
        super.onCreate()

        ctx = this
        notificationIntent = Intent(ctx, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        counter = 0

        timer = Timer()

        timerTask = object : TimerTask() {
            override fun run() {
                counter++;
                handler.post(runnable);
            }
        }
    }

    fun run() {
        val notification: Notification = Notification.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_my_icon)
                .setContentTitle(getString(R.string.ser_title))
                .setShowWhen(show_time!!)
                .setContentText("$message $counter")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.circle))
                .setContentIntent(pendingIntent)
                .build()
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1, notification)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        timer!!.cancel()
        timer!!.purge()
        timer = null
        super.onDestroy()
    }

    private fun doWork() {
        if (do_work!!) {
            timer!!.schedule(timerTask, 0L, if (double_speed!!) period / 2L else period)
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId);
        message = intent.getStringExtra(MESSAGE)
        show_time = intent.getBooleanExtra(TIME, false)
        do_work = intent.getBooleanExtra(WORK, false)
        double_speed = intent.getBooleanExtra(WORK_DOUBLE, false)
        createNotificationChannel()
        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(com.example.foser.R.drawable.ic_my_icon)
                .setContentTitle(getString(com.example.foser.R.string.ser_title))
                .setShowWhen(show_time!!)
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource(resources, com.example.foser.R.drawable.circle))
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)
        doWork()
        return START_NOT_STICKY
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}