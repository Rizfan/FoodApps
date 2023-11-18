package com.rizfan.foodapps.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object Detail : Screen("detail/{makananId}") {
        fun createRoute(makananId: Int) = "detail/$makananId"
    }
}
