package nhn.ntech.cinehub.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nhn.ntech.cinehub.callbacks.AuthCallBack
import nhn.ntech.cinehub.data.repository.UserRepository
import nhn.ntech.cinehub.domain.UserProfile
import nhn.ntech.cinehub.utils.CheckValids
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val realtimeDb: FirebaseDatabase,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val TAG = "AuthViewModel"

    fun login(email: String, password: String, callBack: AuthCallBack) {
        if (email.isEmpty() || password.isEmpty()) {
            callBack.onFailed("Vui lòng nhập đầy đủ thông tin")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser != null) {
                        callBack.onSuccess()
                    } else {
                        callBack.onFailed("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập")
                    }
                } else {
                    callBack.onFailed("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập")
                }
            }
    }


    fun register(
        email: String,
        birthDay: String,
        password: String,
        confirmPassword: String,
        callBack: AuthCallBack,
    ) {
        if (email.isEmpty() || birthDay.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            callBack.onFailed("Vui lòng nhập đầy đủ thông tin")
            return
        }
        if (password != confirmPassword) {
            callBack.onFailed("Mật khẩu không khớp")
            return
        }
        if (!CheckValids.isValidEmail(email)) {
            callBack.onFailed("Email không hợp lệ")
            return
        }
        if (!CheckValids.isValidPassword(password)) {
            callBack.onFailed("Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ cái và số")
            return
        }
        if (!CheckValids.isValidBirthday(birthDay)) {
            callBack.onFailed("Ngày sinh không hợp lệ")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callBack.onSuccess()
                } else {
                    callBack.onFailed("Đăng ký thất bại. Vui lòng kiểm tra lại thông tin đăng ký")
                }
            }

    }


    fun resetPassword(email: String, callBack: AuthCallBack) {
        if (email.isEmpty()) {
            callBack.onFailed("Vui lòng nhập email")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callBack.onSuccess()
                    Log.d(TAG, "resetPassword: Thành công")
                } else {
                    callBack.onFailed("Gửi email thất bại. Vui lòng kiểm tra lại thông tin")
                    Log.d(TAG, "resetPassword: ${task.exception?.message}")
                }
            }
    }

    fun saveUserProfile(
        email: String,
        birthDay: String,
        profileName: String,
        photoUrl: String?,
        createAt: Long,
        callBack: AuthCallBack,
    ) {
        if (email == "" || birthDay == "" || profileName == "" || createAt == 0L) {
            callBack.onFailed("Vui lòng nhập đầy đủ thông tin")
            return
        }
        if (profileName.length < 3) {
            callBack.onFailed("Tên người dùng phải có ít nhất 3 ký tự")
            return
        }
        val uid = auth.currentUser?.uid ?: "NAN"
        val user = UserProfile(uid, email, birthDay, profileName, photoUrl, createAt)
        realtimeDb.getReference("users")
            .child(uid)
            .setValue(user)
            .addOnSuccessListener {
                callBack.onSuccess()
            }
            .addOnFailureListener {
                callBack.onFailed("Lưu thông tin người dùng thất bại")
            }
    }

    fun updateUserProfile(
        profileName: String,
        birthDay: String,
        photoUrl: String?,
        callBack: AuthCallBack,
    ) {
        val uid = auth.currentUser?.uid ?: run {
            callBack.onFailed("User not logged in")
            return
        }

        if (!CheckValids.isValidBirthday(birthDay)) {
            callBack.onFailed("Ngày sinh không hợp lệ")
            return
        }

        if (profileName.length < 3) {
            callBack.onFailed("Tên người dùng phải có ít nhất 3 ký tự")
            return
        }

        if (birthDay == "") {
            callBack.onFailed("Vui lòng nhập đầy đủ thông tin")
            return
        }

        val updates = mutableMapOf<String, Any>()
        if (profileName.isNotEmpty()) updates["profileName"] = profileName
        if (birthDay.isNotEmpty()) updates["birthDay"] = birthDay
        if (photoUrl != null) updates["photoUrl"] = photoUrl

        realtimeDb.getReference("users")
            .child(uid)
            .updateChildren(updates)
            .addOnSuccessListener {
                callBack.onSuccess()
            }
            .addOnFailureListener {
                callBack.onFailed("Update profile failed")
            }
    }

    fun logout() {
        if (auth.currentUser != null) {
            auth.signOut()
        }
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null


    private val _uploadResult = MutableLiveData<Result<String>>()
    val uploadResult: LiveData<Result<String>> = _uploadResult
    var currentPhotoUrl: String? = null

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                userRepository.uploadImage(uri)
            }
            _uploadResult.postValue(result)
        }
    }

}