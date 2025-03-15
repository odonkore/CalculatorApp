package com.example.calculatorapp
import android.util.Log
import net.objecthunter.exp4j.ExpressionBuilder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {

    private val historyList = mutableListOf<CalculationHistory>()
    private lateinit var tvHistory: TextView
    private lateinit var display: TextView
    private var currentInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference to the TextView (Display)
        display = findViewById(R.id.display)

        // Set up button click listeners
        setupButtonClicks()
       // val tvHistory: TextView = findViewById(R.id.tvHistory)
        tvHistory = findViewById(R.id.tvHistory)


    }

    private fun setupButtonClicks() {
        // Number buttons
        val numberButtons = listOf(
            findViewById<Button>(R.id.btn_0),
            findViewById(R.id.btn_1),
            findViewById(R.id.btn_2),
            findViewById(R.id.btn_3),
            findViewById(R.id.btn_4),
            findViewById(R.id.btn_5),
            findViewById(R.id.btn_6),
            findViewById(R.id.btn_7),
            findViewById(R.id.btn_8),
            findViewById(R.id.btn_9)
        )

        // Operators
        val operatorButtons = listOf(
            findViewById<Button>(R.id.btn_add),
            findViewById(R.id.btn_subtract),
            findViewById(R.id.btn_multiply),
            findViewById(R.id.btn_divide),
            findViewById(R.id.btn_percent)
        )

        // Other buttons
        val btnClear = findViewById<Button>(R.id.btn_clear)
        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnEquals = findViewById<Button>(R.id.btn_equals)
        val btnDot = findViewById<Button>(R.id.btn_dot)

        // Number button clicks
        numberButtons.forEach { button ->
            button.setOnClickListener {
                appendToDisplay(button.text.toString())
            }
        }

        // Operator button clicks
        operatorButtons.forEach { button ->
            button.setOnClickListener {
                appendToDisplay(" ${button.text} ")
            }
        }

        // Clear button click
        btnClear.setOnClickListener {
            clearDisplay()
        }

        // Back button click
        btnBack.setOnClickListener {
            removeLastCharacter()
        }

        // Dot button click
        btnDot.setOnClickListener {
            appendToDisplay(".")
        }

        // Equals button click
        btnEquals.setOnClickListener {
            calculateResult()
        }
    }

    private fun appendToDisplay(value: String) {
        currentInput += value
        display.text = currentInput
    }

    private fun clearDisplay() {
        currentInput = ""
        display.text = "0"
    }

    private fun removeLastCharacter() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            display.text = currentInput.ifEmpty { "0" }

        }
    }
// implementing actual logic
    private fun calculateResult() {
    try {
        val expressionText = display.text.toString().trim() // Get the full calculation
            .replace("×", "*")  // Replace '×' with '*'
            .replace("%", "/100")  // Convert percentage to decimal
        Log.d("CalculatorApp", "Expression: $expressionText") // Debug log for input

        if (expressionText.isEmpty()) return // Prevent empty evaluations

        val expression = ExpressionBuilder(expressionText).build() // Use expressionText here
        val result = expression.evaluate()

        // Convert result to readable format
        val resultText = if (result % 1 == 0.0) result.toInt().toString() else result.toString()

        // Format history entry to include both calculation and result
        //val calculationText = "$expressionText = $resultText"
        val calculation = CalculationHistory(display.text.toString(), resultText) // adds new calculations
        historyList.add(0, calculation)

        // Update history TextView
        tvHistory.text = historyList.joinToString("\n") { "${it.expression} = ${it.result}" }

        Log.d("CalculatorApp", "History: ${historyList.joinToString("\n")}") // Debug log for history

        // Show result in display
        display.text = resultText

        // Log errors if any
    } catch (e: Exception) {
        display.text = getString(R.string.error_message)
        Log.e("CalculatorApp", "Error evaluating expression", e) // Log errors if any
    }
    }


}
