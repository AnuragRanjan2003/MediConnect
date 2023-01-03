package com.example.project2.repo

import com.example.project2.api.Wikipedia
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WikiApiInstance {
    private const val base_url = "https://en.wikipedia.org/w/rest.php/"
    val api: Wikipedia by lazy {
        Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create())
            .build().create(Wikipedia::class.java)
    }
}