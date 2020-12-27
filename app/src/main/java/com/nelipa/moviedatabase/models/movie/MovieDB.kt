package com.nelipa.moviedatabase.models.movie

import com.google.gson.annotations.SerializedName

data class MovieDB(
    var id: Int?,
    var title: String?,
    var overview: String?,
    @SerializedName("poster_path") var posterPath: String?,
    @SerializedName("release_date") var releaseDate: String?,
    @SerializedName("genres") var genres: List<MovieGenre>?,
    @SerializedName("production_companies") var productionCompanies: List<MovieProductCompany>?
)