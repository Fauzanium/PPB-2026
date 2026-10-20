package me.fauzanium.ets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import me.fauzanium.ets.navigation.AppNavGraph
import me.fauzanium.ets.ui.theme.ETSTheme
import me.fauzanium.ets.viewmodel.MemberViewModel
import me.fauzanium.ets.viewmodel.MemberViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ETSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MemberViewModel = viewModel(
                        factory = MemberViewModelFactory()
                    )
                    AppNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
