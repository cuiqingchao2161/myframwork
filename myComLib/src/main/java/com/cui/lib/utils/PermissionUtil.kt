package com.cui.lib.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * description : 权限管理
 * author : cuiqingchao
 * date : 2019/9/29 16:14
 */
class PermissionUtil {
    private var mPermissionResultCallBack: PermissionResultCallBack? = null
    private var mRequestCode: Int = 0
    private var mContext: Activity? = null
    private var mFragment: Fragment? = null
    private var mPermissionListNeedReq: MutableList<MyPermissionInfo>? = null
    private var mPermissions: Array<String?>? = null

    companion object {
        val instance: PermissionUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PermissionUtil()
        }
    }

    class MyPermissionInfo{
        var name:String = ""
        var isRationalNeed = true //选择不再询问后置为false
    }

    interface PermissionResultCallBack{
        /**
         * 请求权限提示窗口
         */
        fun showRationaleForPermission()

        /**
         * 当全部权限的申请被用户允许之后,该方法会被调用
         */
        fun onPermissionGranted()

        /**
         * 当权限申请中的某一个或多个权限,被用户以前否定了,并确认了不再提醒时,也就是权限的申请窗体不能再弹出时,
         * 该方法将会被调用
         * @param permissions
         */
        fun showNeverAskForPermission( permissions: Array<String?>)

        /**
         * 当权限申请中的某一个或多个权限,被用户否定了,但没有确认不再提醒时,也就是权限窗体申请时,但被否定了之后,
         * 该方法将会被调用.
         * @param permissions
         */
        fun showDeniedForPermission(permissions: Array<String?>)
    }

    fun checkPermissions(@NonNull context: Activity?, @NonNull permissions: Array<String?>?, @NonNull requestCode: Int, callBack: PermissionResultCallBack){
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw RuntimeException("request permission only can run in MainThread!")
        }

        if (permissions?.size == 0) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onGranted()
            return
        }

        this.mContext = context
        this.mPermissions = permissions
        this.mRequestCode = requestCode
        this.mPermissionResultCallBack = callBack
        this.mPermissionListNeedReq = ArrayList()

        if (needToRequest()) {
            callBack.showRationaleForPermission()
        } else {
            onGranted()
        }
    }

    /**
     * 用于activity中请求权限
     * @param context
     * @param permissions
     * @param requestCode
     * @param callBack
     */
    fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContext?.requestPermissions(mPermissions!!,mRequestCode)
        }
    }

    /**
     * 检查是否须要申请权限
     * @return
     */
    private fun needToRequest(): Boolean {
        for (permission in mPermissions!!) {
            val checkRes = ContextCompat.checkSelfPermission(mContext!!, permission?:"")
            if (checkRes != PackageManager.PERMISSION_GRANTED) {
                val info = MyPermissionInfo()
                info.name = permission?:""
//                if (mContext is Activity && ActivityCompat.shouldShowRequestPermissionRationale((mContext as Activity?)!!, permission?:"")) {//三星部分手机在无此权限且未点击不再询问时也会返回false
                    mPermissionListNeedReq!!.add(info)
//                }

            }
        }

        if (mPermissionListNeedReq!!.size > 0) {
            mPermissions = arrayOfNulls(mPermissionListNeedReq!!.size)
            for (i in mPermissionListNeedReq!!.indices) {
                mPermissions!![i] = mPermissionListNeedReq!![i].name
            }
            return true
        }

        return false
    }

    /**
     * 申请权限结果返回
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == mRequestCode) {
            var isAllGranted = true
            val needRationalPermissionList = ArrayList<MyPermissionInfo>()
            val deniedPermissionList = ArrayList<MyPermissionInfo>()
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (mPermissionListNeedReq!![i].isRationalNeed) {
                        needRationalPermissionList.add(mPermissionListNeedReq!![i])
                    } else {
                        deniedPermissionList.add(mPermissionListNeedReq!![i])
                    }
                    isAllGranted = false
                }
            }

            if (needRationalPermissionList.size != 0) {
                showRational(needRationalPermissionList)
            }

            if (deniedPermissionList.size != 0) {
                onDenied(deniedPermissionList)
            }

            if (isAllGranted) {
                onGranted()
            }

        }
    }

    /**
     * 权限被用户许可之后回调的方法
     */
    private fun onGranted() {
        if (mPermissionResultCallBack != null) {
            mPermissionResultCallBack!!.onPermissionGranted()
        }
    }

    /**
     * 权限申请被用户否定之后的回调方法,这个主要是当用户点击否定的同一时候点击了不在弹出,
     * 那么当再次申请权限,此方法会被调用
     * @param list
     */
    private fun onDenied(list: List<MyPermissionInfo>?) {
        if (list == null || list.size == 0) return

        val permissions = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            permissions[i] = list[i].name
        }

        if (mPermissionResultCallBack != null && permissions.size > 0) {
            mPermissionResultCallBack!!.showNeverAskForPermission(permissions)
        }
    }

    /**
     * 权限申请被用户否定后的回调方法,这个主要场景是当用户点击了否定,但未点击不在弹出,
     * 那么当再次申请权限的时候,此方法会被调用
     * @param list
     */
    private fun showRational(list: List<MyPermissionInfo>?) {
        if (list == null || list.size == 0) return

        val permissions = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            permissions[i] = list[i].name
        }

        if (mPermissionResultCallBack != null) {
            mPermissionResultCallBack!!.showDeniedForPermission(permissions)
        }
    }
}