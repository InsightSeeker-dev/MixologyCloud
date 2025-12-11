package com.example.mixologycloud.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class RemoteConfigData(
    val primaryColor: String,
    val welcomeMessage: String,
    val featureEnabled: Boolean
)

class FirebaseRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
    private val messaging: FirebaseMessaging
) {
    
    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        
        remoteConfig.setDefaultsAsync(
            mapOf(
                KEY_PRIMARY_COLOR to "#FF6200EE",
                KEY_WELCOME_MESSAGE to "Bienvenue sur MixologyCloud",
                KEY_FEATURE_ENABLED to true
            )
        )
    }
    
    suspend fun fetchRemoteConfig(): Result<Unit> {
        return try {
            remoteConfig.fetchAndActivate().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getRemoteConfigData(): RemoteConfigData {
        return RemoteConfigData(
            primaryColor = remoteConfig.getString(KEY_PRIMARY_COLOR),
            welcomeMessage = remoteConfig.getString(KEY_WELCOME_MESSAGE),
            featureEnabled = remoteConfig.getBoolean(KEY_FEATURE_ENABLED)
        )
    }
    
    fun observeRemoteConfigChanges(): Flow<RemoteConfigData> = callbackFlow {
        trySend(getRemoteConfigData())
        
        awaitClose { }
    }
    
    suspend fun subscribeToTopic(topic: String): Result<Unit> {
        return try {
            messaging.subscribeToTopic(topic).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFcmToken(): Result<String> {
        return try {
            val token = messaging.token.await()
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        private const val KEY_PRIMARY_COLOR = "primary_color"
        private const val KEY_WELCOME_MESSAGE = "welcome_message"
        private const val KEY_FEATURE_ENABLED = "feature_enabled"
    }
}
