package com.example.circuler.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    // state 관리
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState>
        get() = _state.asStateFlow()

    // side effect 관리
    private val _sideEffect: MutableSharedFlow<HomeSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<HomeSideEffect>
        get() = _sideEffect.asSharedFlow()

    fun navigateToAddPackaging() =
        viewModelScope.launch {
            _sideEffect.emit(
                HomeSideEffect.NavigateToAddPackaging
            )
        }

    fun navigateToRequestedPackages() =
        viewModelScope.launch {
            _sideEffect.emit(
                HomeSideEffect.NavigateToRequestedPackages
            )
        }

    fun navigateToReadyToGoPackages() =
        viewModelScope.launch {
            _sideEffect.emit(
                HomeSideEffect.NavigateToReadyToGoPackages
            )
        }

    fun navigateToMap() =
        viewModelScope.launch {
            _sideEffect.emit(
                HomeSideEffect.NavigateToMap
            )
        }
}
