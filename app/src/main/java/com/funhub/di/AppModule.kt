package com.funhub.di

import android.content.Context
import androidx.room.Room
import com.funhub.data.local.database.AppDatabase
import com.funhub.data.remote.api.FunHubApi
import com.funhub.data.remote.interceptor.AuthInterceptor
import com.funhub.data.repository.ServerAddressProvider
import com.funhub.data.repository.ServerAddressProviderImpl
import com.funhub.domain.repository.ImageRepository
import com.funhub.domain.repository.SettingsRepository
import com.funhub.domain.repository.VideoRepository
import com.funhub.data.repository.ImageRepositoryImpl
import com.funhub.data.repository.SettingsRepositoryImpl
import com.funhub.data.repository.VideoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        impl: VideoRepositoryImpl
    ): VideoRepository
    
    @Binds
    @Singleton
    abstract fun bindImageRepository(
        impl: ImageRepositoryImpl
    ): ImageRepository
    
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
    
    @Binds
    @Singleton
    abstract fun bindServerAddressProvider(
        impl: ServerAddressProviderImpl
    ): ServerAddressProvider
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "funhub_database"
        ).build()
    }
    
    @Provides
    fun provideVideoDao(database: AppDatabase) = database.videoDao()
    
    @Provides
    fun provideImageDao(database: AppDatabase) = database.imageDao()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.100:5000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideFunHubApi(retrofit: Retrofit): FunHubApi {
        return retrofit.create(FunHubApi::class.java)
    }
}
