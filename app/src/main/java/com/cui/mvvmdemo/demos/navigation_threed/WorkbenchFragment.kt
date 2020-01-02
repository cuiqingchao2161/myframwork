package com.cui.mvvmdemo.demos.navigation_threed

import androidx.recyclerview.widget.GridLayoutManager
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_workbench.*


/**
 * 工作台
 * @author cuiqingchao
 * @date 2019/9/18
 */
class WorkbenchFragment : BaseFragment() {
    private lateinit var gridLayoutManager: GridLayoutManager

    private val spanCount = 3

    companion object {
        fun newInstance() = WorkbenchFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_workbench
    }

    override fun initView() {
        fragment_imageView.setImageResource(R.mipmap.img0100)
        tv_net_error_hint.text = "工作台"
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

