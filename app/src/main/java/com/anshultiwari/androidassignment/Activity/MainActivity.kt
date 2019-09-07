package com.anshultiwari.androidassignment.Activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.anshultiwari.androidassignment.Adapter.CelebsAdapter
import com.anshultiwari.androidassignment.Model.Celebrity
import com.anshultiwari.androidassignment.MyVolley
import com.anshultiwari.androidassignment.R
import com.anshultiwari.androidassignment.Utilities.CenterZoomLayoutManager
import com.anshultiwari.androidassignment.Utilities.Util
import com.anshultiwari.androidassignment.ViewModel.CelebViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mCelebsRecyclerView: RecyclerView
    private lateinit var mCelebsAdapter: CelebsAdapter
    private lateinit var mCelebsList: List<Celebrity>
    private lateinit var mPb: ProgressBar
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var mSpinner: Spinner
    private lateinit var mToolbar: Toolbar

    private lateinit var celebViewModel: CelebViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCelebsList = ArrayList()
        mRequestQueue = MyVolley.getInstance().requestQueue

        // Setup views
        mCelebsRecyclerView = findViewById(R.id.celebs_rv)
        mPb = findViewById(R.id.pb)
        mSpinner = findViewById(R.id.sort_spinner)
        mToolbar = findViewById(R.id.toolbar)

        setSupportActionBar(mToolbar)
        supportActionBar!!.title = ""


        // Get the ViewModel
        celebViewModel = ViewModelProviders.of(this).get(CelebViewModel::class.java)

        // Check for internet
        if (!Util.isNetworkAvailable()) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show()

        } else {
            celebViewModel.fetchCelebsAndStore()
        }

        celebViewModel.getAllCelebs().observe(this, Observer { celebrities ->
            Log.d(TAG, "onChanged: called")
            Log.d(TAG, "onChanged: celebrities size = " + celebrities.size)
            mCelebsList = celebrities

            setupRecyclerView(celebrities)
            setupSortSpinner()
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.view_menu, menu)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.grid -> {
                mCelebsRecyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
                mCelebsRecyclerView.adapter = mCelebsAdapter
                return true
            }

            R.id.list -> {
                mCelebsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                mCelebsRecyclerView.adapter = mCelebsAdapter
                return true
            }

            R.id.carousel -> {
                mCelebsRecyclerView.layoutManager = CenterZoomLayoutManager(this, RecyclerView.HORIZONTAL, false)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

    }

    private fun setupRecyclerView(celebsList: List<Celebrity>) {

        mCelebsAdapter = CelebsAdapter(this, celebsList)
        mCelebsRecyclerView.adapter = mCelebsAdapter
        mCelebsRecyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        mCelebsRecyclerView.setHasFixedSize(true)
    }

    private fun setupSortSpinner() {
        val adapter = ArrayAdapter.createFromResource(this@MainActivity,
                R.array.sort, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner.adapter = adapter

        mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                when (position) {
                    0 -> sortByAge()
                    1 -> sortByHeight()
                    2 -> sortByPopularity()
                }
                mCelebsAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun sortByAge() {
        Collections.sort(mCelebsList) { obj1, obj2 ->
            obj1.age.compareTo(obj2.age, ignoreCase = true) // To sort by age in ascending order
        }
    }

    private fun sortByHeight() {
        Collections.sort(mCelebsList) { obj1, obj2 ->
            obj1.height.compareTo(obj2.height, ignoreCase = true) // To sort by height in ascending order
        }
    }

    private fun sortByPopularity() {
        Collections.sort(mCelebsList) { obj1, obj2 ->
            obj1.popularity.compareTo(obj2.popularity, ignoreCase = true) // To sort by height in ascending order
        }
    }

    companion object {
        private val TAG = "MainActivity"
    }
}
