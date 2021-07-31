package com.irzstudio.gajian.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.irzstudio.gajian.R
import com.irzstudio.gajian.databinding.ActivityDashboardBinding
import com.irzstudio.gajian.ui.account.AccountFragment
import com.irzstudio.gajian.ui.attendance.AttendanceFragment
import com.irzstudio.gajian.ui.salary.SalaryFragment

class DashboardActivity : AppCompatActivity() {

    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initMenuBar()
        attendanceFragment()
    }

    private fun initMenuBar() {
        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.attendance -> attendanceFragment()
                R.id.salary -> salaryFragment()
                R.id.account -> accountFragment()
            }
            true
        }
    }

    private fun attendanceFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, AttendanceFragment()).commit()
    }

    private fun salaryFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, SalaryFragment()).commit()
    }

    private fun accountFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, AccountFragment()).commit()
    }

}