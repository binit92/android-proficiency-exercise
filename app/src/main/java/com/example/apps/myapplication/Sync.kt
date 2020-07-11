package com.example.apps.myapplication

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Sync(val context: Context, val listener: OnTaskCompleted) {

    private val TAG = Sync::class::simpleName.toString()
    private val REST_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"

    companion object

    val facts = Facts()

    fun getJson(): Facts {
        Log.d(TAG, "getJson ")
        try {
            // Instantiate the cache
            val cache = DiskBasedCache(context.cacheDir, 1024 * 1024) // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            val network = BasicNetwork(HurlStack())

            // Instantiate the RequestQueue with the cache and network. Start the queue.
            val requestQueue = RequestQueue(cache, network).apply {
                start()
            }

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, REST_URL, null,
                Response.Listener { response ->

                    Log.i(TAG, "getJson: Response: " + response.toString())
                    parseJson(response)
                },
                Response.ErrorListener { error ->

                    Log.e(TAG, "getJson: onErrorResponse: network not available" + error.message)
                    listener.onError()
                }
            )
            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest)

        } catch (e: Exception) {
            Log.d(TAG, "getJson: some error occured while getting Json")
        }
        return facts
    }

    private fun parseJson(json: JSONObject) {
        Log.i(TAG, "parseJson: ")
        try {
            val factTitle: String = json.getString("title")
            val factRow: JSONArray = json.getJSONArray("rows")
            Log.i(TAG, "parseJson: factTitle" + factTitle)

            facts.factTitle = factTitle

            val entries: ArrayList<Entry> = ArrayList()
            for (i in 0 until factRow.length()) {
                val row: JSONObject = factRow.getJSONObject(i)
                val title: String = row.getString("title")
                val desc: String = row.getString("description")
                val imageUrl: String = row.getString("imageHref")

                // don't add it if title is empty ..
                if (!title.equals("null") && !title.isEmpty()) {
                    val entry = Entry()
                    entry.title = title
                    entry.desc = desc
                    entry.imageref = imageUrl

                    entries.add(entry)
                }
            }

            facts.entries = entries

            listener.onTaskCompleted()
        } catch (je: JSONException) {
            Log.e(TAG, "parseJson: json execption occured")
        }
    }

    // POJO
    class Facts {
        var factTitle: String? = null
        var entries: ArrayList<Entry>? = ArrayList()

    }

    //POJO
    class Entry {
        var title: String? = null
        var desc: String? = null
        var imageref: String? = null
    }
}