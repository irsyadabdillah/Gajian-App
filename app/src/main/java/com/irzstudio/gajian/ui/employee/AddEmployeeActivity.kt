package com.irzstudio.gajian.ui.employee

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import com.irzstudio.gajian.databinding.ActivityAddEmployeeBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEmployeeActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private val binding : ActivityAddEmployeeBinding by lazy {
        ActivityAddEmployeeBinding.inflate(layoutInflater)
    }

    private var calendar = Calendar.getInstance()
    private var calStartTime = Calendar.getInstance()
    private var calEndTime = Calendar.getInstance()

    private var TAG_TIME = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navigationBack()
        initTime()
    }

    private fun navigationBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initTime() {
        binding.txtStartTime.setOnClickListener {
            val hour = calStartTime.get(Calendar.HOUR_OF_DAY)
            val minute = calStartTime.get(Calendar.MINUTE)
            TimePickerDialog(this, this, hour, minute, true).show()
            TAG_TIME = 1
        }

        binding.txtEndTime.setOnClickListener {
            val hour = calEndTime.get(Calendar.HOUR_OF_DAY)
            val minute = calEndTime.get(Calendar.MINUTE)
            TimePickerDialog(this, this, hour, minute, true).show()
            TAG_TIME = 2
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calStartTime.set(Calendar.MINUTE, minute)
        calEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calEndTime.set(Calendar.MINUTE, minute)
        val sdf = SimpleDateFormat("HH:mm")
        val startTime = sdf.format(calStartTime.time)
        val endTime = sdf.format(calEndTime.time)

        if(TAG_TIME == 1) binding.txtStartTime.setText(startTime) else binding.txtEndTime.setText(endTime)
    }

}