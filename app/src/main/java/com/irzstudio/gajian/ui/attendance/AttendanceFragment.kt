package com.irzstudio.gajian.ui.attendance

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irzstudio.gajian.R
import com.irzstudio.gajian.databinding.FragmentAttendanceBinding
import com.irzstudio.gajian.ui.employee.AddEmployeeActivity
import com.irzstudio.gajian.ui.notification.NotificationActivity
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.michalsvec.singlerowcalendar.utils.DateUtils.getDates
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.fragment_attendance.*
import kotlinx.android.synthetic.main.item_calendar_selected.view.*
import java.util.*


class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private var calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var isClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        navToNotification()
        navToAddEmployee()
        hideButtonEmployee()
        setupCalendar(Date())

    }

    private fun navToNotification() {
        binding.imgNotif.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }
    }

    private fun navToAddEmployee() {
        binding.btnAddEmployee.setOnClickListener {
            startActivity(Intent(requireContext(), AddEmployeeActivity::class.java))
        }
    }

    private fun hideButtonEmployee() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.btnAddEmployee.getVisibility() == View.VISIBLE) {
                    binding.btnAddEmployee.visibility = View.GONE
                } else if (dy < 0 && binding.btnAddEmployee.getVisibility() !=View.VISIBLE) {
                    binding.btnAddEmployee.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupCalendar(date: Date) {

        binding.txtDate.text = "${DateUtils.getMonthName(date)}, ${DateUtils.getYear(date)}"

        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(position: Int, date: Date, isSelected: Boolean): Int {

                calendar.time = date

                return if (isSelected) {
                    (Calendar.DAY_OF_WEEK)
                    R.layout.item_calendar_selected
                } else {
                    (Calendar.DAY_OF_WEEK)
                    R.layout.item_calendar_not_selected
                }
            }

            override fun bindDataToCalendarView(holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date, position: Int, isSelected: Boolean) {
                holder.itemView.tv_date_calendar.text = DateUtils.getDayNumber(date)
                holder.itemView.tv_day_calendar.text = DateUtils.getDay3LettersName(date)
            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {

                calendar.time = date

                when (Calendar.DAY_OF_WEEK) {
                    else -> return true
                }
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
            }

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)

                calendar.set(Calendar.MONTH, currentMonth)
                val month = calendar.time
                binding.txtDate.text = "${DateUtils.getMonthName(month)}, ${DateUtils.getYear(date)}"

            }
            override fun whenCalendarScrolled(dx: Int, dy: Int) {
                super.whenCalendarScrolled(dx, dy)
            }
            override fun whenSelectionRestored() {
                super.whenSelectionRestored()
            }
            override fun whenSelectionRefreshed() {
                super.whenSelectionRefreshed()
            }
        }

        val singleRowCalendar = binding.mainSingleRowCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            includeCurrentDate = true
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        binding.btnNext.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfNextMonth())
        }

        binding.btnPrevious.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfPreviousMonth())
        }
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        val year = calendar.time
        calendar.set(Calendar.MONTH, currentMonth)
        val nextMonth = calendar.time

        binding.txtDate.text = "${DateUtils.getMonthName(nextMonth)}, ${DateUtils.getYear(year)}"
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        val year = calendar.time
        calendar.set(Calendar.MONTH, currentMonth)
        val previousMonth = calendar.time

        binding.txtDate.text = "${DateUtils.getMonthName(previousMonth)}, ${DateUtils.getYear(year)}"
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

}