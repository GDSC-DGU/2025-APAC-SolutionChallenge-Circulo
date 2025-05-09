package com.example.circuler.presentation.ui.enter

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
class EnterPackagingViewModel @Inject constructor() : ViewModel() {
    // state 관리
    private val _state = MutableStateFlow(EnterPackagingState())
    val state: StateFlow<EnterPackagingState>
        get() = _state.asStateFlow()

    // side effect 관리
    private val _sideEffect: MutableSharedFlow<EnterPackagingSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<EnterPackagingSideEffect>
        get() = _sideEffect.asSharedFlow()

    fun navigateToConfirmPackage() =
        viewModelScope.launch {
            _sideEffect.emit(
                EnterPackagingSideEffect.NavigateToConfirmPackage
            )
        }

    fun updatedLocation(location: String) {
        _state.value = _state.value.copy(
            uiState = _state.value.uiState.copy(
                location = location
            )
        )
    }

    fun controlPackageBottomSheet() {
        _state.value = _state.value.copy(
            isOpenPackageBottomSheet = !_state.value.isOpenPackageBottomSheet
        )
    }

    fun updatePackageSelectedIndex(index: Int) {
        _state.value = _state.value.copy(selectedPackageIndex = index)
    }

    fun controlDeliveryBottomSheet() {
        _state.value = _state.value.copy(
            isOpenDeliveryBottomSheet = !_state.value.isOpenDeliveryBottomSheet
        )
    }

    fun updateDeliverySelectedIndex(index: Int) {
        _state.value = _state.value.copy(selectedDeliveryIndex = index)
    }

    // todo: post할때 quantity.toInt
}
