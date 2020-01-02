package com.cui.mvvmdemo.filebrowser.ui

//import kotlinx.android.synthetic.main.activity_album.*
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.bean.ReqResult
import com.cui.mvvmdemo.constant.MainConstant
import com.cui.mvvmdemo.filebrowser.entity.FileEntity
import com.cui.mvvmdemo.filebrowser.utils.OpenFile
import com.cui.mvvmdemo.filebrowser.viewmodel.FilePickerViewModel
import com.cui.mvvmdemo.listener.NavigationFinishClickListener
import com.cui.mvvmdemo.filebrowser.ui.adapter.ReceiveFileShowAdapter
import com.cui.mvvmdemo.navigation.Navigator
import com.cui.mvvmdemo.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_file_receive.*
import kotlinx.android.synthetic.main.include_toolbar_layout.*


/**
 * @author xujiangang
 * @date 2019/9/18
 */
class FileReceiveActivity : BaseActivity() {
    companion object {
        var mFileInfos: ArrayList<FileEntity> = ArrayList()
    }

    private lateinit var receiveFileShowAdapter: ReceiveFileShowAdapter
    lateinit var viewModel: FilePickerViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_file_receive
    }

    override fun initView() {
        tvToolbarTitle.text = "收到的文件"
    }

    override fun initData() {
        viewModel = ViewModelProviders.of(this).get(FilePickerViewModel::class.java)
        lifecycle.addObserver(viewModel)
        viewModel.onStart()

        receiveFileShowAdapter = ReceiveFileShowAdapter(this, mFileInfos)
        rc_view.adapter = receiveFileShowAdapter
        rc_view.layoutManager = LinearLayoutManager(this@FileReceiveActivity, LinearLayoutManager.VERTICAL, false)
        rc_view.setHasFixedSize(true)
        rc_view.setItemViewCacheSize(20)
    }

    override fun initListener() {
        ivToolbarNavigation.setOnClickListener(NavigationFinishClickListener(this))

        receiveFileShowAdapter.setOnItemClickListener(object : ReceiveFileShowAdapter.OnFileItemClickListener {
            override fun click(position: Int) {
                if (mFileInfos[position].fileType != null) {
                    if (mFileInfos[position].fileType.title == "IMG") {
                        Navigator.navigationImageDetail(this@FileReceiveActivity, position)
                    } else {
                        startActivity(OpenFile.openFiles(mFileInfos[position].path))
                    }
                } else {
                    startActivity(OpenFile.openFiles(mFileInfos[position].path))
                }

            }


        })
        viewModel.getPathLiveData().observe(this@FileReceiveActivity, Observer<ReqResult<ArrayList<FileEntity>>> { t ->
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
        viewModel.getFilePath(MainConstant.PIC_PATH)
    }

    override fun refreshView() {
        if (mFileInfos.size == 0) {
            rc_view.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        } else {
            rc_view.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        }
        receiveFileShowAdapter.notifyDataSetChanged()
    }

}
