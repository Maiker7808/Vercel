package com.example.model

enum class CardBundleType(
    val cardCount: Int,
    val title: String,
    val priceUsd: Double,
    val pointsAwarded: Int,
    val badge: String? = null
) {
    ONE_CARD(
        cardCount = 1,
        title = "1 Cartón Individual",
        priceUsd = 0.50,
        pointsAwarded = 250,
        badge = "Básico"
    ),
    TWO_CARDS(
        cardCount = 2,
        title = "2 Cartones",
        priceUsd = 1.00,
        pointsAwarded = 600,
        badge = "Popular"
    ),
    THREE_CARDS(
        cardCount = 3,
        title = "3 Cartones",
        priceUsd = 1.50,
        pointsAwarded = 1000,
        badge = "Ahorro"
    ),
    FOUR_CARDS(
        cardCount = 4,
        title = "4 Cartones (Full)",
        priceUsd = 2.00,
        pointsAwarded = 1500,
        badge = "Mejor Valor"
    )
}

enum class PaymentMethodType(
    val displayName: String,
    val appOrBankName: String,
    val bankAccountName: String,
    val accountNumber: String,
    val accountType: String,
    val identificationNumber: String,
    val email: String,
    val phoneNumber: String,
    val isDeUna: Boolean = false
) {
    DE_UNA(
        displayName = "Pago DeUna! / QR (Banco Pichincha)",
        appOrBankName = "DeUna! Ecuador",
        bankAccountName = "",
        accountNumber = "2214686194",
        accountType = "Cuenta de Ahorros",
        identificationNumber = "",
        email = "",
        phoneNumber = "0991234567",
        isDeUna = true
    ),
    BANCO_PICHINCHA_TRANSFER(
        displayName = "Banco Pichincha - Transferencia",
        appOrBankName = "Banco Pichincha C.A.",
        bankAccountName = "",
        accountNumber = "2214686194",
        accountType = "Cuenta de Ahorros",
        identificationNumber = "",
        email = "",
        phoneNumber = "0991234567",
        isDeUna = false
    ),
    BANCO_PICHINCHA_DEPOSIT(
        displayName = "Depósito / Mi Vecino (Banco Pichincha)",
        appOrBankName = "Banco Pichincha / Mi Vecino",
        bankAccountName = "",
        accountNumber = "2214686194",
        accountType = "Cuenta de Ahorros",
        identificationNumber = "",
        email = "",
        phoneNumber = "0991234567",
        isDeUna = false
    ),
    NEQUI(
        displayName = "Pago con Nequi (QR / Celular)",
        appOrBankName = "Nequi",
        bankAccountName = "Cuenta Nequi Oficial",
        accountNumber = "0991234567",
        accountType = "Billetera Digital",
        identificationNumber = "",
        email = "pagos@letsgobingo.ec",
        phoneNumber = "0991234567",
        isDeUna = false
    )
}

data class PurchaseReceipt(
    val bundle: CardBundleType,
    val paymentMethod: PaymentMethodType,
    val referenceCode: String,
    val formattedTotalUsd: String,
    val timestamp: Long = System.currentTimeMillis()
)
