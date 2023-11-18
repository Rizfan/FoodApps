package com.rizfan.foodapps.ui.screen.cartscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizfan.foodapps.data.FoodRepository
import com.rizfan.foodapps.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: FoodRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
        get() = _uiState

    fun getAddedOrderMakanan() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAddedOrderMakanan()
                .collect { orderMakanan ->
                    val totalRequiredPoint =
                        orderMakanan.sumOf { it.makanan.harga * it.count }
                    _uiState.value = UiState.Success(CartState(orderMakanan, totalRequiredPoint))
                }
        }
    }

    fun updateOrderMakanan(makananId: Int, count: Int) {
        viewModelScope.launch {
            repository.updateOrderMakanan(makananId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedOrderMakanan()
                    }
                }
        }
    }
}