package com.cui.mvvmdemo.constant

import com.cui.mvvmdemo.base.MyApplication
import java.io.File

/**
 * 作者： 周旭 on 2017年10月19日 0019.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

object MainConstant {

    object LoadData {
        val FIRST_LOAD = 0 //首次加载
        val REFRESH = 1 //下拉刷新
        val LOAD_MORE = 2 //上拉加载更多
    }

    private val ROOT_PATH: String = MyApplication.getInstance()!!.getExternalFilesDir("")!!.absolutePath
    val DOWNLOAD_PATH = ROOT_PATH + File.separator + "download"
    val PIC_PATH = ROOT_PATH + File.separator + "Pictures"


    const val STATUS_FAIL = 0 //失败
    const val STATUS_SUCCESS = 1   //成功
    const val STATUS_RECYCLE = -1   //成功

    const val MAX_FILE_SIZE = 50 * 1024 * 1024


}
