package nhn.ntech.cinehub.data.model.movies

import com.google.gson.annotations.SerializedName
import nhn.ntech.cinehub.data.model.movies.Result

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)