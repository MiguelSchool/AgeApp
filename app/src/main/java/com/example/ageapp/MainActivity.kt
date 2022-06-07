package com.example.ageapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private val configuration : Configuration = Configuration()
    private val wrapper : ContextThemeWrapper = ContextThemeWrapper()
    @RequiresApi(Build.VERSION_CODES.O)
    private var dateSelectedDateFormat : LocalDateTime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)

    private var isEnglish = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Locale.setDefault(Locale.ENGLISH)
        setToggleText()

        btnSelected_birthday.setOnClickListener {
           onClickDatePicker(it)
           // Toast.makeText(this, "toast geht", Toast.LENGTH_LONG).show()
        }

        btnToggleLanguage.setOnClickListener {
            setToggleText()
        }
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private val onClickDatePicker: (View ) -> Unit = {
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val dayOfMonth = myCalender.get(Calendar.DAY_OF_MONTH)

           DatePickerDialog(this, {
                   it, year, month, dayOfMonth ->
               val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMANY)

               dateSelectedDateFormat = LocalDateTime.of( year,(month + 1), dayOfMonth, 0,0,0 )

               val birthday = dateTimeFormatter.format(dateSelectedDateFormat)
               showSelectedBirthday.text =birthday
               showSelectedBirthday.gravity = Gravity.CENTER
               showResult.text = getMinutes(birthday)
               showResult.gravity = Gravity.CENTER
               showResult.textSize = 25f
           }, year, month, dayOfMonth).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val setToggleText = {
        if( isEnglish ){
            btnToggleLanguage.setText(R.string.english)
        }else {
            btnToggleLanguage.setText(R.string.german)
            configuration.setLocale(Locale("de"))
        }
        wrapper.applyOverrideConfiguration(configuration)
    }

    private val getMinutes : (String) -> String = { selectedDate :String ->
            var message = ""
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
            val birthday = simpleDateFormat.parse(selectedDate)
            val currentDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()))
        if (birthday!!.before(currentDate)) {
            val birthdayInMinutes = birthday.time / 60_000
            val currentDayInMinutes = currentDate!!.time / 60_000
            val differenceInMinutes = currentDayInMinutes - birthdayInMinutes
            message = ( differenceInMinutes ).toString()


        }else {
            Toast.makeText(this,R.string.alertErrBirthdayMessage, Toast.LENGTH_LONG).show()
        }

        message
    }
}
