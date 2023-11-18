package com.rizfan.foodapps.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizfan.foodapps.R
import com.rizfan.foodapps.ui.common.UiState
import com.rizfan.foodapps.ui.components.OrderButton
import com.rizfan.foodapps.ui.components.ProductCounter
import com.rizfan.foodapps.ui.theme.FoodAppsTheme

@Composable
fun DetailScreen(
    makananId: Int,
    detailViewModel: DetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit
){
    LaunchedEffect(makananId) {
        detailViewModel.init(makananId)
    }
    detailViewModel.uiState.collectAsState(initial = UiState.Loading).value.let {uiState ->
        when(uiState){
            is UiState.Loading ->{
                detailViewModel.getMakananbyId(makananId)
            }
            is UiState.Success ->{
                val data = uiState.data
                DetailContent(
                    nama = data.makanan.nama,
                    harga = data.makanan.harga,
                    gambarUrl = data.makanan.gambarUrl,
                    deskripsi = data.makanan.deskripsi,
                    count = data.count,
                    onBackClick = navigateBack,
                    onAddToCart = { count ->
                        detailViewModel.addToCart(data.makanan, count)
                        navigateToCart()
                    }
                )
            }
            is UiState.Error ->{}
        }
    }
}

@Composable
fun DetailContent(
    nama: String,
    harga: Int,
    gambarUrl: String,
    deskripsi: String,
    count: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAddToCart: (count: Int) -> Unit,
    ){

    var totalHarga by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }
    Column {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                AsyncImage(
                    model = gambarUrl,
                    contentDescription = nama,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = nama,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                Text(
                    text = stringResource(R.string.required_point, harga),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.LightGray))
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProductCounter(
                1,
                orderCount,
                onProductIncreased = { orderCount++ },
                onProductDecreased = { if (orderCount > 0) orderCount-- },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            totalHarga = harga * orderCount
            OrderButton(
                text = stringResource(R.string.add_to_cart, totalHarga),
                enabled = orderCount > 0,
                onClick = {
                    onAddToCart(orderCount)
                }
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailContentPreview() {
    FoodAppsTheme {
        DetailContent(
            "Nasi Goreng",
            10000,
            "https://i.pinimg.com/originals/23/ed/d0/23edd0b146ffc32ab856ea9c1d1fcf94.jpg",
            "Lorem Ipsum",
            1,
            onBackClick = {},
            onAddToCart = {}
        )
    }
}