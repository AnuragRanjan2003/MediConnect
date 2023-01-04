package com.example.project2

import com.example.project2.models.SaveDataModel

interface Completion {
    fun onComplete(dataModel: SaveDataModel = SaveDataModel())
    fun onCancelled(name:String="Error",message : String="Error")
}