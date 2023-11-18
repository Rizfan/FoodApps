package com.rizfan.foodapps.ui.screen.cartscreen

import com.rizfan.foodapps.data.model.OrderMakanan

data class CartState(
    val orderMakanan: List<OrderMakanan>,
    val totalHarga: Int
)