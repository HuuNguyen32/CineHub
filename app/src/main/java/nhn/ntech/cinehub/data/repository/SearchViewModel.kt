package nhn.ntech.cinehub.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nhn.ntech.cinehub.data.model.movies.Result
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel(){

    private val _movies = MutableLiveData<List<Result>>()
    val movies: LiveData<List<Result>> = _movies

    private val _lastSearch = MutableLiveData<List<String>>(emptyList())
    val lastSearch: LiveData<List<String>> = _lastSearch

    /** Chỉ gọi API để lấy kết quả tìm kiếm */
    fun searchMovies(query: String) {
        viewModelScope.launch {
            val results = movieRepository.searchMovies(query)
            _movies.value = results.data?.results ?: emptyList()
        }
    }

    /** Lưu lịch sử tìm kiếm – chỉ gọi khi Enter/Search */
    fun saveSearchHistory(query: String) {
        val history = _lastSearch.value?.toMutableList() ?: mutableListOf()
        if (query.isNotBlank() && !history.contains(query)) {
            history.add(0, query)
            _lastSearch.value = history.take(5) // chỉ giữ 10 từ khóa gần nhất
        }
    }

    fun clearHistory() {
        _lastSearch.value = emptyList()
    }

    fun clearMovies() {
        _movies.value = emptyList()
    }


}