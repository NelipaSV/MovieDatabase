package com.nelipa.moviedatabase.models.movie

import com.google.gson.annotations.SerializedName

data class MovieProductCompany(
    var id: Int?,
    var name: String?,
    @SerializedName("logo_path") var logoPath: String?,
    @SerializedName("origin_country") var originCountry: String?
)
