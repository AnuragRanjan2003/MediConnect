package com.example.project2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project2.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileFragmentViewModel : ViewModel() {
    private val user: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    fun getData(){
        viewModelScope.launch(Dispatchers.Main){ getUser() }
    }


    private suspend fun getUser() {
        val def = viewModelScope.async {
            Firebase.database.getReference("users").child(Firebase.auth.currentUser!!.uid).get()
                .await().getValue(User::class.java)
        }
        user.value = def.await()
    }

    fun observeUser():LiveData<User>{
        return user
    }


}