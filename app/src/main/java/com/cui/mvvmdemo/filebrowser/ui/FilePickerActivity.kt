package com.cui.mvvmdemo.filebrowser.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.filebrowser.utils.PickerManager
import com.cui.mvvmdemo.listener.NavigationFinishClickListener
import com.cui.mvvmdemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_file_picker.*
import kotlinx.android.synthetic.main.include_toolbar_layout.*

/**
 * 作者：cuiqingchao 2019-11-24 11:05
 * 文件选择
 */
class FilePickerActivity : BaseActivity(), View.OnClickListener {
    private var commonFileFragment: Fragment? = null
    private var allFileFragment: Fragment? = null
    private var isConfirm = false

    override fun getLayoutId(): Int {
        return R.layout.activity_file_picker
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        val window = window
        window.setFlags(flag, flag)

        super.onCreate(savedInstanceState)
    }
    override fun initView() {
        tvToolbarTitle.text = "选择文件"
    }

    override fun initData() {
        setFragment(1)
    }

    override fun initListener() {
        ivToolbarNavigation.setOnClickListener(NavigationFinishClickListener(this))
        btn_common!!.setOnClickListener(this)
        btn_all!!.setOnClickListener(this)
        tv_confirm!!.setOnClickListener(this)
    }

    override fun requestData() {

    }

    override fun refreshView() {

    }

    private fun setFragment(type: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        hideFragment(fragmentTransaction)
        when (type) {
            1 -> if (commonFileFragment == null) {
                commonFileFragment = FileCommonFragment.newInstance()
                fragmentTransaction.add(R.id.fl_content, commonFileFragment!!)
            } else {
                fragmentTransaction.show(commonFileFragment!!)
            }
            2 -> if (allFileFragment == null) {
                allFileFragment = FileAllFragment.newInstance()
                fragmentTransaction.add(R.id.fl_content, allFileFragment!!)
            } else {
                fragmentTransaction.show(allFileFragment!!)
            }
        }
        fragmentTransaction.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction) {
        if (commonFileFragment != null) {
            transaction.hide(commonFileFragment!!)
        }
        if (allFileFragment != null) {
            transaction.hide(allFileFragment!!)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_common -> {
                setFragment(1)
                btn_common!!.setBackgroundResource(R.mipmap.no_read_pressed)
                btn_common!!.setTextColor(ContextCompat.getColor(this, R.color.white_color))
                btn_all!!.setBackgroundResource(R.mipmap.already_read)
                btn_all!!.setTextColor(ContextCompat.getColor(this, R.color.blue_color))
            }
            R.id.btn_all -> {
                setFragment(2)
                btn_common!!.setBackgroundResource(R.mipmap.no_read)
                btn_common!!.setTextColor(ContextCompat.getColor(this, R.color.blue_color))
                btn_all!!.setBackgroundResource(R.mipmap.already_read_pressed)
                btn_all!!.setTextColor(ContextCompat.getColor(this, R.color.white_color))
            }
            R.id.tv_confirm -> {
                isConfirm = true
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!isConfirm) {
            PickerManager.getInstance().files.clear()
        }
    }
}
