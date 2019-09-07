package com.anshultiwari.androidassignment.Repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.anshultiwari.androidassignment.Database.CelebDatabase
import com.anshultiwari.androidassignment.Database.CelebrityDao
import com.anshultiwari.androidassignment.Model.Celebrity
import com.anshultiwari.androidassignment.MyVolley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class CelebRepository(application: Application) {

    private val celebrityDao: CelebrityDao
    private val celebs: LiveData<List<Celebrity>>
    private val context: Context
    private val mRequestQueue: RequestQueue

    init {
        context = application
        celebrityDao = CelebDatabase.getInstance(application).celebrityDao()
        celebs = celebrityDao.getAllCelebs()
        mRequestQueue = MyVolley.getInstance().requestQueue
    }

    fun getAllCelebs(): LiveData<List<Celebrity>> {
        return celebs
    }


    fun celebsApi() {
        Log.d(TAG, "celebsApi: called")
        val request = StringRequest(Request.Method.GET, "https://api.myjson.com/bins/6e60g",
                Response.Listener { response ->
                    val celebsRemote = ArrayList<Celebrity>()

                    Log.d(TAG, "onResponse: celebs list response = $response")

                    try {
                        val responseObj = JSONObject(response)
                        val celebsObj = responseObj.getJSONObject("celebrities")

                        val iterator = celebsObj.keys() // Fetch all the keys

                        while (iterator.hasNext()) {
                            val celebKey = iterator.next()
                            val singleCelebObj = celebsObj.getJSONObject(celebKey)
                            val celebHeight = singleCelebObj.getString("height")
                            val celebAge = singleCelebObj.getString("age")
                            val celebPopularity = singleCelebObj.getString("popularity")
                            val celebImageUrl = singleCelebObj.getString("photo")

                            val celebrity = Celebrity(celebKey, celebHeight, celebAge, celebPopularity, celebImageUrl)
                            celebsRemote.add(celebrity)

                        }

                        // Store the celebs retreived from API to the database
                        Thread(Runnable { celebrityDao.insertAllCelebs(celebsRemote) }).start()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
            try {
                Log.e(TAG, "onErrorResponse: error = " + error.message)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        request.retryPolicy = DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        mRequestQueue.add(request)

    }

    companion object {
        private val TAG = "CelebRepository"
    }
}
