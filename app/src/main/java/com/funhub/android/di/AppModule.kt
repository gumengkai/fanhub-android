package com.funhub.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.funhub.android.data.local.SettingsManager
import com.funhub.android.data.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideSettingsManager(dataStore: DataStore<Preferences>): SettingsManager {
        return SettingsManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideMediaRepository(
        api: com.funhub.android.data.api.FunHubApi,
        settingsManager: SettingsManager
    ): MediaRepository {
        return MediaRepository(api, settingsManager)
    }
}
