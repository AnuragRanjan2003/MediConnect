package com.example.project2.api

import com.example.project2.models.wiki.WikiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Wikipedia {

    @GET("v1/search/page")
    suspend fun getResult(
        @Query("q")
        query: String,
        @Query("limit")
        limit: Int
    ): Response<WikiResult>
}