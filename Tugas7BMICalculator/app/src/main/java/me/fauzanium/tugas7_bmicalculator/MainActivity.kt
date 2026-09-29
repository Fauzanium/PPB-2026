package me.fauzanium.tugas7_bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.fauzanium.tugas7_bmicalculator.ui.theme.Tugas7BMICalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas7BMICalculatorTheme {
                App()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun App() {
    Shopping(modifier = Modifier
        .safeContentPadding()
        .padding(top = 10.dp)
    )
}

@Composable
fun Shopping(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row() {
            
        }
    }
}



