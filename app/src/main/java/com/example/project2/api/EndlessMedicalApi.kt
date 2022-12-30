package com.example.project2.api

import com.example.project2.models.Terms.TermsResponse
import com.example.project2.models.initSession.SessionResponse
import retrofit2.Call
import retrofit2.http.*

interface EndlessMedicalApi {
    @GET("/InitSession")
    fun initSession(
        @Header("X-RapidAPI-Key")
        apiKey: String,
        @Header("X-RapidAPI-Host")
        hostKey: String
    ): Call<SessionResponse>

    @POST("/AcceptTermsOfUse")
    fun acceptTerms(
        @Header("X-RapidAPI-Key")
        apiKey: String,
        @Header("X-RapidAPI-Host")
        hostKey: String,
        @Query("passphrase")
        passPhrase : String,
        @Query("SessionID")
        sessionId: String

    ): Call<TermsResponse>


    @POST("/UpdateFeature")
    fun updateFeatures(
        @Header("X-RapidAPI-Key")
        apiKey: String,
        @Header("X-RapidAPI-Host")
        hostKey: String,
        @Query("SessionID")
        sessionId: String,
        @Query("value")
        value: String,
        @Query("name")
        name: String
    ): Call<TermsResponse>

    @GET("/Analyze")
    fun getAnalysis(
        @Header("X-RapidAPI-Key")
        apiKey: String,
        @Header("X-RapidAPI-Host")
        hostKey: String,
        @Query("SessionID")
        sessionId: String
    ): Call<Any>


}