package me.fauzanium.ets.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.fauzanium.ets.ui.screens.HomeScreen
import me.fauzanium.ets.ui.screens.ManageDataScreen
import me.fauzanium.ets.ui.screens.MemberDetailScreen
import me.fauzanium.ets.ui.screens.MemberListScreen
import me.fauzanium.ets.ui.screens.RegisterScreen
import me.fauzanium.ets.ui.screens.SearchScreen
import me.fauzanium.ets.viewmodel.MemberViewModel

@Composable
fun AppNavGraph(
    viewModel: MemberViewModel,
    navController: NavHostController = rememberNavController()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val userMessage by viewModel.userMessage.collectAsState()

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Dashboard / Home Screen
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateTo = { route -> navController.navigate(route) }
                )
            }

            // Form Pendaftaran Screen (With optional memberId for Edit mode)
            composable(
                route = Screen.Register.route,
                arguments = listOf(
                    navArgument("memberId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->
                val memberIdArg = backStackEntry.arguments?.getInt("memberId")
                val memberId = if (memberIdArg != null && memberIdArg != -1) memberIdArg else null

                RegisterScreen(
                    viewModel = viewModel,
                    editingMemberId = memberId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Daftar Anggota Screen
            composable(Screen.MemberList.route) {
                MemberListScreen(
                    viewModel = viewModel,
                    onNavigateTo = { route -> navController.navigate(route) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Pencarian Data Screen
            composable(Screen.Search.route) {
                SearchScreen(
                    viewModel = viewModel,
                    onNavigateTo = { route -> navController.navigate(route) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Kelola Data Screen (CRUD)
            composable(Screen.ManageData.route) {
                ManageDataScreen(
                    viewModel = viewModel,
                    onNavigateTo = { route -> navController.navigate(route) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Detail Anggota Screen
            composable(
                route = Screen.MemberDetail.route,
                arguments = listOf(
                    navArgument("memberId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0

                MemberDetailScreen(
                    memberId = memberId,
                    viewModel = viewModel,
                    onNavigateTo = { route -> navController.navigate(route) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
