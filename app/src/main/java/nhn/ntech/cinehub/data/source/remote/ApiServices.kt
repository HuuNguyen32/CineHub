package nhn.ntech.cinehub.data.source.remote

import nhn.ntech.cinehub.data.constant.ConstantApi
import nhn.ntech.cinehub.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {

    @GET(ConstantApi.POPULAR_MOVIE)
    suspend fun getPopularMovies(): Response<MovieResponse>

    @GET(ConstantApi.TOP_RATED_MOVIE)
    suspend fun getTopRatedMovies(): Response<MovieResponse>

}