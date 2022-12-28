package com.example.project2.viewModels

import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project2.MainActivity
import com.example.project2.databinding.ActivitySignUpBinding
import com.example.project2.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SignUpActivityViewModel : ViewModel() {
    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pass: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val name: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val uri: MutableLiveData<Uri> by lazy { MutableLiveData<Uri>() }
    private val mAuth = Firebase.auth
    private val database = Firebase.database
    private val storage = Firebase.storage

    fun signUp(binding: ActivitySignUpBinding, activity: AppCompatActivity) {
        if (email.value.isNullOrBlank()) {
            binding.etEmail.error = "Please Provide Email"
            binding.prg.visibility = View.GONE
            return
        } else if (pass.value.isNullOrBlank()) {
            binding.etPass.error = "Please provide password"
            binding.prg.visibility = View.GONE
            return
        } else if (name.value.isNullOrBlank()) {
            binding.etName.error = "Please provide Name"
            binding.prg.visibility = View.GONE
            return
        } else if (uri == null) {
            binding.prg.visibility = View.GONE
            Snackbar.make(binding.root, "No profile image selected", Snackbar.LENGTH_LONG).show()
            return
        } else {
            mAuth.createUserWithEmailAndPassword(email.value!!, pass.value!!).addOnSuccessListener {
                saveProfileImage(mAuth.currentUser!!.uid, activity, binding)
            }.addOnFailureListener {
                binding.prg.visibility = View.GONE
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }

    }

    private fun saveProfileImage(
        uid: String,
        activity: AppCompatActivity,
        binding: ActivitySignUpBinding
    ) {
        val ref = storage.getReference("photos/${uid}.${getFileExtension(uri.value!!, activity)}")
        ref.putFile(uri.value!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                saveData(uid, activity, it, binding)
            }.addOnFailureListener {
                binding.prg.visibility = View.GONE
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            binding.prg.visibility = View.GONE
            Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun saveData(
        uid: String,
        activity: AppCompatActivity,
        it: Uri?,
        binding: ActivitySignUpBinding
    ) {
        val user = User(email = email.value, name = name.value, photoUrl = it.toString(), uid = uid)
        database.getReference("users").child(uid).setValue(user).addOnSuccessListener {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finishAffinity()
        }.addOnFailureListener {
            binding.prg.visibility = View.GONE
            Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getFileExtension(uri: Uri, activity: AppCompatActivity): String? {
        val cr = activity.contentResolver
        val map = MimeTypeMap.getSingleton()
        return map.getExtensionFromMimeType(cr.getType(uri))
    }
}