package nhn.ntech.cinehub.data.source.remote

import nhn.ntech.cinehub.data.constant.ConstantApi
import nhn.ntech.cinehub.data.model.details.DetailMovieResponse
import nhn.ntech.cinehub.data.model.genre.GenreResponse
import nhn.ntech.cinehub.data.model.movies.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET(ConstantApi.POPULAR_MOVIE)
    suspend fun getPopularMovies(): Response<MovieResponse>

    @GET(ConstantApi.TOP_RATED_MOVIE)
    suspend fun getTopRatedMovies(): Response<MovieResponse>

    @GET(ConstantApi.GENRES_MOVIE)
    suspend fun getGenres(): Response<GenreResponse>

    @GET(ConstantApi.DETAIL)
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: Int
    ): Response<DetailMovieResponse>

    @GET(ConstantApi.RECOMMENDATIONS)
    suspend fun  getRecommendations(
        @Path("movie_id") movieId: Int
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

}