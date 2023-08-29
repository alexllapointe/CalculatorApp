package edu.iu.alex.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    //TextView Declaration
    private lateinit var displayTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayTextView = findViewById<TextView>(R.id.tvScreen)
        initializeButtons()
    }

    private var currentInput = ""
    private var currentOperator = ""
    private var firstOperation = 0.0
    private var secondOperation = 0.0

    //Display Methods

    private fun updateDisplay(){
        displayTextView.text = currentInput

    }

    private fun clearDisplay(){
        currentInput = ""
        currentOperator = ""
        firstOperation = 0.0
        secondOperation = 0.0
        updateDisplay()
    }

    //Button Manipulation

    //button init
    private fun initializeButtons() {
        val numberButtonIds = listOf(
            R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4,
            R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9
        )

        val operatorButtonIds = listOf(
            R.id.bPlus, R.id.bMinus, R.id.bMultiply, R.id.bDivide, R.id.bModulo
        )
        val otherButtonIds = listOf(
            R.id.bC, R.id.bPosNeg, R.id.bPoint, R.id.bEquals
        )

        val allButtonIds = numberButtonIds + operatorButtonIds + otherButtonIds

        for (buttonId in allButtonIds) {
            val button = findViewById<Button>(buttonId)

            button.setOnClickListener {
                val buttonText = button.text.toString()
                onButtonPress(buttonText)
            }
        }
    }

    //buttonPress controller function
    private fun onButtonPress(buttonText: String) {
        when {
            buttonText in "0123456789" -> appendDigit(buttonText)
            buttonText in "+-X/%" -> setOperator(buttonText)
            buttonText == "=" -> performCalculation()
            buttonText == "." -> appendDecimal()
            buttonText == "C" -> clearDisplay()
            buttonText == "+/-" -> toggleSign()

        }
    }

    //Calculation Methods

    //Switch statement used to calculate based on sign
    private fun performCalculation() {
        if (currentInput.isNotEmpty() && currentOperator.isNotEmpty()) {
            secondOperation = currentInput.toDouble()
            currentInput = when (currentOperator) {
                "+" -> (firstOperation + secondOperation).toString()
                "-" -> (firstOperation - secondOperation).toString()
                "X" -> (firstOperation * secondOperation).toString()
                "/" -> (firstOperation / secondOperation).toString()
                "%" -> (firstOperation % secondOperation).toString()
                else -> currentInput
            }
            currentOperator = ""
            firstOperation = currentInput.toDouble()
            updateDisplay()
        }
    }

    //Helper Calculation Methods

    //Appends decimal to end of currentInput when called, refreshes display
    private fun appendDecimal() {
        if ("." !in currentInput) {
            currentInput += "."
            updateDisplay()
        }
    }

    //Adds +/- sign to beginning of number when called
    private fun toggleSign() {
        if (currentInput.isNotEmpty() && currentInput.toDouble() != 0.0) {
            currentInput = (-currentInput.toDouble()).toString()
            updateDisplay()
        }
    }

    //Adds desired number to text view when called
    private fun appendDigit(digit: String){
        currentInput += digit
        updateDisplay()
    }

    //Calls upon performCalulation based upon what action is desired, then resets input
    private fun setOperator(operator: String){
        if (currentOperator.isNotEmpty()) {
            performCalculation()
        }
        firstOperation = currentInput.toDouble()
        currentInput = ""
        currentOperator = operator
    }



}