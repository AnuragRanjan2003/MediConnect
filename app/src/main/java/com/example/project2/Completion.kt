package com.example.project2

interface Completion {
    fun onComplete()
    fun onCancelled(name:String="Error",message : String="Error")
}