package nhn.ntech.cinehub.presentation.views.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.callbacks.AuthCallBack
import nhn.ntech.cinehub.databinding.ActivityRegisterBinding
import nhn.ntech.cinehub.presentation.viewmodels.AuthViewModel

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPadding()
        setEvent()
    }

    private fun setEvent() {
        setEventBack()
        setEventRegister()
    }

    private fun setEventRegister() {
        binding.btnAgreeAndContinue.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val birthDay = binding.edtBirthday.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
            val createAt = System.currentTimeMillis()

            authViewModel.register(
                email,
                birthDay,
                password,
                confirmPassword,
                object : AuthCallBack{
                    override fun onSuccess() {
                        val intent = Intent(
                            this@RegisterActivity,
                            CreateProfileActivity::class.java
                        ).apply {
                            putExtra("email", email)
                            putExtra("birthDay", birthDay)
                            putExtra("createAt", createAt)
                        }
                        startActivity(intent)
                    }

                    override fun onFailed(message: String) {
                        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }
    }

    private fun setEventBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}