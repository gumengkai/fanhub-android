package com.fanhub.android.di

import android.content.Context
import com.fanhub.android.data.local.SettingsRepository
import com.fanhub.android.data.remote.ApiService
import com.fanhub.android.data.repository.FanHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context
    ): SettingsRepository {
        return SettingsRepository(context)
    }
    
    @Provides
    @Singleton
    fun provideFanHubRepository(
        apiService: ApiService
    ): FanHubRepository {
        return FanHubRepository(apiService)
    }
}
