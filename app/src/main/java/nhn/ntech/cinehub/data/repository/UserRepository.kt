package nhn.ntech.cinehub.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nhn.ntech.cinehub.domain.UserProfile
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val cloudinary: Cloudinary,
    private val context: Context,
    private val db: FirebaseDatabase
) {
    suspend fun uploadImage(uri: Uri): Result<String> = runCatching {
        // Tạo file tạm trong cache
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)

        // Copy dữ liệu từ Uri sang file tạm
        context.contentResolver.openInputStream(uri).use { input ->
            tempFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        // Upload ảnh lên Cloudinary
        val result = cloudinary.uploader().upload(
            tempFile,
            ObjectUtils.asMap("folder", "cinehub")
        )

        // Trả về URL ảnh HTTPS
        result["secure_url"] as String

    }

    // Lấy dữ liệu user theo uid và trả về object UserProfile
    fun getUserProfile(uid: String, callback: (UserProfile?) -> Unit) {
        val ref = db.getReference("users").child(uid)
        ref.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserProfile::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("UserRepository", "Lỗi lấy dữ liệu: ${e.message}")
                callback(null)
            }
    }

    // Thêm phim vào favorites
    fun addFavorite(userId: String, movieId: String) {
        val ref = db.getReference("users").child(userId).child("favorites")
        ref.child(movieId).setValue(true)
    }

    // Xóa phim khỏi favorites
    fun removeFavorite(userId: String, movieId: String) {
        val ref = db.getReference("users").child(userId).child("favorites")
        ref.child(movieId).removeValue()
    }

    // Lấy danh sách favorites realtime
    fun getFavorites(userId: String, callback: (List<String>) -> Unit) {
        val ref = db.getReference("users").child(userId).child("favorites")
        ref.get()
            .addOnSuccessListener { snapshot ->
                val favorites = mutableListOf<String>()
                for (child in snapshot.children) {
                    child.key?.let { favorites.add(it) }
                }
                callback(favorites)
            }
            .addOnFailureListener { e ->
                Log.e("UserRepository", "Lỗi lấy favorites: ${e.message}")
                callback(emptyList())
            }
    }

    // Lắng nghe realtime favorites
    fun observeFavorites(userId: String, listener: (List<String>) -> Unit) {
        val ref = db.getReference("users").child(userId).child("favorites")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favorites = snapshot.children.mapNotNull { it.key }
                listener(favorites)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("UserRepository", "Firebase error: ${error.message}")
            }
        })
    }




}