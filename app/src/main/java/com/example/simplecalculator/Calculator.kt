package com.example.simplecalculator

/**
 * 演算担当クラス
 */
class Calculator {
    /**
     * 左項
     */
    private var leftValue: UInt = 0u

    /**
     * 演算子
     */
    private var operator: Operator? = null

    /**
     * 右項
     */
    private var rightValue: UInt = 0u

    /**
     * 現在の数式の更新位置
     */
    private var state: State = State.INITIAL

    private lateinit var listener: Listener

    private enum class State {
        INITIAL,
        LEFT_VALUE,
        OPERATOR,
        RIGHT_VALUE,
        RESULT,
    }

    enum class Operator(val text: String) {
        PLUS("+"),
        MINUS("-"),
        MULTI("×"),
        DIV("÷"),
    }

    fun inputNumber(number: Int) {
        if (number in 0..9) {
            // 1桁の数値以外は入力を受け付けない
            listener.onError("enable only 1 digit")
        } else {
            return
        }

        when(state) {
            State.INITIAL -> {
                leftValue = number.toUInt()
                state = State.LEFT_VALUE
            }
            State.LEFT_VALUE ->  {
                // 桁上がり対策
                val value =  (leftValue * 10u) + number.toUInt()
                if (value > leftValue) {
                    leftValue = value
                }
            }
            State.OPERATOR -> {
                rightValue = number.toUInt()
                state = State.RIGHT_VALUE
            }
            State.RIGHT_VALUE -> {
                // 桁上がり対策
                val value = (rightValue * 10u) + number.toUInt()
                if (value > rightValue) {
                    rightValue = value
                }
            }
            else -> { listener.onError("no operation. calculate is finished") }
        }

        notifyResult()
    }

    fun inputOperator(operator: Operator) {
        when(state) {
            State.LEFT_VALUE, State.OPERATOR -> {
                this.operator = operator
                state = State.OPERATOR
            }
            else -> { listener.onError("no operation. input number") }
        }

        notifyResult()
    }

    fun inputEqual() {
        when(state) {
            State.RIGHT_VALUE -> {
                state = State.RESULT
            }
            else -> { listener.onError("no operation. formula is not complete") }
        }

        notifyResult()
    }

    fun inputClear() {
        leftValue = 0u
        operator = null
        rightValue = 0u

        state = State.INITIAL

        notifyResult()
    }

    fun setResultListener(listener: Listener) {
        this.listener = listener
    }

    private fun notifyResult() {
        listener.onResult(resultText())
    }

    private fun resultText(): Result {
        return when (state) {
            State.INITIAL -> Result("", "0")
            State.LEFT_VALUE -> Result(leftValue.toString(), "0")
            State.OPERATOR -> Result(leftValue.toString() + operator?.text, "0")
            State.RIGHT_VALUE -> Result(leftValue.toString() + operator?.text + rightValue.toString(), "0")
            State.RESULT -> Result(leftValue.toString() + operator?.text + rightValue.toString(), calculateString())
        }
    }

    private fun calculateString(): String {
        return try {
            when(operator) {
                Operator.PLUS  -> (leftValue.toDouble() + rightValue.toDouble()).toString()
                Operator.MINUS -> (leftValue.toDouble() - rightValue.toDouble()).toString()
                Operator.MULTI -> (leftValue.toDouble() * rightValue.toDouble()).toString()
                Operator.DIV -> (leftValue.toDouble() / rightValue.toDouble()).toString()
                else -> ""
            }
        } catch (e: ArithmeticException) {
            ""
        }
    }

    interface Listener {
        fun onResult(result: Result)

        fun onError(result: String)
    }
}

data class Result(val formula: String, val result: String)