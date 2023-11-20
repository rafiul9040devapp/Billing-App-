package com.rafiul.billingapp.utils

fun calculateTotalTip(totalBill: Double, tipPercentage: Float): Double {
    val tip = (totalBill * tipPercentage)
    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) tip else 0.0
}

fun calculateTotalPerPerson(
    totalBill: Double,
    tipPercentage: Float,
    splitAmongPerson: Int
): Double {
    val bill = calculateTotalTip(totalBill, tipPercentage) + totalBill
    return (bill / splitAmongPerson)
}