package com.example.apps.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnTaskCompleted {

    private val TAG = "MainActivity"
    private var sync: Sync? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        syncData()

    }

    private fun syncData() {
        sync = Sync(applicationContext, this)
        sync!!.getJson()

    }

    override fun onTaskCompleted() {
        Log.i(TAG, "onTaskCompleted: fact title " + sync!!.facts.factTitle)
        Log.i(TAG, "onTaskCompleted: number of entries: " + (sync!!.facts.entries?.size ?: 0))
    }
}