package com.example.hackerflow.topstories

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hackerflow.R
import com.example.hackerflow.network.HNItem
import kotlinx.android.synthetic.main.view_story_item.view.*

class TopStoriesRecyclerViewAdapter: RecyclerView.Adapter<TopStoryViewHolder>() {

    var stories = mutableListOf<HNItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopStoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_story_item, parent, false)
        return TopStoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onBindViewHolder(holder: TopStoryViewHolder, position: Int) {
        holder.view.storyNameTextView.text = stories[position].title
        holder.view.storyTextSnippet.text = stories[position].text
        // todo -> remove when we have a way to display snippet preview
        holder.view.storyTextSnippet.visibility = GONE
    }

}

class TopStoryViewHolder(val view: View): RecyclerView.ViewHolder(view)