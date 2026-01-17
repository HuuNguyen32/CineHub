package nhn.ntech.cinehub.utils

import nhn.ntech.cinehub.data.model.details.DetailMovieResponse
import nhn.ntech.cinehub.data.model.movies.Result

object ConvertFactory {
    fun DetailMovieResponse.toResult(): Result {
        return Result(
            adult = this.adult,
            backdropPath = this.backdropPath,
            genreIds = this.genres.map { it.id }, // lấy id từ list Genre
            id = this.id,
            originalLanguage = this.originalLanguage,
            originalTitle = this.originalTitle,
            overview = this.overview,
            popularity = this.popularity,
            posterPath = this.posterPath,
            releaseDate = this.releaseDate,
            title = this.title,
            video = this.video,
            voteAverage = this.voteAverage,
            voteCount = this.voteCount,
            genreNames = this.genres.map { it.name } // lấy tên genre để hiển thị
        )
    }

}