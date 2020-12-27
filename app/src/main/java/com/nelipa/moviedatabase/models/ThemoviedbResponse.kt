package com.nelipa.moviedatabase.models

import com.google.gson.annotations.SerializedName

data class ThemoviedbResponse<T>(
    var page: Int?,
    var results: List<T>?,
    @SerializedName("total_pages") var totalPages: Int?,
    @SerializedName("total_results") var totalResults: Int?
)