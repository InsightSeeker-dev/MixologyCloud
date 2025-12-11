package com.example.mixologycloud.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixologycloud.data.repository.FirebaseRepository
import com.example.mixologycloud.data.repository.RemoteConfigData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FirebaseUiState(
    val remoteConfig: RemoteConfigData? = null,
    val fcmToken: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSubscribed: Boolean = false,
    val successMessage: String? = null
)

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FirebaseUiState())
    val uiState: StateFlow<FirebaseUiState> = _uiState.asStateFlow()
    
    init {
        loadRemoteConfig()
        loadFcmToken()
    }
    
    private fun loadRemoteConfig() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            firebaseRepository.fetchRemoteConfig()
                .onSuccess {
                    val config = firebaseRepository.getRemoteConfigData()
                    _uiState.value = _uiState.value.copy(
                        remoteConfig = config,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    val config = firebaseRepository.getRemoteConfigData()
                    _uiState.value = _uiState.value.copy(
                        remoteConfig = config,
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }
    
    private fun loadFcmToken() {
        viewModelScope.launch {
            firebaseRepository.getFcmToken()
                .onSuccess { token ->
                    _uiState.value = _uiState.value.copy(fcmToken = token)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message
                    )
                }
        }
    }
    
    fun subscribeToNotifications() {
        viewModelScope.launch {
            firebaseRepository.subscribeToTopic("mixology_updates")
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isSubscribed = true,
                        successMessage = "Abonné aux notifications avec succès!"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(error = exception.message)
                }
        }
    }
    
    fun refreshRemoteConfig() {
        loadRemoteConfig()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}
