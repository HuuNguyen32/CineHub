package nhn.ntech.cinehub.data.repository

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
}