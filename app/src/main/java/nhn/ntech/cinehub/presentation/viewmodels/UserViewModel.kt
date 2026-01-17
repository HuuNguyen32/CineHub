package nhn.ntech.cinehub.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nhn.ntech.cinehub.data.model.details.DetailMovieResponse
import nhn.ntech.cinehub.data.model.movies.Result
import nhn.ntech.cinehub.data.repository.MovieRepository
import nhn.ntech.cinehub.data.repository.UserRepository
import nhn.ntech.cinehub.domain.UserProfile
import nhn.ntech.cinehub.utils.ConvertFactory.toResult
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> = _userProfile

    fun loadUserProfile(uid: String) {
        userRepository.getUserProfile(uid) { user ->
            _userProfile.value = user
        }
    }

    val currentUid = auth.currentUser?.uid


    private val _favoriteMovies = MutableLiveData<List<Result>>()
    val favoriteMovies: LiveData<List<Result>> = _favoriteMovies

    fun observeFavorites(userId: String) {
        userRepository.observeFavorites(userId) { ids ->
            // Khi có danh sách ID mới từ Firebase, gọi API để lấy chi tiết phim
            loadMoviesByIds(ids)
        }
    }

    // Hàm gọi API song song để lấy chi tiết phim
    private fun loadMoviesByIds(ids: List<String>) {
        viewModelScope.launch {
            val intIds = ids.mapNotNull { it.toIntOrNull() }
            val movies = movieRepository.getMoviesByIds(intIds)
            val results = movies.map { it.toResult() }
            _favoriteMovies.postValue(results)
        }
    }



    fun addFavorite(userId: String, movieId: Int) {
        userRepository.addFavorite(userId, movieId.toString())
    }

    fun removeFavorite(userId: String, movieId: Int) {
        userRepository.removeFavorite(userId, movieId.toString())
    }


}