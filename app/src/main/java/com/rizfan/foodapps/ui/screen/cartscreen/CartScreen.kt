package com.rizfan.foodapps.ui.screen.cartscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizfan.foodapps.R
import com.rizfan.foodapps.ui.common.UiState
import com.rizfan.foodapps.ui.components.CartItem
import com.rizfan.foodapps.ui.components.OrderButton

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    onOrderButtonClicked: (String) -> Unit,
){
    cartViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                cartViewModel.getAddedOrderMakanan()
            }
            is UiState.Success -> {
                CartContent(
                    uiState.data,
                    onProductCountChanged = { makananId, count ->
                        cartViewModel.updateOrderMakanan(makananId, count)
                    },
                    onOrderButtonClicked = onOrderButtonClicked
                )
            }
            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    state: CartState,
    onProductCountChanged: (id: Int, count: Int) -> Unit,
    modifier: Modifier = Modifier,
    onOrderButtonClicked: (String) -> Unit,
){
    val shareMessage = stringResource(
        R.string.share_message,
        state.orderMakanan.count(),
        state.totalHarga
    )
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.menu_cart),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(weight = 1f)
        ) {
            items(state.orderMakanan, key = { it.makanan.id }) { item ->
                CartItem(
                    rewardId = item.makanan.id,
                    image = item.makanan.gambarUrl,
                    title = item.makanan.nama,
                    totalPoint = item.makanan.harga * item.count,
                    count = item.count,
                    onProductCountChanged = onProductCountChanged,
                )
                HorizontalDivider()
            }
        }
        OrderButton(
            text = stringResource(R.string.total_order, state.totalHarga),
            enabled = state.orderMakanan.isNotEmpty(),
            onClick = {
                onOrderButtonClicked(shareMessage)
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}