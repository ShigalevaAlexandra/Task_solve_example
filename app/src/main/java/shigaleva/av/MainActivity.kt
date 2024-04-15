package shigaleva.av

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var countRightAnswers = 0  //кол-во правильно решенных примеров
    private var countWrongAnswers = 0  //кол-во не правильно решенных примеров
    private var countAnswers = 0  //кол-во решенных примеров
    private var backgroundResult = 0  //сохранение значения цвета выделения результата

    private var firstNumberSave = 0
    private var secondNumberSave = 0
    private var operationSave = ""

    private var visibleBtnStart = true
    private var visibleBtnCheck = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Поиск элементов и запись их состояния в переменные
        val btnCheck = findViewById<Button>(R.id.btn_check)
        val btnStart = findViewById<Button>(R.id.btn_start)
        val editTxtResult = findViewById<EditText>(R.id.editText_result)

        //Обработка событий кнопок
        btnCheck.isEnabled = false  //изначально кнопка "проверить" недоступна

        //нажатие кнопки "старт"
        btnStart.setOnClickListener {
            visibleBtnStart = false
            it.isEnabled = visibleBtnStart
            visibleBtnCheck = true
            btnCheck.isEnabled = visibleBtnCheck
            editTxtResult.text.clear()
            editTxtResult.setBackgroundColor(Color.TRANSPARENT)
            backgroundResult = 0
            addNewExample()
        }

        //нажатие кнопки "проверка"
        btnCheck.setOnClickListener {
            visibleBtnCheck = false
            it.isEnabled = visibleBtnCheck
            visibleBtnStart = true
            btnStart.isEnabled = visibleBtnStart
            checkResult()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //функция создания нового примера
    private fun addNewExample() {
        val txtViewFirstNumber = findViewById<TextView>(R.id.txtView_first_number)
        val txtViewSecondNumber = findViewById<TextView>(R.id.txtView_second_number)
        val txtViewOperation = findViewById<TextView>(R.id.txtView_operation)

        val firstNumber = (10..99).random()
        var secondNumber = (10..99).random()

        //Если осуществляется операция деление, то делимое,
        //делитель и частное должны быть целыми числами
        while (firstNumber % secondNumber != 0) secondNumber = (10..99).random()

        txtViewFirstNumber.text = "$firstNumber"
        txtViewSecondNumber.text = "$secondNumber"

        firstNumberSave = firstNumber
        secondNumberSave = secondNumber

        when ((0..3).random()) {
            0 -> txtViewOperation.text = "+"
            1 -> txtViewOperation.text = "-"
            2 -> txtViewOperation.text = "*"
            3 -> txtViewOperation.text = "/"
        }

        operationSave = txtViewOperation.text.toString()
    }

    //функция проверки правильности решения примера
    private fun checkResult() {
        val txtViewFirstNumber = findViewById<TextView>(R.id.txtView_first_number)
        val txtViewSecondNumber = findViewById<TextView>(R.id.txtView_second_number)
        val txtViewOperation = findViewById<TextView>(R.id.txtView_operation)
        val editTxtResult = findViewById<EditText>(R.id.editText_result)
        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnCheck = findViewById<Button>(R.id.btn_check)

        val firstNumber = txtViewFirstNumber.text.toString().toInt()
        val secondNumber = txtViewSecondNumber.text.toString().toInt()
        val operation = txtViewOperation.text.toString()

        val result = editTxtResult.text.toString().toIntOrNull() ?: 0
        var rightAnswer = 0

        when (operation) {
            "+" -> rightAnswer = firstNumber + secondNumber
            "-" -> rightAnswer = firstNumber - secondNumber
            "*" -> rightAnswer = firstNumber * secondNumber
            "/" -> rightAnswer = firstNumber / secondNumber
        }

        if (result == rightAnswer) {
            editTxtResult.setBackgroundColor(Color.GREEN)
            backgroundResult = 5
            countRightAnswers++
        }
        else {
            editTxtResult.setBackgroundColor(Color.RED)
            backgroundResult = 2
            countWrongAnswers++
        }

        countAnswers++
        updateDataInElement()

        btnStart.isEnabled = visibleBtnStart
        btnCheck.isEnabled = visibleBtnCheck
    }

    //функция обновления данных в элементах активити
    @SuppressLint("SetTextI18n")
    private fun updateDataInElement() {
        val txtViewCountExample = findViewById<TextView>(R.id.txtView_count_example)
        val txtViewCountRightAnswer = findViewById<TextView>(R.id.txtView_count_right)
        val txtViewCountWrongAnswer = findViewById<TextView>(R.id.txtView_count_wrong)
        val txtViewProgress = findViewById<TextView>(R.id.txtView_progress)

        val txtViewFirstNumber = findViewById<TextView>(R.id.txtView_first_number)
        val txtViewSecondNumber = findViewById<TextView>(R.id.txtView_second_number)
        val txtViewOperation = findViewById<TextView>(R.id.txtView_operation)

        val editTxtResult = findViewById<EditText>(R.id.editText_result)

        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnCheck = findViewById<Button>(R.id.btn_check)

        btnStart.isEnabled = visibleBtnStart
        btnCheck.isEnabled = visibleBtnCheck

        txtViewCountExample.text = "$countAnswers"
        txtViewCountRightAnswer.text = "$countRightAnswers"
        txtViewCountWrongAnswer.text = "$countWrongAnswers"
        txtViewProgress.text = "${"%.2f".format((countRightAnswers * 100 / countAnswers.toDouble()))} %"

        txtViewFirstNumber.text = "$firstNumberSave"
        txtViewSecondNumber.text = "$secondNumberSave"
        txtViewOperation.text = "$operationSave"

        when (backgroundResult) {
            0 -> editTxtResult.setBackgroundColor(Color.TRANSPARENT)
            2 -> editTxtResult.setBackgroundColor(Color.RED)
            5 -> editTxtResult.setBackgroundColor(Color.GREEN)
        }
    }

    //сохранение значений переменных
    override fun onSaveInstanceState(instanceState: Bundle) {
        super.onSaveInstanceState(instanceState)
        instanceState.putInt("countAnswers", countAnswers)
        instanceState.putInt("countRightAnswers", countRightAnswers)
        instanceState.putInt("countWrongAnswers", countWrongAnswers)
        instanceState.putInt("firstNumberSave", firstNumberSave)
        instanceState.putInt("secondNumberSave", secondNumberSave)
        instanceState.putString("operationSave", operationSave)
        instanceState.putInt("backgroundResult", backgroundResult)
        instanceState.putBoolean("visibleBtnStart", visibleBtnStart)
        instanceState.putBoolean("visibleBtnCheck", visibleBtnCheck)
    }

    //использование сохраненных значений переменных
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        countAnswers = savedInstanceState.getInt("countAnswers")
        countRightAnswers = savedInstanceState.getInt("countRightAnswers")
        countWrongAnswers = savedInstanceState.getInt("countWrongAnswers")
        firstNumberSave = savedInstanceState.getInt("firstNumberSave")
        secondNumberSave = savedInstanceState.getInt("secondNumberSave")
        operationSave = savedInstanceState.getString("operationSave").toString()
        backgroundResult = savedInstanceState.getInt("backgroundResult")
        visibleBtnStart = savedInstanceState.getBoolean("visibleBtnStart")
        visibleBtnCheck = savedInstanceState.getBoolean("visibleBtnCheck")
        updateDataInElement()
    }
}
