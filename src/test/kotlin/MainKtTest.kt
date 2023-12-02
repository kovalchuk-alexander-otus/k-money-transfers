import org.junit.jupiter.api.Test

import org.junit.Assert.*

class MainKtTest {

    @Test
    fun calcCommissionBase_Default() {
        // arrange
        val amount: Int = 10000
        val commission: Float = 0.75F

        // act
        val result = calcCommissionBase(amount, commission)

        // assert
        assertEquals(75F, result)
    }

    @Test
    fun calcCommissionBase_AllParameters() {
        // arrange
        val amount: Int = 10000
        val commission: Float = 0.75F
        val commissionMin: Float = 90F
        val commissionPlus: Float = 20F

        // act
        val result = calcCommissionBase(amount, commission, commissionMin, commissionPlus)

        // assert
        assertEquals(95F, result)
    }

    @Test
    fun calcCommissionBase_WithoutCommissionPlus() {
        // arrange
        val amount: Int = 10000
        val commission: Float = 0.75F
        val commissionMin: Float = 90F
        val commissionPlus: Float = 0F

        // act
        val result = calcCommissionBase(amount, commission, commissionMin, commissionPlus)

        // assert
        assertEquals(90F, result)
    }

    @Test
    fun checkLimit_VKPayByDay() {
        val cardType: String = cVKPay
        val amount: Int = cVKPayDayLimit+1
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(false, result)
    }
    @Test
    fun checkLimit_VKPayByDayComplete() {
        val cardType: String = cVKPay
        val amount: Int = cVKPayDayLimit
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(true, result)
    }


    @Test
    fun checkLimit_ByDay() {
        val cardType: String = "Мир"
        val amount: Int = cDayLimit+1
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(false, result)
    }
    @Test
    fun checkLimit_ByDayComplete() {
        val cardType: String = "Мир"
        val amount: Int = cDayLimit
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(true, result)
    }

    @Test
    fun checkLimit_VKPayByMonth() {
        val cardType: String = cVKPay
        val amount: Int = 10
        val previousAmount: Int = cVKPayMonthLimit

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(false, result)
    }
    @Test
    fun checkLimit_VKPayByMonthComplete() {
        val cardType: String = cVKPay
        val amount: Int = 10
        val previousAmount: Int = cVKPayMonthLimit - 10

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(true, result)
    }


    @Test
    fun checkLimit_ByMonth() {
        val cardType: String = "Мир"
        val amount: Int = 10
        val previousAmount: Int = cMonthLimit

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(false, result)
    }
    @Test
    fun checkLimit_ByMonthComplete() {
        val cardType: String = "Мир"
        val amount: Int = 10
        val previousAmount: Int = cMonthLimit-10

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertEquals(true, result)
    }

    @Test
    fun calcCommission_Default(){
        val amount: Int = cVKPayDayLimit

        val result = calcCommission(amount = amount)

        assertEquals(0F, result)
    }
}