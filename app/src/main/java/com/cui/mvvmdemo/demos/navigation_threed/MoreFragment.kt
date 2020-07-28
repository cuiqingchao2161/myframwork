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
class MoreFragment : BaseFragment() {
    private lateinit var gridLayoutManager: GridLayoutManager

    private val spanCount = 3

    companion object {
        fun newInstance() = MoreFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_workbench
    }

    override fun initView() {
        fragment_imageView.setImageResource(R.mipmap.img0130)
        tv_net_error_hint.text = "更多"
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

