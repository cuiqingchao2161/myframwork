package com.cui.mvvmdemo.demos.navigation_threed


import android.content.ComponentCallbacks2
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * description : 项目中activity唯一基类，统一规范代码
 * @author cuiqingchao
 * @date 2019/9/18
 */

abstract class BaseActivity : AppCompatActivity() {
    protected open var TAG = "BaseActivity"
    var isInRecentTask = false
    var isResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onDestroy() {
        if (isInRecentTask) {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        super.onDestroy()
    }

    override fun onResume() {
        isResume = true
        isInRecentTask = false
        super.onResume()
    }

    override fun onPause() {
        isResume = false
        super.onPause()
    }

    protected open fun initialize() {
        setContentView(getLayoutId())
        process()
        initView()
        initData()
        initListener()
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


    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> System.gc()
            else -> {
            }
        }
        super.onTrimMemory(level)
    }

    fun process() {
        var content = findViewById<FrameLayout>(android.R.id.content).getChildAt(0)
        if (content != null) {
            content.fitsSystemWindows = true
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{replace(frameId, fragment)}
    }
}
