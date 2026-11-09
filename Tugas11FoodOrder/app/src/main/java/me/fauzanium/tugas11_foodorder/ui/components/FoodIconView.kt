package me.fauzanium.tugas11_foodorder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FoodIconView(
    iconName: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp
) {
    val gradientColors: List<Color>
    val icon: ImageVector

    when (iconName.lowercase()) {
        "cake" -> {
            gradientColors = listOf(Color(0xFFFF8A65), Color(0xFFD81B60))
            icon = Icons.Default.Fastfood
        }
        "rice" -> {
            gradientColors = listOf(Color(0xFFFFCA28), Color(0xFFF57C00))
            icon = Icons.Default.Restaurant
        }
        "chicken" -> {
            gradientColors = listOf(Color(0xFFFF7043), Color(0xFFC62828))
            icon = Icons.Default.LocalDining
        }
        "drink" -> {
            gradientColors = listOf(Color(0xFF26C6DA), Color(0xFF00838F))
            icon = Icons.Default.RestaurantMenu
        }
        "boba" -> {
            gradientColors = listOf(Color(0xFFAB47BC), Color(0xFF6A1B9A))
            icon = Icons.Default.RestaurantMenu
        }
        "snack" -> {
            gradientColors = listOf(Color(0xFFFFB74D), Color(0xFFE65100))
            icon = Icons.Default.Fastfood
        }
        "dimsum" -> {
            gradientColors = listOf(Color(0xFF66BB6A), Color(0xFF2E7D32))
            icon = Icons.Default.LocalDining
        }
        else -> {
            gradientColors = listOf(Color(0xFF7E57C2), Color(0xFF4527A0))
            icon = Icons.Default.Star
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}
