package com.nelipa.moviedatabase.utility

import com.nelipa.moviedatabase.models.movie.MovieDB
import javax.inject.Inject

class ConvertingUtils @Inject constructor() {

    fun getGenresList(movie: MovieDB): String? {
        movie.genres?.let { genresList ->
            val space = ", "
            val stringBuilder = StringBuilder()
            genresList.forEach {
                stringBuilder.append(it.name)
                if (genresList.indexOf(it) != genresList.size-1) {
                    stringBuilder.append(space)
                }
            }
            return stringBuilder.toString()
        } ?: return null
    }

    fun getProductionsList(movie: MovieDB): String? {
        movie.productionCompanies?.let { companiesList ->
            val space = ", "
            val stringBuilder = StringBuilder()
            companiesList.forEach {
                stringBuilder.append(it.name)
                if (companiesList.indexOf(it) != companiesList.size-1) {
                    stringBuilder.append(space)
                }
            }
            return stringBuilder.toString()
        } ?: return null
    }
}