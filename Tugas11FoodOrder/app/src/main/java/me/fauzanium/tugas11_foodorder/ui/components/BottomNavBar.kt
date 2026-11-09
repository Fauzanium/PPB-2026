package me.fauzanium.tugas11_foodorder.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class ScreenRoute(val route: String, val title: String, val icon: ImageVector) {
    HOME("home", "Beranda", Icons.Default.Home),
    CART("cart", "Keranjang", Icons.Default.ShoppingCart),
    FAVORITES("favorites", "Favorit", Icons.Default.Favorite),
    ORDERS("orders", "Pesanan", Icons.Default.ReceiptLong),
    PROFILE("profile", "Profil", Icons.Default.Person)
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    cartItemCount: Int,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        val items = listOf(
            ScreenRoute.HOME,
            ScreenRoute.CART,
            ScreenRoute.FAVORITES,
            ScreenRoute.ORDERS,
            ScreenRoute.PROFILE
        )

        items.forEach { screen ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen.route) },
                icon = {
                    if (screen == ScreenRoute.CART && cartItemCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(text = if (cartItemCount > 99) "99+" else "$cartItemCount")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        }
                    } else {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title
                        )
                    }
                },
                label = { Text(text = screen.title) }
            )
        }
    }
}
