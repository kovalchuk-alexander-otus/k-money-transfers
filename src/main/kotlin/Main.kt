fun main() {
    val commissionPercent: Float = 0.75F; // процент комисии с суммы перевода
    val commissionMin: Int = 35; // минимальная сумма комиссии

    /**
     * рассчет суммы комиссии по сумме перевода
     *
     * amount - сумма перевода
     */
    fun calcCommission(amount: Int): Int {
        val calc = (amount*commissionPercent/100).toInt();
        return if (calc > commissionMin) calc else commissionMin
    }

    println(calcCommission(43234))
}
