package com.ecoquest.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Base ViewModel to provide common functionality for all ViewModels.
 */
abstract class BaseViewModel<S> : ViewModel() {
    
    protected abstract val initialState: S
    
    private val _uiState by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<S> by lazy { _uiState.asStateFlow() }

    protected fun updateState(reducer: (S) -> S) {
        _uiState.value = reducer(_uiState.value)
    }
}
