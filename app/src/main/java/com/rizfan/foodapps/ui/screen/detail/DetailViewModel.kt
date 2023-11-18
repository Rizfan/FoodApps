package com.rizfan.foodapps.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizfan.foodapps.data.FoodRepository
import com.rizfan.foodapps.data.model.Makanan
import com.rizfan.foodapps.data.model.OrderMakanan
import com.rizfan.foodapps.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<OrderMakanan>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<OrderMakanan>>
        get() = _uiState

    fun init(id:Int) {
        getMakananbyId(id)
    }

    fun getMakananbyId(makananId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getOrderMakananById(makananId))
        }
    }

    fun addToCart(makanan: Makanan, count: Int) {
        viewModelScope.launch {
            repository.updateOrderMakanan(makanan.id, count)
        }
    }
}