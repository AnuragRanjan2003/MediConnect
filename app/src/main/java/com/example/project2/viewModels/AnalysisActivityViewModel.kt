package com.example.project2.viewModels

import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project2.Completion
import com.example.project2.models.SaveDataModel
import com.example.project2.models.features.FeaturesError
import com.example.project2.models.wiki.WikiResult
import com.example.project2.repo.WikiApiInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnalysisActivityViewModel : ViewModel() {
    private val item: MutableLiveData<SaveDataModel> by lazy { MutableLiveData<SaveDataModel>() }
    private val details1: MutableLiveData<WikiResult> by lazy { MutableLiveData<WikiResult>() }
    private val details2: MutableLiveData<WikiResult> by lazy { MutableLiveData<WikiResult>() }
    private val details3: MutableLiveData<WikiResult> by lazy { MutableLiveData<WikiResult>() }
    private val list = listOf(details1, details2, details3)
    private val errors = FeaturesError()

    fun getResults(name: String, comp: Completion) {
        Firebase.database.getReference("Reports").child(Firebase.auth.currentUser!!.uid).child(name)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(SaveDataModel::class.java)
                    item.value = data
                    comp.onComplete()
                }

                override fun onCancelled(error: DatabaseError) {
                    comp.onCancelled("database error", error.message)
                }
            })
    }

    fun getDetails(listQuery: ArrayList<String>) {
        getResultFor(0, listQuery)
    }

    private fun getResultFor(i: Int, qlist: ArrayList<String>) {
        viewModelScope.launch(Dispatchers.IO) {

            val response = WikiApiInstance.api.getResult(errors.correctName(qlist[i]), 1)
            if (response.isSuccessful) {
                launch(Dispatchers.Main) {
                    list[i].value = response.body()
                }
                if (i <= 1)
                    getResultFor(i + 1, qlist)
                else
                    return@launch
            } else {
                e("wiki error", response.errorBody().toString())
            }
        }
    }


    fun observeData(): LiveData<SaveDataModel> {
        return item
    }

    fun observeDetail1(): LiveData<WikiResult> {
        return details1
    }

    fun observeDetail2(): LiveData<WikiResult> {
        return details2
    }

    fun observeDetail3(): LiveData<WikiResult> {
        return details3
    }
}