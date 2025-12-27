package nhn.ntech.cinehub.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nhn.ntech.cinehub.data.model.MovieResponse
import nhn.ntech.cinehub.data.repository.MovieRepository
import nhn.ntech.cinehub.data.source.network.ApiResponse
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _popularMovies = MutableLiveData<ApiResponse<MovieResponse>>()
    private val _topRatedMovies = MutableLiveData<ApiResponse<MovieResponse>>()

    val popularMovies: LiveData<ApiResponse<MovieResponse>> = _popularMovies
    val topRatedMovies: LiveData<ApiResponse<MovieResponse>> = _topRatedMovies


    fun getPopularMovies() {
        _popularMovies.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getAllPopularMovies()
            _popularMovies.postValue(response)
        }
    }

    fun getTopRatedMovies(){
        _topRatedMovies.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getAllTopRatedMovies()
            _topRatedMovies.postValue(response)
        }
    }
}