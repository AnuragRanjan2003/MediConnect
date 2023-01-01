package com.example.project2.viewModels

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project2.MainActivity
import com.example.project2.databinding.ActivityLoginBinding
import com.example.project2.models.User
import com.example.project2.uiComponents.AnimatedButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivityViewModel : ViewModel() {
    private val webClientId =
        "99593684623-qv422jv6ibmrjafgn6onuulucmmk56ue.apps.googleusercontent.com"
    private val mAuth = Firebase.auth
    private val database = Firebase.database

    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pass: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(webClientId)
            .requestEmail().build()


    fun login(binding: ActivityLoginBinding, activity: AppCompatActivity) {
        if (email.value.isNullOrBlank()) {
            binding.etEmail.error = "Please provide email"
            binding.prg.visibility = View.GONE
            return
        } else if (pass.value.isNullOrBlank()) {
            binding.etPass.error = "Please provide password"
            binding.prg.visibility = View.GONE
            return
        } else {
            mAuth.signInWithEmailAndPassword(email.value!!, pass.value!!).addOnSuccessListener {
                activity.startActivity(Intent(activity, MainActivity::class.java))
            }.addOnFailureListener {
                binding.prg.visibility = View.GONE
                Snackbar.make(
                    binding.root,
                    it.message.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    fun loginWithGoogle(button: AnimatedButton, activity: AppCompatActivity): Intent {
        button.activate()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        return googleSignInClient.signInIntent

    }

    fun handleResult(
        task: Task<GoogleSignInAccount>,
        binding: ActivityLoginBinding,
        activity: AppCompatActivity,
        button: AnimatedButton
    ) {
        if (task.isSuccessful) {
            val account = task.result
            if (account != null)
                updateUI(account, binding, activity, button)
            else Snackbar.make(
                binding.root,
                task.exception?.message.toString(),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun updateUI(
        account: GoogleSignInAccount,
        binding: ActivityLoginBinding,
        activity: AppCompatActivity,
        button: AnimatedButton
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful)
                saveUserData(it.result.user!!, activity, binding, button)
            else {
                Snackbar.make(binding.root, it.exception?.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
                button.deactivate()
            }
        }
    }

    private fun saveUserData(
        fUser: FirebaseUser,
        activity: AppCompatActivity,
        binding: ActivityLoginBinding,
        button: AnimatedButton
    ) {
        val user = User(fUser.displayName, fUser.email, fUser.uid, fUser.photoUrl.toString())
        database.getReference("users").child(user.uid!!).setValue(user).addOnSuccessListener {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finishAffinity()
        }
            .addOnFailureListener {
                button.deactivate()
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
            }
    }


}