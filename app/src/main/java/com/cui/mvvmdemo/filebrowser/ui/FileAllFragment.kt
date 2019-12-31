package com.hiscene.presentation.filebrowser.ui

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cui.lib.toast.ToastUtils
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.bean.ReqResult
import com.cui.mvvmdemo.constant.MainConstant
import com.cui.mvvmdemo.filebrowser.entity.FileEntity
import com.cui.mvvmdemo.filebrowser.utils.FileSelectFilter
import com.cui.mvvmdemo.filebrowser.viewmodel.FilePickerViewModel
import com.hiscene.presentation.filebrowser.ui.adapter.ReceiveFileShowAdapter
import com.hiscene.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_file_all.*
import java.io.File

/**
 * 作者：cuiqingchao 2019-11-24 11:05
 * 全部文件
 */

class FileAllFragment : BaseFragment() {
    private var mFilter: FileSelectFilter? = null
    //筛选类型条件
    private val mFileTypes = arrayOf<String>()
    private var mAllFileAdapter: ReceiveFileShowAdapter? = null
    private var mFileInfos: ArrayList<FileEntity> = ArrayList()
    private lateinit var viewModel: FilePickerViewModel
    private var mPath: String? = null
    private var rootPath: String? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_file_all
    }

    override fun initView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rl_all_file!!.layoutManager = layoutManager
        rl_all_file.setHasFixedSize(true)
        rl_all_file.setItemViewCacheSize(20)

        mAllFileAdapter = ReceiveFileShowAdapter(context!!, mFileInfos)
        rl_all_file!!.adapter = mAllFileAdapter
    }

    override fun initData() {
        viewModel = ViewModelProviders.of(this).get(FilePickerViewModel::class.java)
        lifecycle.addObserver(viewModel)

        mPath = Environment.getExternalStorageDirectory().absolutePath
        rootPath = Environment.getExternalStorageDirectory().absolutePath
        mFilter = FileSelectFilter(mFileTypes)
    }

    override fun initListener() {

        tv_back!!.setOnClickListener(View.OnClickListener {
            val tempPath = File(mPath).parent
            if (tempPath == null || mPath == rootPath) {
                Toast.makeText(context, "最外层了", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            mPath = tempPath
            requestData()
        })
        mAllFileAdapter?.setOnItemClickListener(object : ReceiveFileShowAdapter.OnFileItemClickListener {
            override fun click(position: Int) {

                if (mFileInfos != null && mFileInfos.size > position && mFileInfos[position].file != null) {
                    val entity = mFileInfos[position]
                    //如果是文件夹点击进入文件夹
                    if (entity.file.isDirectory) {
                        getIntoChildFolder(position)
                    } else {
                        if (entity.file.length() < MainConstant.MAX_FILE_SIZE) {
                            val intent = Intent()
                            intent.putExtra("path", entity.path)
                            activity!!.setResult(Activity.RESULT_OK, intent)
                            activity!!.finish()
                        } else {
                            ToastUtils.show("所选文件不能超过50M")
                        }
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
        mPath?.let {
            viewModel.getFilePath(mPath!!)
        }
    }

    override fun refreshView() {
        if (mFileInfos.size == 0) {
            rl_all_file.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        } else {
            rl_all_file.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        }
        mAllFileAdapter?.notifyDataSetChanged()
        rl_all_file!!.scrollToPosition(0)
    }

    //进入子文件夹
    private fun getIntoChildFolder(position: Int) {
        mPath = mFileInfos[position].file.absolutePath
        requestData()
    }

    companion object {
        fun newInstance(): FileAllFragment {
            return FileAllFragment()
        }
    }
}
