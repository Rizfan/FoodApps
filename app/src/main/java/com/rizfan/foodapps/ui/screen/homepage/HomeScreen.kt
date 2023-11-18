package com.rizfan.foodapps.ui.screen.homepage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizfan.foodapps.R
import com.rizfan.foodapps.data.model.OrderMakanan
import com.rizfan.foodapps.ui.common.UiState
import com.rizfan.foodapps.ui.components.MakananListItem
import com.rizfan.foodapps.ui.components.searchBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    makananViewModel: MakananViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit,
) {
    makananViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                makananViewModel.getAllMakanan()
            }

            is UiState.Success -> {
                HomeContent(
                    orderMakanan = uiState.data,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail
                )
            }

            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    orderMakanan: List<OrderMakanan>,
    modifier: Modifier = Modifier,
    makananViewModel: MakananViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit
) {

    val query by makananViewModel.query

    Box(modifier = modifier) {
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()
        val showButton: Boolean by remember {
            derivedStateOf { listState.firstVisibleItemIndex > 0 }
        }
        LazyColumn(
            state = listState, contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            stickyHeader {
                searchBar(
                    query = query,
                    onQueryChange = makananViewModel::searchMakanan,
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                )
            }
            if (orderMakanan.isEmpty()){
                item {
                    Text(
                        text = "Data Tidak Ada!",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }else{
                items(orderMakanan) { data ->
                    MakananListItem(
                        nama = data.makanan.nama,
                        gambarUrl = data.makanan.gambarUrl,
                        harga = data.makanan.harga,
                        navigateToDetail = {
                            navigateToDetail(data.makanan.id)
                        }
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(onClick = {
                scope.launch {
                    listState.scrollToItem(index = 0)
                }
            })
        }
    }
}

@Composable
fun ScrollToTopButton(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick, modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = stringResource(R.string.scroll_to_top),
        )
    }
}
