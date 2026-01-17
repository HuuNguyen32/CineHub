package nhn.ntech.cinehub.presentation.views.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.callbacks.AuthCallBack
import nhn.ntech.cinehub.databinding.ActivityCreateProfileBinding
import nhn.ntech.cinehub.presentation.viewmodels.AuthViewModel
import nhn.ntech.cinehub.presentation.views.home.MainActivity

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPadding()

        authViewModel.uploadResult.observe(this) { result ->
            if (result.isSuccess) {
                val photoUrl = result.getOrNull()
                authViewModel.currentPhotoUrl = photoUrl
                Log.d("CHECK", photoUrl ?: "")
            }else{
                Toast.makeText(this, "Upload ảnh thất bại", Toast.LENGTH_SHORT).show()
            }
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            uri ->
            if (uri != null){
                binding.imgCreateProfile.setImageURI(uri)
                authViewModel.uploadImage(uri)
            }
            else{
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgCreate.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnAgreeAndStart.setOnClickListener {
            Log.d("CHECK", "on btn agree and start click")
            val email = intent.getStringExtra("email") ?: ""
            val birthDay = intent.getStringExtra("birthDay") ?: ""
            val createAt = intent.getLongExtra("createAt", 0)
            val profileName = binding.edtProfileName.text.toString().trim()

            val photoUrl = authViewModel.currentPhotoUrl
            if (photoUrl.isNullOrEmpty()) {
                Toast.makeText(this, "Bạn chưa chọn ảnh hoặc ảnh chưa upload xong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.saveUserProfile(
                email, birthDay, profileName, photoUrl, createAt,
                object : AuthCallBack {
                    override fun onSuccess() {
                        Toast.makeText(this@CreateProfileActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CreateProfileActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }

                    override fun onFailed(message: String) {
                        Toast.makeText(this@CreateProfileActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
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