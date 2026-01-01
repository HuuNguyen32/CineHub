package nhn.ntech.cinehub.utils

import nhn.ntech.cinehub.data.model.genre.Genre

object GenreMapper {
    fun convertGenreIDToGenreName(genreIDs: List<Int>, genres: List<Genre>): List<String> {
        return genres.filter { it -> genreIDs.contains(it.id) }.map { it.name }.toList()
    }
}