package com.example.foser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        val buttonRestart = findViewById<Button>(R.id.buttonRestart)
        val buttonStop = findViewById<Button>(R.id.buttonStop)

        buttonStart.setOnClickListener {
            clickStart(findViewById(R.id.content))
        }

        buttonRestart.setOnClickListener {
            clickRestart(findViewById(R.id.content))
        }

        buttonStop.setOnClickListener {
            clickStop(findViewById(R.id.content))
        }

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

    private fun clickStart(view: View?) {
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show()
    }

    private fun clickStop(view: View?) {
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
    }

    private fun clickRestart(view: View?) {
        clickStop(view)
        clickStart(view)
    }
}