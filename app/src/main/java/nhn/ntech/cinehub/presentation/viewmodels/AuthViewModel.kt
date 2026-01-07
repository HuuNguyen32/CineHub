package nhn.ntech.cinehub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import nhn.ntech.cinehub.callbacks.AuthCallBack
import nhn.ntech.cinehub.domain.UserProfile
import nhn.ntech.cinehub.utils.CheckValids
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val realtimeDb: FirebaseDatabase,
    private val user: FirebaseUser?
) : ViewModel() {

    fun login(email: String, password: String, callBack: AuthCallBack){
        if (email.isEmpty() || password.isEmpty()){
            callBack.onFailed("Vui lòng nhập đầy đủ thông tin")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    if (user != null){
                        callBack.onSuccess()
                    }
                    else{
                        callBack.onFailed("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập")
                    }
                } else {
                    callBack.onFailed("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập")
                }
            }
    }


    fun register(email: String, birthDay: String, password: String, confirmPassword: String, callBack: AuthCallBack){
        if (email.isEmpty() || birthDay.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            callBack.onFailed("Vui lòng nhập đầy đủ thông tin")
            return
        }
        if (password != confirmPassword){
            callBack.onFailed("Mật khẩu không khớp")
            return
        }
        if (!CheckValids.isValidEmail(email)){
            callBack.onFailed("Email không hợp lệ")
            return
        }
        if (!CheckValids.isValidPassword(password)){
            callBack.onFailed("Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ cái và số")
            return
        }
        if (!CheckValids.isValidBirthday(birthDay)){
            callBack.onFailed("Ngày sinh không hợp lệ")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    callBack.onSuccess()
                }else{
                    callBack.onFailed("Đăng ký thất bại. Vui lòng kiểm tra lại thông tin đăng ký")
                }
            }

    }


    fun saveUserProfile(uid: String, email: String, birthDay: String, profileName: String, photoUrl: String?, callBack: AuthCallBack){
        val user = UserProfile(uid, email, birthDay, profileName, photoUrl)
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

    fun logout(){
        if (user != null){
            auth.signOut()
        }
    }

}