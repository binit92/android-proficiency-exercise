package com.example.apps.myapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity(), OnTaskCompleted {

    private val TAG = MainActivity::class::simpleName.toString()
    private var sync: Sync? = null

    private lateinit var entries: ArrayList<Sync.Entry>

    // TODO: see ButterKnife binding later.. it is throwing some error.
    //@BindView(R.id.list_view) lateinit var listView: ListView
    //@BindView(R.id.pullToRefresh) lateinit var pullToRefresh:SwipeRefreshLayout
    //@BindView(R.id.error_text) lateinit var errorTextView: TextView

    private lateinit var listView: ListView
    private lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var errorTextView: TextView

    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ButterKnife.bind(this)
        listView = findViewById(R.id.list_view)
        pullToRefresh = findViewById(R.id.pullToRefresh)
        errorTextView = findViewById(R.id.error_text)

        refresh()

        pullToRefresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun setActionBar(factTitle: String) {
        try {
            supportActionBar?.title = factTitle

        } catch (np: NullPointerException) {
            Log.e(TAG, "setActionBar: error setting title in actionbar")
        }
    }

    private fun refresh() {
        sync = Sync(applicationContext, this)
        sync!!.getJson()

    }


    override fun onTaskCompleted() {

        errorTextView.visibility = View.GONE
        pullToRefresh.visibility = View.VISIBLE

        Log.i(TAG, "onTaskCompleted: fact title " + sync!!.facts.factTitle)
        val factTitle: String? = sync!!.facts.factTitle
        if (factTitle != null) {
            setActionBar(factTitle)
        }

        Log.i(TAG, "onTaskCompleted: number of entries: " + (sync!!.facts.entries?.size ?: 0))
        entries = sync!!.facts.entries!!

        adapter = CustomAdapter(applicationContext, entries)
        listView.adapter = adapter

    }

    override fun onError() {
        pullToRefresh.visibility = View.GONE

        errorTextView.visibility = View.VISIBLE
        errorTextView.bringToFront()
        errorTextView.text = applicationContext.getString(R.string.network_error)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_refresh -> {
            refresh()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}