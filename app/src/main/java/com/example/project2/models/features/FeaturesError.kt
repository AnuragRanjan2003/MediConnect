package com.example.project2.models.features

class FeaturesError {
    fun correctName(s: String): String {
        return when (s) {
            errorMelltus -> "Diabetes mellitus type 1"
            else -> s
        }
    }

    companion object {
        val errorMelltus = "Diabetes melltus type 1"
    }

}