package com.example.foser

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi


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

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun doWork() {
        try {
            Thread.sleep(5000)
        } catch (e: Exception) {
            //
        }
        val info = """Start working...
            show_time=${show_time.toString()}
             do_work=${do_work.toString()}
            double_speed=${double_speed.toString()}"""
        Toast.makeText(this, info, Toast.LENGTH_LONG).show()
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
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
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