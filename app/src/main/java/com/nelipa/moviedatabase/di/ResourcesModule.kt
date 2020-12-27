package com.nelipa.moviedatabase.di

import android.app.Application
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ResourcesModule {

    @Provides
    fun getAppResources(application: Application): Resources {
        return application.resources
    }
}