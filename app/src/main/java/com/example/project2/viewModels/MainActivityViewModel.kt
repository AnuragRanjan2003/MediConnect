package com.example.project2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project2.models.SaveDataModel

class MainActivityViewModel : ViewModel() {
    private val list: MutableLiveData<List<SaveDataModel>> by lazy { MutableLiveData<List<SaveDataModel>>() }
    private val filteredList: MutableLiveData<List<SaveDataModel>> by lazy { MutableLiveData<List<SaveDataModel>>() }


    fun setList(list: List<SaveDataModel>) {
        this.list.value = list
    }

    fun getList(): LiveData<List<SaveDataModel>> {
        return list
    }

    fun setFilteredList(list: List<SaveDataModel>) {
        this.filteredList.value = list
    }

    fun getFilteredList(): LiveData<List<SaveDataModel>> {
        return filteredList
    }
}