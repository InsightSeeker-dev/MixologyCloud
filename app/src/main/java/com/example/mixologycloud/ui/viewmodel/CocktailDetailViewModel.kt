package com.example.mixologycloud.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixologycloud.data.mapper.toUi
import com.example.mixologycloud.data.repository.CocktailRepository
import com.example.mixologycloud.ui.model.CocktailUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CocktailDetailUiState(
    val cocktail: CocktailUi? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CocktailDetailViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val cocktailId: String = checkNotNull(savedStateHandle["cocktailId"])
    
    private val _uiState = MutableStateFlow(CocktailDetailUiState())
    val uiState: StateFlow<CocktailDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadCocktailDetail()
    }
    
    private fun loadCocktailDetail() {
        viewModelScope.launch {
            _uiState.value = CocktailDetailUiState(isLoading = true)
            
            try {
                val cocktailData = cocktailRepository.getCocktailById(cocktailId)
                if (cocktailData != null) {
                    _uiState.value = CocktailDetailUiState(
                        cocktail = cocktailData.toUi(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = CocktailDetailUiState(
                        isLoading = false,
                        error = "Cocktail not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CocktailDetailUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
