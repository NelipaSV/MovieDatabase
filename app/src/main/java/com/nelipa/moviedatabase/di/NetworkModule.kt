package com.nelipa.moviedatabase.di

import com.nelipa.moviedatabase.api.ThemoviedbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ActivityComponent::class)
class NetworkModule {

    private val BASE_URL = "https://api.themoviedb.org/3/"

    @Provides
    fun createRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    @Provides
    fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun createThemoviedbService(): ThemoviedbApi {
        return createRetrofitClient().create(ThemoviedbApi::class.java)
    }
}