import kotlin.math.roundToInt
import kotlin.random.Random

fun main() {
    val commissionPercent = 0.75F // процент комисии с суммы перевода
    val commissionMin = 35 // минимальная сумма комиссии
    val cVKPay = "VK Pay"
    val cardTypes = arrayOf("VK Pay", "Mastercard", "Maestro", "Visa", "Мир")
    val cardTypesWithCommission = arrayOf("Visa", "Мир")
    val cardTypesNoCommission = arrayOf("Mastercard", "Maestro")

    fun calcCommissionBase(amount: Int, commission: Float, commissionMin: Float): Float {
        val calc = amount * commission
        return if (calc > commissionMin) calc else commissionMin
    }

    /**
     * рассчет суммы комиссии по сумме перевода
     *
     * cardType - тип карты/счёта (по умолчанию VK Pay);
     * previousAmount - сумму предыдущих переводов в этом месяце (по умолчанию 0 рублей);
     * amount - сумма совершаемого перевода;
     */
    fun calcCommission(cardType: String = cVKPay, previousAmount: Int = 0, amount: Int): Double {
        val fullAmount = previousAmount + amount;
        val commission: Float = when {
            cVKPay.equals(cardType) -> 0F
            cardTypesWithCommission.indexOf(cardType) != -1 -> calcCommissionBase(amount, 0.75F, 35F)
            cardTypesNoCommission.indexOf(cardType) != -1 && fullAmount < 75_000 -> 0F
            else -> calcCommissionBase(amount, 0.6F, 20F)
        }
        return (commission * 100.0).roundToInt() / 100.0
    }

    // отладка
    //  VK Pay - default
    for (i in 1..10) {
        var a = Random.nextInt(1, 50_000);
        println("За перевод суммы ${a} будет взята комиссия ${calcCommission(amount = a)}")
    }

    // random card & others
    for (i in 1..100) {
        val card = cardTypes[Random.nextInt(0, cardTypes.size - 1)]
        val amount = Random.nextInt(1, 100_000)
        val previous = Random.nextInt(0, 50_000)
        println(
            "За перевод суммы ${amount} по карте ${card} (с учетом ранее совершенных переводов на сумму ${previous}) будет взята комиссия ${
                calcCommission(
                    card,
                    previous,
                    amount
                )
            }"
        )
    }
}
