package edu.iu.alex.calculatorapp
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayTextView = findViewById(R.id.tvScreen)

        initializeButtons()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentInput", currentInput)
        outState.putString("currentOperator", currentOperator)
        outState.putDouble("firstOperation", firstOperation)
        outState.putDouble("secondOperation", secondOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentInput = savedInstanceState.getString("currentInput", "")
        currentOperator = savedInstanceState.getString("currentOperator", "")
        firstOperation = savedInstanceState.getDouble("firstOperation", 0.0)
        secondOperation = savedInstanceState.getDouble("secondOperation", 0.0)
        updateDisplay()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            val trigonometricButtonIds = listOf(
                R.id.bSin, R.id.bCos, R.id.bTan
            )

            val logarithmicButtonIds = listOf(
                R.id.bLog10, R.id.bLn
            )

            val buttonsToNull = trigonometricButtonIds + logarithmicButtonIds

            for (buttonId in buttonsToNull) {
                var button = findViewById<Button>(buttonId)
                button = null
            }
        }
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
        val trigonometricButtonIds = listOf(
            R.id.bSin, R.id.bCos, R.id.bTan
        )

        val logarithmicButtonIds = listOf(
            R.id.bLog10, R.id.bLn
        )

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

        val allButtonIds = numberButtonIds + operatorButtonIds + otherButtonIds + trigonometricButtonIds + logarithmicButtonIds

        for (buttonId in allButtonIds) {
            var button = findViewById<Button>(buttonId)

            // Check if orientation is not landscape and set new buttons to null.
            if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE && (buttonId in trigonometricButtonIds || buttonId in logarithmicButtonIds)) {
                button = null
            } else {
                button.setOnClickListener {
                    val buttonText = button.text.toString()
                    onButtonPress(buttonText)
                }
            }
        }
    }

    //buttonPress controller function
    private fun onButtonPress(buttonText: String) {
        when {
            buttonText in "0123456789" -> {
                appendDigit(buttonText)
                Log.d("CalculatorApp", "Digit $buttonText pressed")
            }
            buttonText in "+-X/%" -> {
                setOperator(buttonText)
                Log.d("CalculatorApp", "Operator $buttonText pressed")
            }
            buttonText == "=" -> {
                performCalculation()
                Log.d("CalculatorApp", "Equal key pressed")
            }
            buttonText == "." -> {
                appendDecimal()
                Log.d("CalculatorApp", "Period key pressed")
            }
            buttonText == "C" -> {
                clearDisplay()
                Log.d("CalculatorApp", "Clear key pressed")
            }
            buttonText == "+/-" -> {
                toggleSign()
                Log.d("CalculatorApp", "Pos/Neg key pressed")
            }
            buttonText in listOf("sin", "cos", "tan", "log", "ln") -> {
                setOperator(buttonText)
                Log.d("CalculatorApp", "Operator $buttonText pressed")
            }
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
                "sin" -> Math.sin(secondOperation).toString()
                "cos" -> Math.cos(secondOperation).toString()
                "tan" -> Math.tan(secondOperation).toString()
                "log" -> Math.log10(secondOperation).toString()
                "ln" -> Math.log(secondOperation).toString()
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

    //Calls upon performCalculation based upon what action is desired, then resets input
    private fun setOperator(operator: String){
        if (currentOperator.isNotEmpty()) {
            performCalculation()
        }
        firstOperation = currentInput.toDouble()
        currentInput = ""
        currentOperator = operator
    }



}
