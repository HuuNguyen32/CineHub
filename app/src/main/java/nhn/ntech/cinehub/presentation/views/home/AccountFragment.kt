package nhn.ntech.cinehub.presentation.views.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.FragmentAccountBinding
import nhn.ntech.cinehub.presentation.viewmodels.AuthViewModel
import nhn.ntech.cinehub.presentation.viewmodels.UserViewModel
import nhn.ntech.cinehub.presentation.views.auth.LoginActivity


@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.isLoggedIn()){
            initTopBar()

            binding.tvLogout.setOnClickListener {
                showSimpleLogoutDialog(requireContext()){
                    viewModel.logout()
                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }

            binding.tvPersonalInformation.setOnClickListener {
                val action = AccountFragmentDirections.actionAccountFragmentToPersonalFragment()
                findNavController().navigate(action)
            }

            binding.tvHistory.setOnClickListener {
                val action = AccountFragmentDirections.actionAccountFragmentToHistoryFragment()
                findNavController().navigate(action)
            }

            binding.tvChangePassword.setOnClickListener {
                val action = AccountFragmentDirections.actionAccountFragmentToChangePasswordFragment()
                findNavController().navigate(action)
            }

            binding.tvSetting.setOnClickListener {
                val action = AccountFragmentDirections.actionAccountFragmentToSettingFragment()
                findNavController().navigate(action)
            }
        }else{
            binding.tvLogout.apply {
                text = "Đăng nhập"
                setOnClickListener {
                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }
        }
    }

    private fun initTopBar() {
        val uid = userViewModel.currentUid
        if (uid != null){
            userViewModel.loadUserProfile(uid)
            userViewModel.userProfile.observe(viewLifecycleOwner){
                    userProfile ->
                if (userProfile != null){
                    Glide.with(requireActivity())
                        .load(userProfile.photoUrl)
                        .centerCrop()
                        .into(binding.imgAvatar)

                    binding.tvUsername.text = userProfile.profileName
                    binding.tvEmail.text = userProfile.email
                }
            }
        }
    }


    fun showSimpleLogoutDialog(context: Context, onLogoutConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản?")
            .setPositiveButton("Đăng xuất") { dialog, _ ->
                onLogoutConfirmed()
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
            .apply {
                // Tùy chỉnh màu nút
                getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                    ContextCompat.getColor(context, R.color.red)
                )
                getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                    ContextCompat.getColor(context, R.color.aqua)
                )
            }
    }

}