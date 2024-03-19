// Małgorzata Śliwińska
// Zrealizowane punkty: 1a,b,c; 2a,bii; 3a,b,c; 4; 5; 6; 8; 9

// Działanie pierwiastka: Należy go nacisnąć po liczbie do spierwiastkowania

package com.example.malgorzata_sliwinska_czw_9_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var clearButton: Button
    private lateinit var equalsButton: Button
    private lateinit var decimalButton: Button
    private lateinit var digitsButtons: List<Button>
    private lateinit var operationsButtons: List<Button>
    private lateinit var calculatorDisplay: TextView

    private var beginning: Boolean = true //zabezpieczenie przed użyciem operatora lub "=" na początku
    private var equalsPressed: Boolean = false //zabezpieczenie przed wielokrotnym nacisnięciem "="
    private var usedDecimal: Boolean = false //zabezpieczenie przed złym użyciem separatora

    private var currentOperation: String = ""
    private var currentNumber: String = ""
        set(value) {
            if (value == "") {
                setDisplay("0")
                usedDecimal = false
            } else {
                setDisplay(value)
            }
            field = value
        }

    private val calcEngine: CalculatorEngineInterface = CalculatorEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearButton = findViewById(R.id.buttonC)
        decimalButton = findViewById(R.id.buttonDecimal)
        equalsButton = findViewById(R.id.buttonEquals)
        calculatorDisplay = findViewById(R.id.calculatorDisplay)

        val buttonIDs = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
        )
        val operationsIDs =
            listOf(R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMul, R.id.buttonDiv, R.id.buttonSqrt, R.id.buttonPerc)

        digitsButtons = (buttonIDs.map { id -> findViewById<Button>(id) }).toList()
        operationsButtons = (operationsIDs.map { id -> findViewById<Button>(id) }).toList()

        digitsButtons.forEach { button -> button.setOnClickListener { i -> digitPressed(i as Button) } }
        operationsButtons.forEach { button -> button.setOnClickListener { i -> operationPressed(i as Button) } }
        decimalButton.setOnClickListener { decimalPressed() }
        equalsButton.setOnClickListener { equalsPressed() }
        clearButton.setOnClickListener { clear() }
    }

    private fun digitPressed(button: Button) {
        beginning = false
        equalsPressed = false
        confirmOperation()
        currentNumber = currentNumber.plus(button.text.toString())
    }

    private fun operationPressed(button: Button) {
        beginning = false
        equalsPressed = false
        confirmNumber()
        currentOperation = button.text.toString() //wielokrotne użycie przycisku powoduje nadpisanie aktualnej operacji
        //zapisywanie operatorów dla pojedynczych liczb
        if ((currentOperation == getString(R.string.sqrt)) or (currentOperation == getString(R.string.percent))) {
            confirmOperation()
        }
    }

    private fun decimalPressed() {
        beginning = false
        equalsPressed = false
        if (!usedDecimal) {
            currentNumber = if (currentNumber.isNotEmpty()) {
                currentNumber.plus(getString(R.string.decimal))
            } else {
                "0."
            }
            usedDecimal = true
        }
    }

    private fun equalsPressed() {
        if (!beginning and !equalsPressed) {
            confirmNumber()
            confirmOperation()
            var result = calcEngine.evaluate()
            setDisplay(result.toString())
            equalsPressed = true
            beginning = true
        }
    }

    private fun clear() {
        currentNumber = ""
        currentOperation = ""
    }

    private fun setDisplay(value: String) {
        val len = value.length
        if (len <= 10) {
            calculatorDisplay.text = value
        } else {
            calculatorDisplay.text = "..." + value.substring(len - 10)
        }
    }

    //dodanie liczby do listy numbers w calcEngine
    private fun confirmNumber() {
        if (currentNumber.isNotEmpty()) {
            calcEngine.addNumber(currentNumber.toDouble())
            currentNumber = ""
        }
    }

    //dodanie operacji do listy operations w calcEngine
    private fun confirmOperation() {
        when(currentOperation) {
            getString(R.string.add) -> calcEngine.addOperation("+")
            getString(R.string.subtract) -> calcEngine.addOperation("-")
            getString(R.string.multiply) -> calcEngine.addOperation("*")
            getString(R.string.divide) -> calcEngine.addOperation("/")
            getString(R.string.sqrt) -> calcEngine.addOperation("v")
            getString(R.string.percent) -> calcEngine.addOperation("%")
        }
        currentOperation = ""
    }
}