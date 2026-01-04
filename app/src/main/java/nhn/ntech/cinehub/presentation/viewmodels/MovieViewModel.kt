package nhn.ntech.cinehub.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nhn.ntech.cinehub.data.model.details.DetailMovieResponse
import nhn.ntech.cinehub.data.model.genre.Genre
import nhn.ntech.cinehub.data.model.genre.GenreResponse
import nhn.ntech.cinehub.data.model.movies.MovieResponse
import nhn.ntech.cinehub.data.model.movies.Result
import nhn.ntech.cinehub.data.repository.MovieRepository
import nhn.ntech.cinehub.data.source.network.ApiResponse
import java.util.Collections
import javax.inject.Inject
import kotlin.collections.map


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _popularMovies = MutableLiveData<ApiResponse<MovieResponse>>()
    private val _genres = MutableLiveData<ApiResponse<GenreResponse>>()
    private val _topRatedMovies = MutableLiveData<ApiResponse<MovieResponse>>()
    private val _detailMovie = MutableLiveData<ApiResponse<DetailMovieResponse>>()
    private val _recommendations = MutableLiveData<ApiResponse<MovieResponse>>()


    val popularMovies: LiveData<ApiResponse<MovieResponse>> = _popularMovies
    val genres: LiveData<ApiResponse<GenreResponse>> = _genres
    val topRatedMovies: LiveData<ApiResponse<MovieResponse>> = _topRatedMovies
    val detailMovie: LiveData<ApiResponse<DetailMovieResponse>> = _detailMovie
    val recommendations: LiveData<ApiResponse<MovieResponse>> = _recommendations


    fun getPopularMovies() {
        if (_popularMovies.value == null){
            _popularMovies.value = ApiResponse.Loading()
            viewModelScope.launch(Dispatchers.IO) {
                val response = movieRepository.getAllPopularMovies()
                _popularMovies.postValue(response)
            }
        }
    }

    fun getTopRatedMovies(){
        if (_topRatedMovies.value == null){
            _topRatedMovies.value = ApiResponse.Loading()
            viewModelScope.launch(Dispatchers.IO) {
                val response = movieRepository.getAllTopRatedMovies()
                _topRatedMovies.postValue(response)
            }
        }
    }

    fun getGenres(){
        if (_genres.value == null){
            _genres.value = ApiResponse.Loading()
            viewModelScope.launch(Dispatchers.IO) {
                val response = movieRepository.getAllGenres()
                _genres.postValue(response)
            }
        }
    }

    fun getDetailMovie(movieId: Int) {
        _detailMovie.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getDetailMovie(movieId)
            _detailMovie.postValue(response)
        }
    }

    fun getRecommendations(movieId: Int) {
        _recommendations.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getRecommendations(movieId)
            _recommendations.postValue(response)
        }
    }

    fun convertGenreIDToGenreName(genreIDs: List<Int>, genres: List<Genre>): List<String> {
        val genreMap = genres.associateBy({ it.id }, { it.name })
        return genreIDs.mapNotNull { genreMap[it] }
    }

    fun convertFactory(list: List<Result>, genreList: List<Genre>): List<Result>{
        return list.map { result ->
            val genreNameList = if (genreList.isNotEmpty()){
                convertGenreIDToGenreName(result.genreIds, genreList)
            } else Collections.emptyList()
            result.copy(genreNames = genreNameList)
        }
    }

}