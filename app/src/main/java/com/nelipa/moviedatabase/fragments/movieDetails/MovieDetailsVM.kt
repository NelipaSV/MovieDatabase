package com.nelipa.moviedatabase.fragments.movieDetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nelipa.moviedatabase.api.ThemoviedbApi
import com.nelipa.moviedatabase.base.BaseVM
import com.nelipa.moviedatabase.constants.Constants
import com.nelipa.moviedatabase.models.movie.MovieDB
import com.nelipa.moviedatabase.utility.DefaultDispatcherProvider
import com.nelipa.moviedatabase.utility.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsVM @ViewModelInject constructor(
    private val themoviedbApi: ThemoviedbApi,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : BaseVM() {

    private var movieLiveData = MutableLiveData<MovieDB>()

    fun getMovieDetails(): LiveData<MovieDB> = movieLiveData

    fun loadMovieDetails(movieID: Int) {

        viewModelScope.launch(dispatchers.io()) {
            try {
                themoviedbApi.getMovieDetails(
                    movieId = movieID,
                    apiKey = Constants.MOVIE_API_KEY
                ).also { movie ->
                    movieLiveData.postValue(movie)
                }
            } catch (e: Exception) {
                handleResponseError(responseHandler.handleException(e))
                e.printStackTrace()
            }
        }
    }
}