package com.example.foser

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {

    private var buttonStart: Button? = null
    private var buttonStop:android.widget.Button? = null
    private var buttonRestart:android.widget.Button? = null
    private var textInfoService: TextView? = null
    private var textInfoSettings:TextView? = null
    private var message: String? = null
    private var show_time: Boolean? = null
    private  var work:kotlin.Boolean? = null
    private  var work_double:kotlin.Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonStart = findViewById<Button>(R.id.buttonStart)
        var buttonRestart = findViewById<Button>(R.id.buttonRestart)
        var buttonStop = findViewById<Button>(R.id.buttonStop)

        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        buttonRestart = findViewById(R.id.buttonRestart);
        textInfoService = findViewById(R.id.textInfoServiceState);
        textInfoSettings = findViewById(R.id.textInfoSettings);

        buttonStart.setOnClickListener {
            clickStart(findViewById(R.id.content))
        }

        buttonRestart.setOnClickListener {
            clickRestart(findViewById(R.id.content))
        }

        buttonStop.setOnClickListener {
            clickStop(findViewById(R.id.content))
        }

        updateUI()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.itemSettings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.itemExit -> {
                finishAndRemoveTask()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getPreferences(): String? {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        message = sharedPreferences.getString("message", "ForSer")
        show_time = sharedPreferences.getBoolean("show_time", true)
        work = sharedPreferences.getBoolean("sync", true)
        work_double = sharedPreferences.getBoolean("double", false)
        return """
            Message: $message
            show_time: ${show_time.toString()}
            work: ${work.toString()}
            double: ${work_double.toString()}
            """.trimIndent()
    }

    private fun updateUI() {
        if (isMyForegroundServiceRunning()) {
            buttonStart!!.isEnabled = false
            buttonStop!!.isEnabled = true
            buttonRestart!!.isEnabled = true
            textInfoService!!.text = resources.getString(com.example.foser.R.string.info_service_running)
        } else {
            buttonStart!!.isEnabled = true
            buttonStop!!.isEnabled = false
            buttonRestart!!.isEnabled = false
            textInfoService!!.text = resources.getString(com.example.foser.R.string.info_service_not_running)
        }
        textInfoSettings!!.text = getPreferences()
    }

    fun clickStart(view: View?) {
        getPreferences()
        val startIntent = Intent(this, MyForegroundService::class.java)
        /*
        val test = MyForegroundService()
        startIntent.putExtra(test.MESSAGE, message)
        startIntent.putExtra(test.TIME, show_time)
        startIntent.putExtra(test.WORK, work)
        startIntent.putExtra(test.WORK_DOUBLE, work_double)
        ContextCompat.startForegroundService(this, startIntent)
        updateUI()*/
    }

    fun clickStop(view: View?) {
        val stopIntent = Intent(this, MyForegroundService::class.java)
        stopService(stopIntent)
        updateUI()
    }

    private fun clickRestart(view: View?) {
        clickStop(view)
        clickStart(view)
    }

    private fun isMyForegroundServiceRunning(): Boolean {
        val myServiceName = MyForegroundService::class.java.name
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (runningService in activityManager.getRunningServices(Int.MAX_VALUE)) {
            val runningServiceName = runningService.service.className
            if (runningServiceName == myServiceName) {
                return true
            }
        }
        return false
    }
}