package me.fauzanium.tugas11_foodorder.data.local

import me.fauzanium.tugas11_foodorder.data.model.FoodCategory
import me.fauzanium.tugas11_foodorder.data.model.FoodItem

object DummyData {
    val sampleFoodItems = listOf(
        FoodItem(
            id = "food_1",
            name = "Brownies Lumer Cokelat",
            description = "Brownies lezat buatan sendiri dengan keju parut & cokelat melimpah, hangat dan manis cocok untuk cemilan sore.",
            price = 15000.0,
            category = FoodCategory.CEMILAN,
            sellerName = "Siti Rahma",
            sellerClass = "XI Kuliner 2",
            rating = 4.9,
            isPromo = true,
            iconName = "cake"
        ),
        FoodItem(
            id = "food_2",
            name = "Nasi Goreng Spesial Telur",
            description = "Nasi goreng bumbu rempah pilihan dilengkapi telur ceplok, sosis, dan kerupuk renyah.",
            price = 18000.0,
            category = FoodCategory.MAKANAN,
            sellerName = "Budi Santoso",
            sellerClass = "XII PPLG 1",
            rating = 4.8,
            isPromo = false,
            iconName = "rice"
        ),
        FoodItem(
            id = "food_3",
            name = "Ayam Geprek Sambal Matah",
            description = "Ayam crispy renyah di-geprek dengan sambal matah segar racikan khas siswa.",
            price = 20000.0,
            category = FoodCategory.MAKANAN,
            sellerName = "Ahmad Rizky",
            sellerClass = "XI PPLG 3",
            rating = 4.9,
            isPromo = true,
            iconName = "chicken"
        ),
        FoodItem(
            id = "food_4",
            name = "Es Teh Solo Segar",
            description = "Es teh racikan khas Solo dengan rasa melati mantap dan kemanisan yang pas.",
            price = 5000.0,
            category = FoodCategory.MINUMAN,
            sellerName = "Dewi Lestari",
            sellerClass = "X AKL 1",
            rating = 4.7,
            isPromo = false,
            iconName = "drink"
        ),
        FoodItem(
            id = "food_5",
            name = "Risol Mayo Melted (3 pcs)",
            description = "Risol isian smoked beef, telur, dan mayonnaise lumer gurih renyah saat digigit.",
            price = 12000.0,
            category = FoodCategory.CEMILAN,
            sellerName = "Rina Melati",
            sellerClass = "XI Tata Boga 1",
            rating = 4.9,
            isPromo = false,
            iconName = "snack"
        ),
        FoodItem(
            id = "food_6",
            name = "Dimsum Ayam Mentai (4 pcs)",
            description = "Dimsum olahan daging ayam segar dipadu saus mentai gurih dan di-torch hingga wangi.",
            price = 16000.0,
            category = FoodCategory.MAKANAN,
            sellerName = "Fajar Permana",
            sellerClass = "XII DKV 2",
            rating = 4.8,
            isPromo = true,
            iconName = "dimsum"
        ),
        FoodItem(
            id = "food_7",
            name = "Es Matcha Latte Creamy",
            description = "Minuman matcha jepang asli dengan susu segar dan topping cold foam lembut.",
            price = 14000.0,
            category = FoodCategory.MINUMAN,
            sellerName = "Nabila Putri",
            sellerClass = "X PPLG 2",
            rating = 4.9,
            isPromo = false,
            iconName = "boba"
        ),
        FoodItem(
            id = "food_8",
            name = "Cireng Bumbu Rujak Pedas",
            description = "Cireng crispy gurih kenyal lengkap dengan kuah bumbu rujak pedas manis asam segar.",
            price = 10000.0,
            category = FoodCategory.CEMILAN,
            sellerName = "Andi Wijaya",
            sellerClass = "XI TKT 1",
            rating = 4.6,
            isPromo = false,
            iconName = "snack"
        )
    )
}
