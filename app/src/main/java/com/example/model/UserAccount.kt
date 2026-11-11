package com.example.model

enum class SubscriptionPlan(
    val title: String,
    val priceUsdMonthly: Double,
    val dailyCards: Int,
    val bonusPoints: Int,
    val perks: List<String>
) {
    VIP_SILVER(
        title = "Pase VIP Silver",
        priceUsdMonthly = 2.99,
        dailyCards = 5,
        bonusPoints = 2500,
        perks = listOf("5 Cartones diarios", "Doble oportunidad en lotería", "Sin anuncios")
    ),
    VIP_GOLD(
        title = "Pase VIP Gold",
        priceUsdMonthly = 5.99,
        dailyCards = 15,
        bonusPoints = 7500,
        perks = listOf("15 Cartones diarios", "Auto-marcado gratis", "Multiplicador 3x en puntos")
    ),
    VIP_MASTER(
        title = "Pase VIP Master LEGO",
        priceUsdMonthly = 9.99,
        dailyCards = 50,
        bonusPoints = 20000,
        perks = listOf("Cartones ilimitados de prueba", "Sorteos exclusivos en vivo", "Soporte prioritario")
    )
}

data class UserAccount(
    val email: String,
    val name: String,
    val activeSubscription: SubscriptionPlan? = null,
    val isSubscribed: Boolean = false,
    val subscriptionExpiryDate: String? = null
)
