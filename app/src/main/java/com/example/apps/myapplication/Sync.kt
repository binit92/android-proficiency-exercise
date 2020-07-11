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


class Sync(val context: Context) {

    private val TAG = "Sync"
    private val REST_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"

    fun getJson() {
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
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                    Log.e(TAG, "getJson: onErrorResponse: network not available")
                }
            )

            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest)
        } catch (e: Exception) {
            Log.d(TAG, "getJson: some error occured while getting Json")
        }

    }

    private fun parseJson(json: String) {

    }
}