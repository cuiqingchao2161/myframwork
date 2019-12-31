package com.hiscene.presentation.filebrowser.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cui.lib.toast.ToastUtils
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.bean.ReqResult
import com.cui.mvvmdemo.constant.MainConstant
import com.cui.mvvmdemo.constant.MainConstant.MAX_FILE_SIZE
import com.cui.mvvmdemo.constant.MainConstant.PIC_PATH
import com.cui.mvvmdemo.filebrowser.entity.FileEntity
import com.cui.mvvmdemo.filebrowser.viewmodel.FilePickerViewModel
import com.hiscene.presentation.filebrowser.ui.adapter.ReceiveFileShowAdapter
import com.hiscene.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_file_normal.*

/**
 * 作者：cuiqingchao 2019-11-24 11:05
 * 常用文件
 */

class FileCommonFragment : BaseFragment() {
    private var mFileInfos: ArrayList<FileEntity> = ArrayList()
    private lateinit var receiveFileShowAdapter: ReceiveFileShowAdapter
    private lateinit var viewModel: FilePickerViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_file_normal
    }

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProviders.of(this).get(FilePickerViewModel::class.java)
        lifecycle.addObserver(viewModel)

        receiveFileShowAdapter = ReceiveFileShowAdapter(activity, mFileInfos)

        rc_view_normal.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rc_view_normal.adapter = receiveFileShowAdapter
        rc_view_normal.setHasFixedSize(true)
        rc_view_normal.setItemViewCacheSize(20)
    }

    override fun initListener() {
        receiveFileShowAdapter.setOnItemClickListener(object : ReceiveFileShowAdapter.OnFileItemClickListener {
            override fun click(position: Int) {
                if (mFileInfos[position].file != null) {
                    val entity = mFileInfos[position]
                    if (entity.file.length() < MAX_FILE_SIZE) {
                        val intent = Intent()
                        intent.putExtra("path", entity.path)
                        activity!!.setResult(Activity.RESULT_OK, intent)
                        activity!!.finish()
                    } else {
                        ToastUtils.show("所选文件不能超过50M")
                    }

                }
            }

        })
        viewModel.getPathLiveData().observe(this, Observer<ReqResult<ArrayList<FileEntity>>> { t ->
            if (t?.status == MainConstant.STATUS_SUCCESS) {
                swipe_fresh.isRefreshing = false
                t.data?.let {
                    mFileInfos.clear()
                    mFileInfos.addAll(t.data!!)
                    refreshView()
                }
            }
        })

        //下拉刷新的监听
        swipe_fresh.setOnRefreshListener {
            requestData()
        }
    }

    override fun requestData() {
        swipe_fresh.isRefreshing = true
        viewModel.getFilePath(PIC_PATH)
    }

    override fun refreshView() {
        if (mFileInfos.size == 0) {
            rc_view_normal.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        } else {
            rc_view_normal.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        }
        receiveFileShowAdapter.notifyDataSetChanged()
    }

    companion object {

        fun newInstance(): FileCommonFragment {
            return FileCommonFragment()
        }
    }
}
