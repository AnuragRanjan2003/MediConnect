package com.example.project2.repo

import com.example.project2.api.EndlessMedicalApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EndlessMedicalApiInstance {
    val api: EndlessMedicalApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://endlessmedicalapi1.p.rapidapi.com")
            .build()
            .create(EndlessMedicalApi::class.java)
    }
}