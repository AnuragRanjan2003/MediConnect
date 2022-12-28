package com.example.project2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.project2.databinding.ActivityLoginBinding
import com.example.project2.uiComponents.AnimatedButton
import com.example.project2.viewModels.LoginActivityViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginActivityViewModel
    private lateinit var gButton: View
    private lateinit var button: AnimatedButton
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = Firebase.auth
        checkUser()
        viewModel = ViewModelProvider(this)[LoginActivityViewModel::class.java]
        gButton = findViewById(R.id.btn_google)
        button = AnimatedButton(
            text = "Login with Google",
            icon = AppCompatResources.getDrawable(this, R.drawable.google_logo)!!,
            changeText = "Please wait..",
            view = gButton
        )

        binding.etEmail.doAfterTextChanged { viewModel.email.value = it.toString() }
        binding.etPass.doAfterTextChanged { viewModel.pass.value = it.toString() }

        binding.btnLogin.setOnClickListener {
            binding.prg.visibility = View.VISIBLE
            viewModel.login(binding, this)
        }

        gButton.setOnClickListener {
            val intent = viewModel.loginWithGoogle(button, this)
            launcher.launch(intent)
        }

        binding.tvSignUp.setOnClickListener {
                startActivity(Intent(this,SignUpActivity::class.java))
                finishAffinity()
        }


    }

    private fun checkUser() {
        if(mAuth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                viewModel.handleResult(task, binding, this, button)
            }
        }
}