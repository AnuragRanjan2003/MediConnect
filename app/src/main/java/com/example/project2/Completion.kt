package com.example.project2

import com.example.project2.models.SaveDataModel

interface Completion {
    fun onComplete(dataModel: SaveDataModel = SaveDataModel(),position : Int=0)
    fun onCancelled(name:String="Error",message : String="Error")
}