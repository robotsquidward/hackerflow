package com.example.hackerflow

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackerflow.network.HackerNewsRepository
import com.example.hackerflow.network.HackerNewsRetrofit
import com.example.hackerflow.topstories.TopStoriesRecyclerViewAdapter
import com.example.hackerflow.topstories.TopStoriesViewModel
import com.example.hackerflow.util.createWithFactory
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*

class ScrollingActivity : AppCompatActivity() {

    private val topStoriesViewModel: TopStoriesViewModel by viewModels {
        createWithFactory {
            TopStoriesViewModel(repo = HackerNewsRepository(HackerNewsRetrofit.retrofitInstance))
        }
    }
    private val adapter = TopStoriesRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)

        topStoriesRecyclerView.adapter = adapter
        topStoriesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        topStoriesViewModel.getTopStories()
        topStoriesViewModel.topStories.observe(this, Observer {
            it?.let { stories -> adapter.stories = stories }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
