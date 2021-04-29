package com.cui.mvvmdemo.demos.calendarReminder

import android.widget.Toast
import com.cui.lib.toast.ToastUtils
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_calender_reminder.*

/**
 * 在系统日历中插入提醒和删除提醒
 */
class CalendarReminderActivity : BaseActivity() {
    var i = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_calender_reminder
    }


    override fun initView() {}
    override fun initData() {}
    override fun initListener() {
        add.setOnClickListener {
            CalendarReminderUtils.addCalendarEvent(this,"提醒$i","有一个新的任务$i",0,1)
            ToastUtils.show("添加提醒$i")
            i++
        }

        delete.setOnClickListener {
            if(i>0){
                i--
            }
            CalendarReminderUtils.deleteCalendarEvent(this,"提醒$i")
            ToastUtils.show("删除提醒$i")
        }
    }
    override fun requestData() {}
    override fun refreshView() {}
}