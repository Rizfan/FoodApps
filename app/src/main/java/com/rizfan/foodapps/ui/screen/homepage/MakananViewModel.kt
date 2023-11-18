package com.rizfan.foodapps.ui.screen.homepage

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizfan.foodapps.data.FoodRepository
import com.rizfan.foodapps.data.model.OrderMakanan
import com.rizfan.foodapps.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakananViewModel @Inject constructor (
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<OrderMakanan>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderMakanan>>>
        get() = _uiState

    fun getAllMakanan() {
        viewModelScope.launch {
            repository.getsMakanan()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect{
                    _uiState.value = UiState.Success(it.sortedBy { it.makanan.nama })
                }
        }
    }

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun searchMakanan(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch {
            repository.searchMakanan(_query.value)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it.sortedBy { it.makanan.nama })
                }
        }
    }
}