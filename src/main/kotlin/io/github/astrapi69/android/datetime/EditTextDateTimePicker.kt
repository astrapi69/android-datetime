/**
 * The MIT License
 *
 * Copyright (C) 2021 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.android.datetime

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

public class EditTextDateTimePicker @JvmOverloads constructor(
    private val editText: EditText,
    ctx: Context,
    dateFormat: String,
    setToday: Boolean = true
) : View.OnClickListener, OnDateSetListener {
    private val myCalendar: Calendar
    private var ctx: Context
    var dateFormat: String
    private var simpleDateFormat: SimpleDateFormat
    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        myCalendar[Calendar.YEAR] = year
        myCalendar[Calendar.MONTH] = monthOfYear
        myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        setEditText()
    }

    init {
        editText.setOnClickListener(this)
        myCalendar = Calendar.getInstance()
        this.ctx = ctx
        this.dateFormat = dateFormat
        simpleDateFormat = SimpleDateFormat(this.dateFormat)
        if (setToday) {
            setEditText()
        }
    }

    protected fun setEditText() {
        editText.setText(simpleDateFormat.format(myCalendar.time))
    }

    override fun onClick(v: View) {
        val datePickerDialog = DatePickerDialog(
            ctx,
            { datePicker, year, monthOfYear, dayOfMonth ->
                myCalendar.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    ctx,
                    { view, hourOfDay, minute ->
                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        myCalendar.set(Calendar.MINUTE, minute)
                        setEditText()
                    }, myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE], true
                ).show()
            }, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DATE]
        )
        datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
        datePickerDialog.show()
    }
}
