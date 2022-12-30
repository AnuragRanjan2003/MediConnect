package com.example.project2.models.features

import com.google.gson.annotations.SerializedName

data class Feature(
    @SerializedName("SessionID")
    val sessionId: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("name")
    val featureName: String
)

