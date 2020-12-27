package com.nelipa.moviedatabase.fragments.moviesList

import com.nelipa.moviedatabase.models.movie.MovieDB
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.nelipa.moviedatabase.api.ThemoviedbApi
import com.nelipa.moviedatabase.base.BaseVM
import com.nelipa.moviedatabase.constants.Constants.MOVIE_API_KEY
import kotlinx.coroutines.*

class MovieListVM @ViewModelInject constructor(
    private val themoviedbApi: ThemoviedbApi
) : BaseVM() {

    private var lastSearchedQuery: String = ""
    private var loadedListMoviesLiveData = MutableLiveData<Pair<List<MovieDB>, Boolean>>()
    private var loadedListMovies = ArrayList<MovieDB>()

    private var page: Int = 1
    private var lastPage: Int = 1

    private var isTrendingMovies = true

    fun getCurrentPage() = page
    fun nextPage() {
        page += 1
    }

    fun setStartingPage() {
        page = 1
    }

    fun getLastPage() = lastPage

    fun getListMovies(): LiveData<Pair<List<MovieDB>, Boolean>> = loadedListMoviesLiveData
    private fun displayMovies(isTrendingMovies: Boolean) {
        loadedListMoviesLiveData.postValue(Pair(loadedListMovies, isTrendingMovies))
    }

    private fun clearMovies() {
        loadedListMovies.clear()
    }

    fun loadPageTrendingMovies() {
        isTrendingMovies = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                themoviedbApi.getTrending(
                    page = page,
                    apiKey = MOVIE_API_KEY
                ).also { response ->
                    if (response.page == 1) {
                        clearMovies()
                        response.totalPages?.let {
                            lastPage = it
                        }
                    }
                    response.results?.let { moviesPage ->
                        loadedListMovies.addAll(moviesPage)
                        displayMovies(isTrendingMovies)
                    }
                }
            } catch (e: Exception) {
                handleResponseError(responseHandler.handleException(e))
                e.printStackTrace()
            }
        }

    }

    fun loadPageSearchMovies(searchQuery: String = lastSearchedQuery) {
        lastSearchedQuery = searchQuery
        isTrendingMovies = false

        viewModelScope.launch(Dispatchers.IO) {
            try {
                themoviedbApi.searchMovie(
                    page = page,
                    apiKey = MOVIE_API_KEY,
                    query = searchQuery
                ).also { response ->
                    if (response.page == 1) {
                        clearMovies()
                        response.totalPages?.let {
                            lastPage = it
                        }
                    }
                    response.results?.let { moviesPage ->
                        loadedListMovies.addAll(moviesPage)
                        displayMovies(isTrendingMovies)
                    }
                }
            } catch (e: Exception) {
                handleResponseError(responseHandler.handleException(e))
                e.printStackTrace()
            }
        }
    }

    fun loadNextPage() {
        if (isTrendingMovies) {
            loadPageTrendingMovies()
        } else {
            loadPageSearchMovies()
        }
    }
}