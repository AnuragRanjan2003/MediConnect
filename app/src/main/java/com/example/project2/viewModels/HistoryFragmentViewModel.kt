package com.example.project2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project2.models.SaveDataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HistoryFragmentViewModel : ViewModel() {
    private val list: MutableLiveData<ArrayList<SaveDataModel>> by lazy { MutableLiveData<ArrayList<SaveDataModel>>() }
    private val filteredList: MutableLiveData<ArrayList<SaveDataModel>> by lazy { MutableLiveData<ArrayList<SaveDataModel>>() }
    private val unFilteredList: MutableLiveData<ArrayList<SaveDataModel>> by lazy { MutableLiveData<ArrayList<SaveDataModel>>() }
    private val user = Firebase.auth.currentUser

    fun getData() {
        Firebase.database.getReference("Reports").child(user!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list1 = ArrayList<SaveDataModel>()
                    val list2 = ArrayList<SaveDataModel>()
                    val list3 = ArrayList<SaveDataModel>()
                    for (item in snapshot.children) {
                        val obj = item.getValue(SaveDataModel::class.java)
                        if (obj != null) {
                            list1.add(obj)
                            if (!obj.archive)
                                list2.add(obj)
                            else
                                list3.add(obj)
                        }
                    }
                    list.value = list1
                    filteredList.value=  list2
                    unFilteredList.value = list3
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    fun observeData(): LiveData<ArrayList<SaveDataModel>> {
        return list
    }

    fun observeFilteredList():LiveData<ArrayList<SaveDataModel>>{
        return filteredList
    }

    fun observeUnFilteredList():LiveData<ArrayList<SaveDataModel>>{
        return unFilteredList
    }


}