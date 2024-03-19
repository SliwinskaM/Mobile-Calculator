package com.example.malgorzata_sliwinska_czw_9_30

import java.lang.Math.sqrt


class CalculatorEngine(): CalculatorEngineInterface {
    var numbers = mutableListOf<Double>()
    var operations = mutableListOf<String>()

    override fun addNumber(number: Double) {
        numbers.add(number)
    }

    override fun addOperation(operation: String) {
        operations.add(operation)
    }


    private fun evaluatePartial(operator: String, a: Double, b: Double): Double {
        when(operator) {
            "+" -> return a + b
            "-" -> return a - b
            "*" -> return a * b
            "/" -> return a / b
        }
        throw Error()
    }

    private fun evaluateSingle() {
        var operation: String
        var a: Double
        var aIdx: Int
        // wyszukanie wystąpienia operatora
        var operationIdx: Int = operations.indexOfFirst { e -> e.equals("v") or e.equals("%") } //jeśli nie znajdzie: -1
        while (operationIdx >= 0) {
            // wyszukanie liczb podlegających danemu operatorowi
            aIdx = operationIdx
            //usunięcie operacji z listy
            operation = operations.removeAt(operationIdx)
            //edycja listy liczb
            a = numbers[aIdx]
            if (operation == "v") {
                numbers[aIdx] = sqrt(a)
            } else if (operation == "%") {
                numbers[aIdx] = a / 100
            }
            //powtórne wyszukanie
            operationIdx = operations.indexOfFirst { e -> e.equals("v") or e.equals("%") }
        }
    }

    private fun evaluateOperationType(op1: String, op2: String) {
        var a: Double
        var b: Double
        var aIdx: Int
        var bIdx: Int
        var operation: String
        // Wyszukanie danych operatorów (w pętli)
        var operationIdx: Int = operations.indexOfFirst { e -> e.equals(op1) or e.equals(op2) } //jeśli nie znajdzie: -1
        while (operationIdx >= 0) {
            // wyszukanie liczb podlegających danemu operatorowi
            aIdx = operationIdx
            bIdx = aIdx + 1
            //usunięcie operacji z listy
            operation = operations.removeAt(operationIdx)
            //edycja listy liczb
            b = numbers.removeAt(bIdx)
            a = numbers[aIdx]
            numbers[aIdx] = evaluatePartial(operation, a, b)
            //powtórne wyszukanie
            operationIdx = operations.indexOfFirst { e -> e.equals(op1) or e.equals(op2) }
        }
    }


    override fun evaluate(): Double {
        while (numbers.isNotEmpty() && operations.isNotEmpty()) {
            evaluateSingle()
            evaluateOperationType("*", "/")
            evaluateOperationType("+", "-")
        }
        return numbers.removeAt(numbers.size - 1)
    }
}
