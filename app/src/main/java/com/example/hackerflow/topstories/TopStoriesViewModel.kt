package com.example.hackerflow.topstories

import android.util.Log
import androidx.lifecycle.*
import com.example.hackerflow.network.HNItem
import com.example.hackerflow.network.HackerNewsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

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
                    for (item in topStoryIds) {
                        repo.getItem(item.toString()).body()?.let { itemDetail ->
                            stories.add(itemDetail)
                        }
                    }
                    savedStateHandler.set(TOP_STORIES_KEY, stories)
                }
            }
        }
    }

}