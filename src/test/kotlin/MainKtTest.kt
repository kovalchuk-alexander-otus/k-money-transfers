import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

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
        val amount: Int = cVKPayDayLimit + 1
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertFalse(result)
    }

    @Test
    fun checkLimit_VKPayByDayComplete() {
        val cardType: String = cVKPay
        val amount: Int = cVKPayDayLimit
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertTrue(result)
    }


    @Test
    fun checkLimit_ByDay() {
        val cardType: String = "Мир"
        val amount: Int = cDayLimit + 1
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertFalse(result)
    }

    @Test
    fun checkLimit_ByDayComplete() {
        val cardType: String = "Мир"
        val amount: Int = cDayLimit
        val previousAmount: Int = 0

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertTrue(result)
    }

    @Test
    fun checkLimit_VKPayByMonth() {
        val cardType: String = cVKPay
        val amount: Int = 10
        val previousAmount: Int = cVKPayMonthLimit

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertFalse(result)
    }

    @Test
    fun checkLimit_VKPayByMonthComplete() {
        val cardType: String = cVKPay
        val amount: Int = 10
        val previousAmount: Int = cVKPayMonthLimit - 10

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertTrue(result)
    }


    @Test
    fun checkLimit_ByMonth() {
        val cardType: String = "Мир"
        val amount: Int = 10
        val previousAmount: Int = cMonthLimit

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertFalse(result)
    }

    @Test
    fun checkLimit_ByMonthComplete() {
        val cardType: String = "Мир"
        val amount: Int = 10
        val previousAmount: Int = cMonthLimit - 10

        val result: Boolean = checkLimit(cardType, previousAmount, amount)

        assertTrue(result)
    }

    @Test
    fun calcCommission_Default() {
        val amount: Int = cVKPayDayLimit

        val result = calcCommission(amount = amount)

        assertEquals(0F, result)
    }

    @ParameterizedTest
    @MethodSource("provide")
    fun calcCommission_Options(cardType: String, previousAmount: Int, amount: Int, expected: Float) {
        val result = calcCommission(
            cardType,
            previousAmount,
            amount
        ); // cardType: String = cVKPay, previousAmount: Int = 0, amount: Int)

        assertEquals(expected, result)
    }

    companion object {
        @JvmStatic
        fun provide(): Stream<Arguments> {
            return Stream.of(
                // кейс #1 : трат не было - первое движение размером в 1_000
                Arguments.of("VK Pay", 0, 1_000, 0F),
                Arguments.of("Mastercard", 0, 1_000, 0F),
                Arguments.of("Maestro", 0, 1_000, 0F),
                Arguments.of("Visa", 0, 1_000, 35F),
                Arguments.of("Мир", 0, 1_000, 35F),
                // кейс #2 : траты были размером в 75_000 + делаем движение в 1_000
                Arguments.of("VK Pay", commissionFreeLimit, 1_000, -1F),
                Arguments.of("Mastercard", commissionFreeLimit, 1_000, 26F),
                Arguments.of("Maestro", commissionFreeLimit, 1_000, 26F),
                Arguments.of("Visa", commissionFreeLimit, 1_000, 35F),
                Arguments.of("Мир", commissionFreeLimit, 1_000, 35F),
                // кейс #3 : траты были в районе 75_000 + делаем движение, чтобы достич 75_000
                Arguments.of("VK Pay", commissionFreeLimit-1_000, 1_000, -1F),
                Arguments.of("Mastercard", commissionFreeLimit-1_000, 1_000, 0F),
                Arguments.of("Maestro", commissionFreeLimit-1_000, 1_000, 0F),
                Arguments.of("Visa", commissionFreeLimit-1_000, 1_000, 35F),
                Arguments.of("Мир", commissionFreeLimit-1_000, 1_000, 35F),
                // кейс #4 : траты были в районе 75_000 + движение, которое не превысило 75_000
                Arguments.of("VK Pay", commissionFreeLimit-1_000, 900, -1F),
                Arguments.of("Mastercard", commissionFreeLimit-1_000, 900, 0F),
                Arguments.of("Maestro", commissionFreeLimit-1_000, 900, 0F),
                Arguments.of("Visa", commissionFreeLimit-1_000, 900, 35F),
                Arguments.of("Мир", commissionFreeLimit-1_000, 900, 35F),
                // кейс #5 : траты были в районе 75_000 + движение, которое превысило 75_000
                Arguments.of("VK Pay", commissionFreeLimit-1_000, 5_000, -1F),
                Arguments.of("Mastercard", commissionFreeLimit-1_000, 5_000, 44F),
                Arguments.of("Maestro", commissionFreeLimit-1_000, 5_000, 44F),
                Arguments.of("Visa", commissionFreeLimit-1_000, 5_000, 37.5F),
                Arguments.of("Мир", commissionFreeLimit-1_000, 5_000, 37.5F),
                // кейс #6 : траты были больше 75_000 + движение 5_000
                Arguments.of("VK Pay", commissionFreeLimit+1, 5_000, -1F),
                Arguments.of("Mastercard", commissionFreeLimit+1, 5_000, 50F),
                Arguments.of("Maestro", commissionFreeLimit+1, 5_000, 50F),
                Arguments.of("Visa", commissionFreeLimit+1, 5_000, 37.5F),
                Arguments.of("Мир", commissionFreeLimit+1, 5_000, 37.5F),
                // кейс #7 : траты были больше 75_000 + движение больше дневного лимита
                Arguments.of("VK Pay", commissionFreeLimit+1, cDayLimit+1, -1F),
                Arguments.of("Mastercard", commissionFreeLimit+1, cDayLimit+1, -1F),
                Arguments.of("Maestro", commissionFreeLimit+1, cDayLimit+1, -1F),
                Arguments.of("Visa", commissionFreeLimit+1, cDayLimit+1, -1F),
                Arguments.of("Мир", commissionFreeLimit+1, cDayLimit+1, -1F),
                // кейс #8 : траты были мелкие 1_000 + движение больше дневного лимита
                Arguments.of("VK Pay", 1_000, cDayLimit, -1F),
                Arguments.of("Mastercard", 1_000, cDayLimit, 476F),
                Arguments.of("Maestro", 1_000, cDayLimit, 476F),
                Arguments.of("Visa", 1_000, cDayLimit, 1125F),
                Arguments.of("Мир", 1_000, cDayLimit, 1125F),


                // TODO: а вдруг появятся карты, о которых не знает программа
                Arguments.of("Подсолнух", commissionFreeLimit-1_000, 900, 35F),
                Arguments.of("Подсолнух", commissionFreeLimit+1, 5_000, 57.6F)
            );
        }
    }

}