package com.example.mixologycloud.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixologycloud.data.mapper.toUi
import com.example.mixologycloud.data.repository.CocktailRepository
import com.example.mixologycloud.ui.model.CocktailUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CocktailListUiState(
    val cocktails: List<CocktailUi> = emptyList(),
    val groupedCocktails: Map<String, List<CocktailUi>> = emptyMap(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CocktailListViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CocktailListUiState())
    val uiState: StateFlow<CocktailListUiState> = _uiState.asStateFlow()
    
    init {
        loadCocktails()
    }
    
    private fun loadCocktails() {
        viewModelScope.launch {
            combine(
                cocktailRepository.getAllCocktails(),
                cocktailRepository.getCocktailCount()
            ) { cocktails, count ->
                val cocktailsUi = cocktails.map { it.toUi() }
                val grouped = cocktailsUi.groupBy { it.insertionDay }
                
                CocktailListUiState(
                    cocktails = cocktailsUi,
                    groupedCocktails = grouped,
                    totalCount = count,
                    isLoading = false
                )
            }.catch { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    fun addRandomCocktail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            cocktailRepository.addRandomCocktail()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
        }
    }
    
    fun deleteAllCocktails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                cocktailRepository.deleteAllCocktails()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
