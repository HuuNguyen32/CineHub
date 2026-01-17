package nhn.ntech.cinehub.presentation.views.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.callbacks.AuthCallBack
import nhn.ntech.cinehub.databinding.FragmentPersonalBinding
import nhn.ntech.cinehub.presentation.viewmodels.AuthViewModel
import nhn.ntech.cinehub.presentation.viewmodels.UserViewModel
import kotlin.getValue

@AndroidEntryPoint
class PersonalFragment : Fragment() {

    private lateinit var binding: FragmentPersonalBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setEvent()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        authViewModel.uploadResult.observe(this) { result ->
            if (result.isSuccess) {
                val photoUrl = result.getOrNull()
                authViewModel.currentPhotoUrl = photoUrl
                Log.d("CHECK", photoUrl ?: "")
            }else{
                Toast.makeText(requireContext(), "Upload ảnh thất bại", Toast.LENGTH_SHORT).show()
            }
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
                uri ->
            if (uri != null){
                binding.imgProfile.setImageURI(uri)
                authViewModel.uploadImage(uri)
            }
            else{
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgCreate.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnAgreeAndStart.setOnClickListener {
            val photoUrl = authViewModel.currentPhotoUrl
            val profileName = binding.edtProfileName.text.toString().trim()
            val birthDay = binding.edtBirthday.text.toString().trim()

            if (photoUrl.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Bạn chưa chọn ảnh hoặc ảnh chưa upload xong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.updateUserProfile(
                profileName,
                birthDay,
                photoUrl,
                object : AuthCallBack {
                    override fun onSuccess() {
                        Toast.makeText(requireContext(), "Sửa thành công", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }

                    override fun onFailed(message: String) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun initData() {
        val uid = userViewModel.currentUid
        if (uid != null){
            userViewModel.loadUserProfile(uid)
            userViewModel.userProfile.observe(viewLifecycleOwner){
                    userProfile ->
                if (userProfile != null){
                    Glide.with(requireActivity())
                        .load(userProfile.photoUrl)
                        .centerCrop()
                        .into(binding.imgProfile)

                    binding.edtProfileName.setText(userProfile.profileName)
                    binding.edtBirthday.setText(userProfile.birthDay)
                }
            }
        }
    }
}