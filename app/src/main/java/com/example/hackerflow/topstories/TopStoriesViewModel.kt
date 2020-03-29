package com.example.hackerflow.topstories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackerflow.network.HNItem
import com.example.hackerflow.network.HackerNewsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import kotlin.math.min

private const val TOP_STORIES_KEY = "top_stories_state"

class TopStoriesViewModel(
    private val savedStateHandler: SavedStateHandle = SavedStateHandle(),
    private val repo: HackerNewsRepository,
    private val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable: Throwable ->
        Log.e("KTX_EXE", throwable.localizedMessage ?: throwable.message ?: throwable.stackTrace.toString())
    }
): ViewModel() {

    val topStories: LiveData<MutableList<HNItem>> = savedStateHandler.getLiveData(TOP_STORIES_KEY, mutableListOf())

    fun getTopStories(fresh: Boolean = false) {
        if (fresh || topStories.value.isNullOrEmpty()) {
            viewModelScope.launch(coroutineExceptionHandler) {
                val response =  repo.getTopStories()
                response.body()?.let { topStoryIds ->
                    val stories = mutableListOf<HNItem>()
                    for (index in 0..29) {
                        if (index < topStoryIds.size) {
                            repo.getItem(topStoryIds[index].toString()).body()?.let { itemDetail ->
                                stories.add(itemDetail)
                            }
                        }
                    }
                    savedStateHandler.set(TOP_STORIES_KEY, stories)
                    // todo -> remove when getting snippets is easier
                    //updateStoriesWithSnippets()
                }
            }
        }
    }

    /**
     * Method to pull text snippets from web content. Needs to be updated to
     * pull useful snippets from websites.
     */
    private fun updateStoriesWithSnippets() {
        topStories.value?.let { stories ->
            for (item in stories) {
                if (item.text.isNullOrBlank()) {
                    CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                        val doc = Jsoup.connect(item.url).get()
                        val body: Element = doc.body()
                        val text = body.text()
                        val truncatedText = text.substring(0, min(text.length, 300))
                        item.text = truncatedText
                    }
                } else {
                    item.text = item.text?.substring(0, min(item.text?.length ?: 0, 300)) ?: ""
                }
            }
        }
    }
}