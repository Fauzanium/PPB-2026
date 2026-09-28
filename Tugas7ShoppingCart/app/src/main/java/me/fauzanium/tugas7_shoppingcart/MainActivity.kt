package me.fauzanium.tugas7_shoppingcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import me.fauzanium.tugas7_shoppingcart.ui.theme.Tugas7ShoppingCartTheme

data class CartItem(
    val id: Int,
    val name: String,
    val price: Int,
    @param:DrawableRes val imageRes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas7ShoppingCartTheme {
                App()
            }
        }
    }
}

fun formatRupiah(amount: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
    return "Rp${formatter.format(amount)}"
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun App() {
    Shopping(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
    )
}

@Composable
fun Shopping(modifier: Modifier = Modifier) {
    val items = remember {
        listOf(
            CartItem(1, "Mobil Mainan", 50000, R.drawable.p1),
            CartItem(2, "Tumbler", 20000, R.drawable.p2),
            CartItem(3, "Sepatu", 500000, R.drawable.p3)
        )
    }

    val quantities = remember {
        mutableStateListOf(1, 1, 1)
    }

    var isCheckedOut by remember { mutableStateOf(false) }

    val total = items.indices.sumOf { index ->
        items[index].price * quantities[index]
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "Shopping Cart",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            items.forEachIndexed { index, item ->
                ItemCard(
                    name = item.name,
                    price = item.price,
                    imageRes = item.imageRes,
                    quantity = quantities[index],
                    onQuantityChange = { newQty ->
                        quantities[index] = newQty
                        isCheckedOut = false
                    }
                )
            }

            if (isCheckedOut) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Barang belanja berhasil dicheckout!",
                    color = Color(0xFF2E7D32),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = formatRupiah(total),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            }

            Button(
                onClick = { isCheckedOut = true },
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Checkout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ItemCard(
    name: String,
    price: Int,
    @DrawableRes imageRes: Int,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatRupiah(price),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedIconButton(
                    onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text(text = "-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$quantity",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                OutlinedIconButton(
                    onClick = { onQuantityChange(quantity + 1) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
