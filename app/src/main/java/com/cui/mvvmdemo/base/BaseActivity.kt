package com.cui.mvvmdemo.base


import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentCallbacks2
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.cui.lib.toast.ToastUtils
import com.cui.lib.utils.HomeWatcher
import com.cui.lib.utils.NewStatusBarUtil
import com.cui.lib.utils.PermissionUtil
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.ui.widgets.dialog.LoadingDialog
import com.cui.mvvmdemo.ui.widgets.dialog.PermissionDialog
import com.cui.mvvmdemo.navigation.NavigatorBack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.observable.ObservableJust
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * description : 项目中activity唯一基类，统一规范代码
 * @author cuiqingchao
 * @date 2019/9/18
 */

abstract class BaseActivity : AppCompatActivity() , PermissionUtil.PermissionResultCallBack  {
    open var TAG = this.javaClass.simpleName
    private var mHomeWatcher: HomeWatcher? = null
    var isInRecentTask = false
    var isResume = false
    var mPermissionDialog: PermissionDialog? = null
    lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewStatusBarUtil.transparencyBar(this)
        initialize()
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
        NewStatusBarUtil.setStatusBarColor(this, R.color.white_color)//设置状态栏颜色和顶部布局背景色一致
        NewStatusBarUtil.setStatusBarTextColor(this,true)
        watchHomeAndPowerPress()
    }

    override fun onStop() {
        Log.d(TAG, "onStop: ")
        super.onStop()
        if (mHomeWatcher != null) {
            mHomeWatcher!!.stopWatch()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        if (isInRecentTask) {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        super.onDestroy()
        mPermissionDialog = null
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        isResume = true
        isInRecentTask = false
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: ")
        isResume = false
        super.onPause()
    }

    private fun watchHomeAndPowerPress() {
        Log.d(TAG, "watchHomeAndPowerPress: ")
        mHomeWatcher = HomeWatcher(applicationContext)
        mHomeWatcher!!.setOnHomePressedListener(object : HomeWatcher.OnHomePressedListener {
            override fun onHomePressed() {
                Log.d(TAG, "onHomePressed: ")
                //按了HOME键
                homePressed()
            }

            override fun onHomeLongPressed() {
                Log.d(TAG, "onHomeLongPressed: ")
                //长按HOME键
                homeLongPressed()
            }
        })
        mHomeWatcher!!.startWatch()
    }

    private fun homePressed() {
        Log.i(TAG, "homePressed")
        moveTaskToBack(true)
        ToastUtils.show(R.string.label_backstage)
    }

    private fun homeLongPressed() {
        Log.i(TAG, "homeLongPressed")
        isInRecentTask = true
    }

    protected open fun initialize() {
        Log.d(TAG, "initialize: ")
        setContentView(getLayoutId())
        process()
        initView()

        PermissionUtil.instance.checkPermissions(this@BaseActivity,permissions,permissionReqCode,this@BaseActivity)
//        requestPerCallBackWithPermissionCheck()
    }

    /**
     * 延后执行出栈,并弹出提示
     *
     * @param delayMills 延迟时间
     */
    @SuppressLint("CheckResult")
    fun finishedDelay(delayMills: Int) {
//        Log.i(TAG, "finishDelay %d", delayMills)
        ObservableJust("finishDelay")
                .delay(delayMills.toLong(), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe {
                    finishAndRemoveTask()
                }
    }

    /**
     * 获得页面布局Id
     *
     * @return 布局Id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化view相关参数，在onCreate()里setContentView()后调用
     */
    protected abstract fun initView()

    /**
     * 初始化固定值数据，权限获取成功后调用（在initView()后）
     */
    protected abstract fun initData()

    /**
     * 设置监听（有可能需要用到数据 所以须放在initData后面）
     */
    protected abstract fun initListener()

    /**
     * 自动触发的数据获取，例如联系人列表的第一次获取，需要事件触发的数据拉取子类自行定义，不放在这里，此方法须放在initListener后面
     */
    protected abstract fun requestData()

    /**
     * view绑定获取的数据
     */
    protected abstract fun refreshView()


    fun dismissDialog() {
        if (mPermissionDialog != null) {
            mPermissionDialog!!.dismiss()
        }
    }

    fun showPermissionDialog() {
        if (mPermissionDialog != null) {
            mPermissionDialog!!.show()
        }
    }

    fun showLoadingDialog(msg: String) {
//        Log.i(TAG, "showProgressDialog %s", msg)
        if (!this::mLoadingDialog.isInitialized) {
            mLoadingDialog = LoadingDialog(this)
            mLoadingDialog.setCancelable(false)
        }
        if (this.isFinishing) {
            return
        }
        mLoadingDialog.setMessage(msg)
        mLoadingDialog.show()
    }

    fun dismissLoadingDialog() {
        Log.i(TAG, "dismissProgressDialog")
        if (this::mLoadingDialog.isInitialized) {
            mLoadingDialog.dismiss()
        }
    }

    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> System.gc()
            else -> {
            }
        }
        super.onTrimMemory(level)
    }

    val permissions:Array<String?>? = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    var permissionReqCode = 1000
    override fun showRationaleForPermission() {
        Log.i(TAG,"showRationaleForPermission")
//        mPermissionDialog = PermissionDialog(this)
//        mPermissionDialog!!.setOnCertainButtonClickListener { PermissionUtil.instance.requestPermissions() }
//        showPermissionDialog()
        PermissionUtil.instance.requestPermissions()
    }

    override fun onPermissionGranted() {
        Log.i(TAG,"onPermissionGranted")
        initData()
        initListener()
        requestData()
    }

    override fun showNeverAskForPermission(permissions: Array<String?>) {
        Log.i(TAG,"showNeverAskForPermission")
        finish()
        ToastUtils.show(R.string.permission_never_ask_again)
    }

    override fun showDeniedForPermission(permissions: Array<String?>) {
        Log.i(TAG,"showDeniedForPermission")
        finish()
        ToastUtils.show(R.string.permission_denied)
    }

    /**
     * 申请权限结果返回
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.instance.onRequestPermissionResult(requestCode,permissions,grantResults)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        NavigatorBack.navigateBackCommon(this)
    }

    private fun process() {
        var content = findViewById<FrameLayout>(android.R.id.content).getChildAt(0)
        if (content != null) {
            content.fitsSystemWindows = true
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }

    fun AppCompatActivity.showFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { show(fragment) }
    }

    fun AppCompatActivity.hideFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { hide(fragment) }
    }
}
