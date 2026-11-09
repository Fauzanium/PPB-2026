package me.fauzanium.tugas11_foodorder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

import me.fauzanium.tugas11_foodorder.data.model.CartItem
import me.fauzanium.tugas11_foodorder.data.model.FoodCategory
import me.fauzanium.tugas11_foodorder.data.model.FoodItem
import me.fauzanium.tugas11_foodorder.data.model.Order
import me.fauzanium.tugas11_foodorder.data.model.PaymentMethod
import me.fauzanium.tugas11_foodorder.data.repository.FoodRepository
import me.fauzanium.tugas11_foodorder.data.repository.FoodRepositoryImpl
import java.util.UUID

class FoodViewModel(
    private val repository: FoodRepository = FoodRepositoryImpl()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(FoodCategory.ALL)
    val selectedCategory: StateFlow<FoodCategory> = _selectedCategory.asStateFlow()

    val filteredProducts: StateFlow<List<FoodItem>> = combine(
        repository.products,
        _searchQuery,
        _selectedCategory
    ) { products, query, category ->
        products.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.sellerName.contains(query, ignoreCase = true)

            val matchesCategory = when (category) {
                FoodCategory.ALL -> true
                FoodCategory.PROMO -> item.isPromo
                else -> item.category == category
            }

            matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val favoriteIds: StateFlow<Set<String>> = repository.favoriteIds
    val orders: StateFlow<List<Order>> = repository.orders
    val isDarkTheme: StateFlow<Boolean> = repository.isDarkTheme
    val deliveryAddress: StateFlow<String> = repository.deliveryAddress
    val selectedPaymentMethod: StateFlow<PaymentMethod> = repository.selectedPaymentMethod

    val favoriteProducts: StateFlow<List<FoodItem>> = combine(
        repository.products,
        repository.favoriteIds
    ) { products, favIds ->
        products.filter { favIds.contains(it.id) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartItemCount: StateFlow<Int> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.quantity }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val cartSubtotal: StateFlow<Double> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.totalPrice }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val shippingFee: Double = 5000.0

    val cartTotal: StateFlow<Double> = cartSubtotal.combine(cartSubtotal) { subtotal, _ ->
        if (subtotal > 0) subtotal + shippingFee else 0.0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    private val _selectedProductForDetail = MutableStateFlow<FoodItem?>(null)
    val selectedProductForDetail: StateFlow<FoodItem?> = _selectedProductForDetail.asStateFlow()

    private val _addedToCartFeedback = MutableStateFlow<FoodItem?>(null)
    val addedToCartFeedback: StateFlow<FoodItem?> = _addedToCartFeedback.asStateFlow()

    private val _showAddProductDialog = MutableStateFlow(false)
    val showAddProductDialog: StateFlow<Boolean> = _showAddProductDialog.asStateFlow()

    private val _lastCreatedOrder = MutableStateFlow<Order?>(null)
    val lastCreatedOrder: StateFlow<Order?> = _lastCreatedOrder.asStateFlow()

    private val _showOrderSuccessDialog = MutableStateFlow(false)
    val showOrderSuccessDialog: StateFlow<Boolean> = _showOrderSuccessDialog.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: FoodCategory) {
        _selectedCategory.value = category
    }

    fun selectProductForDetail(product: FoodItem?) {
        _selectedProductForDetail.value = product
    }

    fun toggleFavorite(productId: String) {
        repository.toggleFavorite(productId)
    }

    fun addToCart(product: FoodItem, quantity: Int = 1) {
        repository.addToCart(product.id, quantity)
        _addedToCartFeedback.value = product
        _snackbarMessage.value = "${product.name} berhasil ditambahkan ke keranjang!"
    }

    fun dismissAddToCartFeedback() {
        _addedToCartFeedback.value = null
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        repository.updateCartItemQuantity(productId, newQuantity)
    }

    fun removeFromCart(productId: String) {
        repository.removeFromCart(productId)
    }

    fun updateDeliveryAddress(address: String) {
        repository.updateDeliveryAddress(address)
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        repository.updatePaymentMethod(method)
    }

    fun placeOrder(): Boolean {
        val newOrder = repository.placeOrder()
        return if (newOrder != null) {
            _lastCreatedOrder.value = newOrder
            _showOrderSuccessDialog.value = true
            _snackbarMessage.value = "Pesanan #${newOrder.id} berhasil dibuat!"
            true
        } else {
            _snackbarMessage.value = "Keranjang belanja masih kosong!"
            false
        }
    }

    fun dismissOrderSuccessDialog() {
        _showOrderSuccessDialog.value = false
        _lastCreatedOrder.value = null
    }

    fun setShowAddProductDialog(show: Boolean) {
        _showAddProductDialog.value = show
    }

    fun addNewProduct(
        name: String,
        description: String,
        price: Double,
        category: FoodCategory,
        sellerName: String,
        sellerClass: String,
        isPromo: Boolean
    ) {
        val newProduct = FoodItem(
            id = "food_" + UUID.randomUUID().toString().take(8),
            name = name,
            description = description,
            price = price,
            category = category,
            sellerName = sellerName,
            sellerClass = sellerClass.ifBlank { "XI PPLG 1" },
            rating = 5.0,
            isPromo = isPromo,
            iconName = when (category) {
                FoodCategory.MAKANAN -> "food"
                FoodCategory.MINUMAN -> "drink"
                FoodCategory.CEMILAN -> "snack"
                FoodCategory.PROMO -> "promo"
                else -> "food"
            }
        )
        repository.addProduct(newProduct)
        _showAddProductDialog.value = false
        _snackbarMessage.value = "Produk makanan '${newProduct.name}' berhasil ditambahkan!"
    }

    fun toggleTheme(isDark: Boolean) {
        repository.toggleTheme(isDark)
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FoodViewModel() as T
            }
        }
    }
}
