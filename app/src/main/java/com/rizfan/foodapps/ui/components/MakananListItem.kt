package com.rizfan.foodapps.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizfan.foodapps.R
import com.rizfan.foodapps.ui.theme.FoodAppsTheme


@Composable
fun MakananListItem(
    nama: String,
    gambarUrl: String,
    harga: Int,
    modifier: Modifier = Modifier,
    navigateToDetail:() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable {
            navigateToDetail()
        }
    ) {
        AsyncImage(
            model = gambarUrl,
            contentDescription = nama,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(60.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = nama,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.required_point, harga),
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MakananListItemPreview() {
    FoodAppsTheme {
        MakananListItem(
            nama = "Nasi Goreng",
            gambarUrl = "https://i.pinimg.com/originals/23/ed/d0/23edd0b146ffc32ab856ea9c1d1fcf94.jpg",
            harga = 15000,
            navigateToDetail = {}
        )
    }
}