import kotlin.math.roundToInt
import kotlin.random.Random

fun main() {
    val commissionPercent = 0.75F // процент комисии с суммы перевода
    val commissionMin = 35 // минимальная сумма комиссии
    val cVKPay = "VK Pay"
    val cardTypes = arrayOf("VK Pay", "Mastercard", "Maestro", "Visa", "Мир")
    val cardTypesWithCommission = arrayOf("Visa", "Мир")
    val cardTypesNoCommission = arrayOf("Mastercard", "Maestro")
    val iDayLimit = "Превышен лимит переводов в сутки. Разрешенная сумма %d, сумма операции %d"
    val iMonthLimit = "Превышен лимит переводов в месяц. Разрешенная сумма %d, сумма операции %d"

    /**
     * расчет суммы комиссии (базовый)
     */
    fun calcCommissionBase(
        amount: Int,
        commission: Float,
        commissionMin: Float = 0F,
        commissionPlus: Float = 0F
    ): Float {
        val calc = amount * commission / 100
        return if (calc > commissionMin) calc else commissionMin
    }

    /**
     * контроль лимитов
     */
    fun checkLimit(cardType: String, previousAmount: Int, amount: Int): Boolean {
        when {
            cVKPay.equals(cardType) && amount > 15_000 -> {
                println(String.format(iDayLimit, 15_000, amount))
                return false
            }

            cVKPay.equals(cardType) && previousAmount > 40_000 -> {
                println(String.format(iMonthLimit, 40_000, previousAmount))
                return false
            }

            !cVKPay.equals(cardType) && amount > 150_000 -> {
                println(String.format(iDayLimit, 150_000, amount))
                return false
            }

            !cVKPay.equals(cardType) && previousAmount > 600_000 -> {
                println(String.format(iMonthLimit, 600_000, previousAmount))
                return false
            }
        }
        return true
    }

    /**
     * рассчет суммы комиссии по сумме перевода
     *
     * cardType - тип карты/счёта (по умолчанию VK Pay);
     * previousAmount - сумму предыдущих переводов в этом месяце (по умолчанию 0 рублей);
     * amount - сумма совершаемого перевода;
     */
    fun calcCommission(cardType: String = cVKPay, previousAmount: Int = 0, amount: Int): Double {
        if (checkLimit(cardType, previousAmount, amount)) {

            val fullAmount = previousAmount + amount;
            val commission: Float = when {
                cVKPay.equals(cardType) -> 0F
                cardTypesWithCommission.indexOf(cardType) != -1 -> calcCommissionBase(amount, 0.75F, 35F)
                cardTypesNoCommission.indexOf(cardType) != -1 && fullAmount < 75_000 -> 0F
                else -> calcCommissionBase(amount, 0.60F, commissionPlus = 20F)
            }
            return (commission * 100.0).roundToInt() / 100.0
        }
        return (-1).toDouble()
    }

    // отладка
    //  VK Pay - default
    for (i in 1..10) {
        var a = Random.nextInt(1, 50_000);
        println("За перевод суммы ${a} будет взята комиссия ${calcCommission(amount = a)}")
    }

    // random card & others
    for (i in 1..100) {
        val card = cardTypes[Random.nextInt(0, cardTypes.size)]
        val amount = Random.nextInt(1, 200_000)
        val previous = Random.nextInt(0, 1000_000)
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
