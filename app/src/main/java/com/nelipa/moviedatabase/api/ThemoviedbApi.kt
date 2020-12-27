package com.nelipa.moviedatabase.api

import com.nelipa.moviedatabase.models.movie.MovieDB
import com.nelipa.moviedatabase.models.ThemoviedbResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ThemoviedbApi {

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrending(
        @Path("media_type") mediaType: String = "movie",
        @Path("time_window") timeWindow: String = "week",
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): ThemoviedbResponse<MovieDB>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("language") language: String? = null,
        @Query("region") region: String? = null,
        @Query("year") year: Int? = null,
        @Query("primary_release_year") primaryReleaseYear: Int? = null,
        @Query("include_adult") includeAdult: Boolean? = null
    ): ThemoviedbResponse<MovieDB>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("append_to_response") appendToResponse: String? = null
    ): MovieDB
}