package com.nelipa.moviedatabase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nelipa.moviedatabase.api.ThemoviedbApi
import com.nelipa.moviedatabase.constants.Constants
import com.nelipa.moviedatabase.fragments.movieDetails.MovieDetailsVM
import com.nelipa.moviedatabase.models.movie.MovieDB
import com.nelipa.moviedatabase.utility.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class MovieListVMTest {
    private val apiMock = mock<ThemoviedbApi>()

    private lateinit var vm: MovieDetailsVM

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val testDispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = TestCoroutineDispatcher()
        override fun io(): CoroutineDispatcher = TestCoroutineDispatcher()
        override fun main(): CoroutineDispatcher = TestCoroutineDispatcher()
        override fun unconfined(): CoroutineDispatcher = TestCoroutineDispatcher()
    }

    @Captor
    private lateinit var stringCaptor: ArgumentCaptor<String>
    @Captor
    private lateinit var intCaptor: ArgumentCaptor<Int>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        vm = init()
    }


    @Test(timeout = 5_000)
    fun `test loadMovieDetails - success flow`() = runBlockingTest {
        val movieId = 1
        val apiResponse = MovieDB(
            id = movieId,
            title = "",
            overview = "",
            posterPath = "",
            releaseDate = "",
            genres = emptyList(),
            productionCompanies = mock(),
        )

        doReturn(apiResponse).`when`(apiMock).getMovieDetails(
            movieId = movieId ,
            apiKey = Constants.MOVIE_API_KEY,
            language = null,
            appendToResponse = null
        )

        vm.loadMovieDetails(movieId)

        verify(vm, times(0)).handleResponseError(anyOrNull())

        assertEquals(movieId, vm.getMovieDetails().value!!.id)
    }

    @Test(timeout = 5_000, expected = Exception::class)
    fun `test loadMovieDetails - fail flow`() = runBlockingTest {
        val movieId = 1

        doThrow(Exception()).`when`(apiMock).getMovieDetails(
            movieId = movieId ,
            apiKey = Constants.MOVIE_API_KEY,
            language = null,
            appendToResponse = null
        )

        vm.loadMovieDetails(movieId)

        verify(apiMock, times(1)).getMovieDetails(
            movieId = movieId ,
            apiKey = Constants.MOVIE_API_KEY,
            language = null,
            appendToResponse = null
        )

        verify(vm, times(1)).handleResponseError(anyOrNull())

        assertEquals("Something went wrong", vm.getLoadError().value!!.getContentIfNotHandled())
    }

    @Test(timeout = 5_000, expected = Exception::class)
    fun `test loadMovieDetails - check if proper parameters`() = runBlockingTest {
        val movieId = 1

        doThrow(Exception()).`when`(apiMock).getMovieDetails(
            movieId = movieId ,
            apiKey = Constants.MOVIE_API_KEY,
            language = null,
            appendToResponse = null
        )

        vm.loadMovieDetails(movieId)

        verify(apiMock, times(1)).getMovieDetails(
            movieId = movieId ,
            apiKey = capture(stringCaptor),
            language = null,
            appendToResponse = null
        )

        assertEquals("d5eefb3dc46ebdcb4226b18c9112dad6", stringCaptor.value)
    }

    @Test(timeout = 5_000, expected = Exception::class)
    fun `test loadMovieDetails - check if proper id`() = runBlockingTest {
        val movieId = 1

        doThrow(Exception()).`when`(apiMock).getMovieDetails(
            movieId = movieId ,
            apiKey = Constants.MOVIE_API_KEY,
            language = null,
            appendToResponse = null
        )

        vm.loadMovieDetails(movieId)

        verify(apiMock, times(1)).getMovieDetails(
            movieId = capture(intCaptor) ,
            apiKey = Constants.MOVIE_API_KEY,
            language = null,
            appendToResponse = null
        )

        assertEquals(movieId, intCaptor.value)
    }

    private fun init(): MovieDetailsVM = spy(
        MovieDetailsVM(
            apiMock,
            testDispatcherProvider
        )
    )
}