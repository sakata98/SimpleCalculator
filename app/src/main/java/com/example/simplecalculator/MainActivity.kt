package com.example.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    /**
     * 数式表示領域
     */
    private lateinit var formulaText: TextView

    /**
     * 結果表示領域
     */
    private lateinit var resultText: TextView

    private val calculator = Calculator()

    private val listener = View.OnClickListener { view ->
        when (view.id) {
            R.id.button0 -> calculator.inputNumber(0)
            R.id.button1 -> calculator.inputNumber(1)
            R.id.button2 -> calculator.inputNumber(2)
            R.id.button3 -> calculator.inputNumber(3)
            R.id.button4 -> calculator.inputNumber(4)
            R.id.button5 -> calculator.inputNumber(5)
            R.id.button6 -> calculator.inputNumber(6)
            R.id.button7 -> calculator.inputNumber(7)
            R.id.button8 -> calculator.inputNumber(8)
            R.id.button9 -> calculator.inputNumber(9)
            R.id.button_plus -> calculator.inputOperator(Calculator.Operator.PLUS)
            R.id.button_minus -> calculator.inputOperator(Calculator.Operator.MINUS)
            R.id.button_multi -> calculator.inputOperator(Calculator.Operator.MULTI)
            R.id.button_div -> calculator.inputOperator(Calculator.Operator.DIV)
            R.id.button_equal -> calculator.inputEqual()
            R.id.button_clear -> calculator.inputClear()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
    }

    private fun initialize() {
        listOf<Button>(
            findViewById(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9),
            findViewById(R.id.button_plus),
            findViewById(R.id.button_minus),
            findViewById(R.id.button_multi),
            findViewById(R.id.button_div),
            findViewById(R.id.button_equal),
            findViewById(R.id.button_clear)
        ).map {
            it.setOnClickListener(listener)
        }

        formulaText = findViewById(R.id.formula)
        resultText = findViewById(R.id.result)

        calculator.setResultListener(object : Calculator.Listener {
            override fun onResult(result: Result) {
                formulaText.text = result.formula
                resultText.text = result.result
            }

            override fun onError(result: String) {
                // ログ出力のみ。
                Log.e("TAG", result)
            }
        })
    }
}