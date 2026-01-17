package nhn.ntech.cinehub.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import nhn.ntech.cinehub.data.model.details.DetailMovieResponse
import nhn.ntech.cinehub.data.source.network.GenericApiResponse
import nhn.ntech.cinehub.data.source.remote.ApiServices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiServices: ApiServices
) : GenericApiResponse(){
    suspend fun getAllPopularMovies() = apiCall {
        apiServices.getPopularMovies()
    }

    suspend fun getAllTopRatedMovies() = apiCall {
        apiServices.getTopRatedMovies()
    }

    suspend fun getAllGenres() = apiCall {
        apiServices.getGenres()
    }

    suspend fun getDetailMovie(movieId: Int) = apiCall {
        apiServices.getDetailMovie(movieId)
    }

    suspend fun getRecommendations(movieId: Int) = apiCall {
        apiServices.getRecommendations(movieId)
    }

    suspend fun searchMovies(query: String) = apiCall {
        apiServices.searchMovies(query)
    }

    suspend fun getMoviesByIds(ids: List<Int>): List<DetailMovieResponse> = coroutineScope {
        val deferred = ids.map { id ->
            async {
                try {
                    val response = apiServices.getDetailMovie(id)
                    if (response.isSuccessful) response.body() else null
                } catch (e: Exception) {
                    null // nếu lỗi thì trả về null
                }
            }
        }

        // Chờ tất cả hoàn thành và lọc bỏ null
        deferred.awaitAll().filterNotNull()
    }

}