package com.example.project2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.project2.databinding.ActivitySignUpBinding
import com.example.project2.viewModels.SignUpActivityViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this)[SignUpActivityViewModel::class.java]

        binding.etEmail.doAfterTextChanged { viewModel.email.value = it.toString() }
        binding.etPass.doAfterTextChanged { viewModel.pass.value = it.toString() }
        binding.etName.doAfterTextChanged { viewModel.name.value = it.toString() }

        binding.picture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            launcher.launch(intent)
        }

        binding.btnSignUp.setOnClickListener {
            binding.prg.visibility = View.VISIBLE
            viewModel.signUp(binding = binding, activity = this)
        }

        binding.back.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finishAffinity()
        }

    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                binding.done.visibility = View.VISIBLE
                viewModel.uri.value = it.data!!.data
            }
        }

}