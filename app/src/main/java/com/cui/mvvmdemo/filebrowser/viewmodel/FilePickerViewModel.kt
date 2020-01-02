package com.cui.mvvmdemo.filebrowser.viewmodel

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.cui.lib.toast.ToastUtils
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.base.EventMutableLiveData
import com.cui.mvvmdemo.bean.ReqResult
import com.cui.mvvmdemo.constant.MainConstant
import com.cui.mvvmdemo.filebrowser.entity.FileEntity
import com.cui.mvvmdemo.filebrowser.utils.FileUtils
import com.cui.mvvmdemo.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FilePickerViewModel : BaseViewModel() {
    private var mutablePathLiveData: EventMutableLiveData<ReqResult<ArrayList<FileEntity>>> = EventMutableLiveData()


    fun getFilePath(fileAbsolutePath :String){
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            ToastUtils.show(R.string.not_available)
            return
        }
       Observable.create(ObservableOnSubscribe<String> {
           var files = getFileList(fileAbsolutePath)
           val fileData = ReqResult<ArrayList<FileEntity>>()
           fileData.status = MainConstant.STATUS_SUCCESS
           fileData.data = files
           mutablePathLiveData.postValue(fileData)
       })
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe ()
    }


    /**
     * 根据地址获取当前地址下的所有目录和文件，并且排序
     *
     * @param path
     * @return List<File>
    </File> */
    private fun getFileList(path: String): ArrayList<FileEntity> {
        return FileUtils.getFileListByDirPath(path)
    }

    fun getPathLiveData() : MutableLiveData<ReqResult<ArrayList<FileEntity>>>{
        return mutablePathLiveData
    }
}