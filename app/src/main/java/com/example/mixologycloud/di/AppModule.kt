package com.example.mixologycloud.di

import android.content.Context
import androidx.room.Room
import com.example.mixologycloud.data.local.CocktailDao
import com.example.mixologycloud.data.local.MixologyDatabase
import com.example.mixologycloud.data.remote.CocktailApiService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideMixologyDatabase(
        @ApplicationContext context: Context
    ): MixologyDatabase {
        return Room.databaseBuilder(
            context,
            MixologyDatabase::class.java,
            "mixology_database"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideCocktailDao(database: MixologyDatabase): CocktailDao {
        return database.cocktailDao()
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideCocktailApiService(okHttpClient: OkHttpClient): CocktailApiService {
        return Retrofit.Builder()
            .baseUrl(CocktailApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CocktailApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }
    
    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}
