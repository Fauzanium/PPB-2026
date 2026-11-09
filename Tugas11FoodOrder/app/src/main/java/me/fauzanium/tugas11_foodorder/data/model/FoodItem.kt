package me.fauzanium.tugas11_foodorder.data.model

enum class FoodCategory(val displayName: String) {
    ALL("Semua"),
    MAKANAN("Makanan"),
    MINUMAN("Minuman"),
    CEMILAN("Cemilan"),
    PROMO("Promo")
}

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: FoodCategory,
    val sellerName: String,
    val sellerClass: String = "X PPLG 1",
    val rating: Double = 4.8,
    val isPromo: Boolean = false,
    val iconName: String = "food"
)

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int
) {
    val totalPrice: Double
        get() = foodItem.price * quantity
}

enum class PaymentMethod(val title: String, val description: String) {
    QRIS("QRIS / E-Wallet", "GoPay, OVO, Dana, ShopeePay"),
    BANK_TRANSFER("Transfer Bank", "BCA, Mandiri, BRI"),
    CASH_KANTIN("Bayar Tunai di Kantin", "Bayar langsung saat mengambil makanan")
}

data class Order(
    val id: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val shippingFee: Double,
    val totalAmount: Double,
    val address: String,
    val paymentMethod: PaymentMethod,
    val date: String,
    val status: String = "Diproses"
)
