package com.example.hackerflow.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsAPI {

    @GET("item/{id}")
    suspend fun getItem(@Path("id") id: String): Response<HNItem>

    @GET("topstories")
    suspend fun getTopStories(): Response<MutableList<Int>>

    @GET("newstories")
    suspend fun getNewStories(): Response<MutableList<Int>>

    @GET("jobstories")
    suspend fun getJobStories(): Response<MutableList<Int>>

}

/**
 * An 'item' returned from the Hacker News API. Can represent almost anything on the site,
 * including stories, comments, etc.
 *
 * https://github.com/HackerNews/API
 */
data class HNItem(
    val id: Int?,
    val deleted: Boolean? = false,
    val type: String? = null,
    val by: String? = null,
    val time: Int? = null,
    val text: String? = null,
    val dead: Boolean? = false,
    val parent: Int? = null,
    val poll: Int? = null,
    val kids: MutableList<Int>? = null,
    val url: String? = null,
    val score: Int? = null,
    val title: String? = null,
    val parts: MutableList<Int>? = null,
    val descendants: Int? = null
)