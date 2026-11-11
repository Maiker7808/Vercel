package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AppUserRecord
import com.example.model.CalledBall
import com.example.model.CardBundleType
import com.example.model.CouponRecord
import com.example.model.FinancialAlgorithmStats
import com.example.model.LotteryType
import com.example.model.PaymentMethodType
import com.example.model.SubscriptionPlan
import com.example.model.UserAccount
import com.example.util.QrCodeGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("LEGO Bingo", appName)
    }

    @Test
    fun `verify bingo ball letter mapping`() {
        assertEquals('B', CalledBall.fromNumber(1).letter)
        assertEquals('B', CalledBall.fromNumber(15).letter)
        assertEquals('I', CalledBall.fromNumber(16).letter)
        assertEquals('I', CalledBall.fromNumber(30).letter)
        assertEquals('N', CalledBall.fromNumber(31).letter)
        assertEquals('N', CalledBall.fromNumber(45).letter)
        assertEquals('G', CalledBall.fromNumber(46).letter)
        assertEquals('G', CalledBall.fromNumber(60).letter)
        assertEquals('O', CalledBall.fromNumber(61).letter)
        assertEquals('O', CalledBall.fromNumber(75).letter)
    }

    @Test
    fun `verify lottery types configuration`() {
        assertEquals(69, LotteryType.POWERBALL.whiteMax)
        assertEquals(26, LotteryType.POWERBALL.bonusMax)
        assertEquals(70, LotteryType.MEGA_MILLIONS.whiteMax)
        assertEquals(25, LotteryType.MEGA_MILLIONS.bonusMax)
    }

    @Test
    fun `verify qr matrix generation for sharing`() {
        val testUrl = "https://example.com/app"
        val matrix = QrCodeGenerator.generateQrMatrix(testUrl, size = 29)
        assertEquals(29, matrix.size)
        assertEquals(29, matrix[0].size)
        // Verify top-left finder pattern corner is black
        assertTrue(matrix[0][0])
        assertTrue(matrix[0][6])
        assertTrue(matrix[6][0])
        assertTrue(matrix[6][6])
    }

    @Test
    fun `verify store card bundles in USD and payment methods`() {
        assertEquals(1, CardBundleType.ONE_CARD.cardCount)
        assertEquals(0.50, CardBundleType.ONE_CARD.priceUsd, 0.001)

        assertEquals(2, CardBundleType.TWO_CARDS.cardCount)
        assertEquals(1.00, CardBundleType.TWO_CARDS.priceUsd, 0.001)

        assertEquals(3, CardBundleType.THREE_CARDS.cardCount)
        assertEquals(1.50, CardBundleType.THREE_CARDS.priceUsd, 0.001)

        assertEquals(4, CardBundleType.FOUR_CARDS.cardCount)
        assertEquals(2.00, CardBundleType.FOUR_CARDS.priceUsd, 0.001)

        assertTrue(PaymentMethodType.DE_UNA.isDeUna)
        assertNotNull(PaymentMethodType.BANCO_PICHINCHA_TRANSFER.accountNumber)
        assertNotNull(PaymentMethodType.BANCO_PICHINCHA_DEPOSIT.accountNumber)
    }

    @Test
    fun `verify subscription plans and user account model`() {
        assertEquals(2.99, SubscriptionPlan.VIP_SILVER.priceUsdMonthly, 0.001)
        assertEquals(5.99, SubscriptionPlan.VIP_GOLD.priceUsdMonthly, 0.001)
        assertEquals(9.99, SubscriptionPlan.VIP_MASTER.priceUsdMonthly, 0.001)

        val user = UserAccount(email = "test@lego.com", name = "Player1", isSubscribed = true)
        assertEquals("test@lego.com", user.email)
        assertEquals("Player1", user.name)
        assertTrue(user.isSubscribed)
    }

    @Test
    fun `verify financial algorithm winner payout calculation`() {
        val stats = FinancialAlgorithmStats(
            totalRegisteredUsers = 10,
            totalAppSessions = 150,
            totalCashAccumulatedUsd = 1000.0,
            totalStoreRevenueUsd = 450.0,
            totalPayoutsAwardedUsd = 200.0,
            dynamicPrizePoolUsd = 850.0
        )

        // Invested 10 USD with 5x multiplier -> 50 USD
        val payout1 = stats.calculateWinnerPayout(investedUsd = 10.0, tierMultiplier = 5.0)
        assertEquals(50.0, payout1, 0.001)

        // Safety limit check: max payout cannot exceed 35% of dynamic prize pool (850 * 0.35 = 297.5)
        val payoutBig = stats.calculateWinnerPayout(investedUsd = 100.0, tierMultiplier = 10.0)
        assertTrue(payoutBig <= 297.5)
    }

    @Test
    fun `verify lego store card purchases and mock payment ledger recording`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = com.example.viewmodel.BingoLotteryViewModel(app)

        val initialPoints = viewModel.uiState.value.playerPoints
        val initialPool = viewModel.uiState.value.accumulatedMoneyPoolUsd
        val initialPurchasesCount = viewModel.uiState.value.storePurchases.size

        // 1. Purchase Individual Card (1 Card) with DeUna
        viewModel.purchaseCardBundle(CardBundleType.ONE_CARD, PaymentMethodType.DE_UNA)
        val state1 = viewModel.uiState.value
        assertTrue(state1.cards.size >= 1)
        assertEquals(initialPoints + 250, state1.playerPoints)
        assertEquals(initialPool + 0.50, state1.accumulatedMoneyPoolUsd, 0.001)
        assertEquals(initialPurchasesCount + 1, state1.storePurchases.size)

        // 2. Purchase Quadruple Card Bundle (4 Cards Full) with Banco Pichincha
        viewModel.purchaseCardBundle(CardBundleType.FOUR_CARDS, PaymentMethodType.BANCO_PICHINCHA_DEPOSIT)
        val state4 = viewModel.uiState.value
        assertEquals(4, state4.cards.size)
        assertEquals(initialPoints + 250 + 1500, state4.playerPoints)
        assertEquals(initialPool + 0.50 + 2.00, state4.accumulatedMoneyPoolUsd, 0.001)
        assertEquals(initialPurchasesCount + 2, state4.storePurchases.size)

        // 3. Verify latest cash deposit record has correct method
        val latestDeposit = state4.cashDeposits.first()
        assertEquals(PaymentMethodType.BANCO_PICHINCHA_DEPOSIT.displayName, latestDeposit.method)
        assertEquals(2.00, latestDeposit.amountUsd, 0.001)
        assertEquals("Acreditado", latestDeposit.status)
    }
}
