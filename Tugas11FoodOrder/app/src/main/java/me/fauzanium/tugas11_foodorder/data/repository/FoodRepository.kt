package me.fauzanium.tugas11_foodorder.data.repository

import kotlinx.coroutines.flow.StateFlow
import me.fauzanium.tugas11_foodorder.data.local.DummyData
import me.fauzanium.tugas11_foodorder.data.model.CartItem
import me.fauzanium.tugas11_foodorder.data.model.FoodCategory
import me.fauzanium.tugas11_foodorder.data.model.FoodItem
import me.fauzanium.tugas11_foodorder.data.model.Order
import me.fauzanium.tugas11_foodorder.data.model.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

interface FoodRepository {
    val products: StateFlow<List<FoodItem>>
    val cartItems: StateFlow<List<CartItem>>
    val favoriteIds: StateFlow<Set<String>>
    val orders: StateFlow<List<Order>>
    val isDarkTheme: StateFlow<Boolean>
    val deliveryAddress: StateFlow<String>
    val selectedPaymentMethod: StateFlow<PaymentMethod>

    fun searchAndFilter(query: String, category: FoodCategory): List<FoodItem>
    fun getProductById(id: String): FoodItem?
    fun toggleFavorite(productId: String)
    fun addToCart(productId: String, quantity: Int = 1)
    fun updateCartItemQuantity(productId: String, newQuantity: Int)
    fun removeFromCart(productId: String)
    fun clearCart()
    fun addProduct(foodItem: FoodItem)
    fun updateDeliveryAddress(address: String)
    fun updatePaymentMethod(paymentMethod: PaymentMethod)
    fun placeOrder(): Order?
    fun toggleTheme(isDark: Boolean)
}

class FoodRepositoryImpl : FoodRepository {
    private val _products = MutableStateFlow<List<FoodItem>>(DummyData.sampleFoodItems)
    override val products: StateFlow<List<FoodItem>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(DummyData.sampleFoodItems[1], 1),
            CartItem(DummyData.sampleFoodItems[2], 1)
        )
    )
    override val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(setOf("food_1", "food_3"))
    override val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    override val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    override val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _deliveryAddress = MutableStateFlow("Jl. Pendidikan No. 15, Kelas XI PPLG 1")
    override val deliveryAddress: StateFlow<String> = _deliveryAddress.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.QRIS)
    override val selectedPaymentMethod: StateFlow<PaymentMethod> = _selectedPaymentMethod.asStateFlow()

    override fun searchAndFilter(query: String, category: FoodCategory): List<FoodItem> {
        return _products.value.filter { item ->
            val matchesQuery = item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.sellerName.contains(query, ignoreCase = true)

            val matchesCategory = when (category) {
                FoodCategory.ALL -> true
                FoodCategory.PROMO -> item.isPromo
                else -> item.category == category
            }

            matchesQuery && matchesCategory
        }
    }

    override fun getProductById(id: String): FoodItem? {
        return _products.value.find { it.id == id }
    }

    override fun toggleFavorite(productId: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _favoriteIds.value = current
    }

    override fun addToCart(productId: String, quantity: Int) {
        val item = getProductById(productId) ?: return
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.foodItem.id == productId }

        if (index >= 0) {
            val existing = currentList[index]
            currentList[index] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            currentList.add(CartItem(foodItem = item, quantity = quantity))
        }
        _cartItems.value = currentList
    }

    override fun updateCartItemQuantity(productId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(productId)
            return
        }
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.foodItem.id == productId }
        if (index >= 0) {
            currentList[index] = currentList[index].copy(quantity = newQuantity)
            _cartItems.value = currentList
        }
    }

    override fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.foodItem.id == productId }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }

    override fun addProduct(foodItem: FoodItem) {
        _products.value = listOf(foodItem) + _products.value
    }

    override fun updateDeliveryAddress(address: String) {
        _deliveryAddress.value = address
    }

    override fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        _selectedPaymentMethod.value = paymentMethod
    }

    override fun placeOrder(): Order? {
        val currentCart = _cartItems.value
        if (currentCart.isEmpty()) return null

        val subtotal = currentCart.sumOf { it.totalPrice }
        val shippingFee = 5000.0
        val total = subtotal + shippingFee
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val dateStr = dateFormat.format(Date())

        val newOrder = Order(
            id = "ORD-" + UUID.randomUUID().toString().take(6).uppercase(),
            items = currentCart,
            subtotal = subtotal,
            shippingFee = shippingFee,
            totalAmount = total,
            address = _deliveryAddress.value,
            paymentMethod = _selectedPaymentMethod.value,
            date = dateStr,
            status = "Diproses Siswa"
        )

        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        return newOrder
    }

    override fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
}
