package me.fauzanium.tugas11_foodorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import me.fauzanium.tugas11_foodorder.ui.components.AddToCartFeedbackDialog
import me.fauzanium.tugas11_foodorder.ui.components.BottomNavBar
import me.fauzanium.tugas11_foodorder.ui.components.ScreenRoute
import me.fauzanium.tugas11_foodorder.ui.screens.CartScreen
import me.fauzanium.tugas11_foodorder.ui.screens.CheckoutScreen
import me.fauzanium.tugas11_foodorder.ui.screens.FavoritesScreen
import me.fauzanium.tugas11_foodorder.ui.screens.FoodDetailScreen
import me.fauzanium.tugas11_foodorder.ui.screens.HomeScreen
import me.fauzanium.tugas11_foodorder.ui.screens.OrdersScreen
import me.fauzanium.tugas11_foodorder.ui.screens.ProfileScreen
import me.fauzanium.tugas11_foodorder.ui.theme.Tugas11FoodOrderTheme
import me.fauzanium.tugas11_foodorder.viewmodel.FoodViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketSiswaFoodOrderApp()
        }
    }
}

@Composable
fun MarketSiswaFoodOrderApp(
    viewModel: FoodViewModel = remember { FoodViewModel() }
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val cartItemCount by viewModel.cartItemCount.collectAsState()
    val selectedProductForDetail by viewModel.selectedProductForDetail.collectAsState()
    val addedToCartFeedback by viewModel.addedToCartFeedback.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    var currentRoute by remember { mutableStateOf(ScreenRoute.HOME.route) }
    var isInCheckout by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbarMessage()
        }
    }

    Tugas11FoodOrderTheme(darkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (selectedProductForDetail == null && !isInCheckout) {
                    BottomNavBar(
                        currentRoute = currentRoute,
                        cartItemCount = cartItemCount,
                        onNavigate = { route ->
                            currentRoute = route
                        }
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when {
                    // Food Detail Screen Navigation
                    selectedProductForDetail != null -> {
                        FoodDetailScreen(
                            foodItem = selectedProductForDetail!!,
                            viewModel = viewModel,
                            onBackClick = { viewModel.selectProductForDetail(null) },
                            onCartClick = {
                                viewModel.selectProductForDetail(null)
                                currentRoute = ScreenRoute.CART.route
                            }
                        )
                    }

                    // Checkout Screen Navigation
                    isInCheckout -> {
                        CheckoutScreen(
                            viewModel = viewModel,
                            onBackClick = { isInCheckout = false },
                            onOrderSuccess = {
                                isInCheckout = false
                                currentRoute = ScreenRoute.ORDERS.route
                            }
                        )
                    }

                    // Bottom Nav Screens
                    else -> {
                        when (currentRoute) {
                            ScreenRoute.HOME.route -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToDetail = { food ->
                                        viewModel.selectProductForDetail(food)
                                    },
                                    onNavigateToProfile = {
                                        currentRoute = ScreenRoute.PROFILE.route
                                    }
                                )
                            }

                            ScreenRoute.CART.route -> {
                                CartScreen(
                                    viewModel = viewModel,
                                    onNavigateToCheckout = { isInCheckout = true },
                                    onNavigateToHome = { currentRoute = ScreenRoute.HOME.route }
                                )
                            }

                            ScreenRoute.FAVORITES.route -> {
                                FavoritesScreen(
                                    viewModel = viewModel,
                                    onNavigateToDetail = { food ->
                                        viewModel.selectProductForDetail(food)
                                    }
                                )
                            }

                            ScreenRoute.ORDERS.route -> {
                                OrdersScreen(viewModel = viewModel)
                            }

                            ScreenRoute.PROFILE.route -> {
                                ProfileScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }

            // Add To Cart Feedback Dialog
            addedToCartFeedback?.let { product ->
                AddToCartFeedbackDialog(
                    foodItem = product,
                    onViewCart = {
                        viewModel.dismissAddToCartFeedback()
                        viewModel.selectProductForDetail(null)
                        currentRoute = ScreenRoute.CART.route
                    },
                    onContinueShopping = {
                        viewModel.dismissAddToCartFeedback()
                    }
                )
            }
        }
    }
}
