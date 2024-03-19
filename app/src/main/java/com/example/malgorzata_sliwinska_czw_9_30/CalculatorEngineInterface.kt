package com.example.malgorzata_sliwinska_czw_9_30

import java.math.BigDecimal

interface CalculatorEngineInterface {
    fun addNumber(number: Double)
    fun addOperation(operation: String)
    fun evaluate(): Double

}