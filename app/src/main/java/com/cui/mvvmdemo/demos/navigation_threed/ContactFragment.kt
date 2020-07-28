package com.cui.mvvmdemo.demos.navigation_threed

import androidx.recyclerview.widget.GridLayoutManager
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_workbench.*


/**
 * 工作台
 * @author cuiqingchao
 * @date 2019/9/18
 */
class ContactFragment : BaseFragment() {
    private lateinit var gridLayoutManager: GridLayoutManager

    private val spanCount = 3

    companion object {
        fun newInstance() = ContactFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_workbench
    }

    override fun initView() {
        fragment_imageView.setImageResource(R.mipmap.img0030)
        tv_net_error_hint.text = "联系人"
    }


    override fun initData() {
    }

    override fun initListener() {
    }


    override fun requestData() {

    }

    override fun refreshView() {

    }

}

