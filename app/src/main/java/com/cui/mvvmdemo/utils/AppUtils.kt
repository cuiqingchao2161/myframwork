package com.cui.mvvmdemo.utils

import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.Log
import com.elvishew.xlog.XLog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2020/11/25 16:02
 */
class AppUtils {
    private val TAG = this.javaClass.simpleName
    private var packageName_pre : String = ""

    fun startCameraStateListener(context: Context){
        XLog.i(TAG, "startCameraStateListener")
        CoroutineScope(Dispatchers.Default).launch {
            Observable.interval(3000, TimeUnit.MILLISECONDS)
                    .subscribe(object : Observer<Long> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: Long) {
                            //判断是否有use 查看使用情况的权限
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                                val useGranted = isUseGranted(context)
                                Log.e("TopAppService", "use 权限 是否允许授权=$useGranted")
                                if (useGranted) {
                                    val curPackageName: String? = getHigherPackageName(context)

                                    curPackageName?.apply {
                                        XLog.i(TAG, "curPackageName:$curPackageName,packageName_pre:$packageName_pre")
                                        if (packageName_pre != curPackageName) { //如果两个包名不相同，那么代表切换了应用
                                            packageName_pre = curPackageName!! //更新当前的应用包名
                                            if(curPackageName == context.applicationContext.packageName){
//                                                var intent = Intent(BROADCAST_APP_RECOVER_TOP)
//                                                context.sendBroadcast(intent)
                                            }
                                        }
                                    }
                                    Log.e("TopAppService", "顶层app=$curPackageName")
                                } else {
                                    //开启应用授权界面
                                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                }
                            }else {
                                val curPackageName: String? = getHigherPackageName(context)

                                curPackageName?.apply {
                                    XLog.i(TAG, "curPackageName:$curPackageName,packageName_pre:$packageName_pre")
                                    if (packageName_pre != curPackageName) { //如果两个包名不相同，那么代表切换了应用
                                        packageName_pre = curPackageName!! //更新当前的应用包名
                                        if(curPackageName == context.applicationContext.packageName){
//                                            var intent = Intent(BROADCAST_APP_RECOVER_TOP)
//                                            context.sendBroadcast(intent)
                                        }
                                    }
                                }
                                Log.e("TopAppService", "顶层app=$curPackageName")
                            }

                        }

                        override fun onError(e: Throwable) {

                        }

                    })
        }
    }

    /**
     * 判断  用户查看使用情况的权利是否给予app
     *
     * @return
     */
    private fun isUseGranted(context: Context): Boolean {
        val appOps: AppOpsManager = context
                .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = -1
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mode = appOps.checkOpNoThrow("android:get_usage_stats",
                    Process.myUid(), context.packageName)
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * 高版本：获取顶层的activity的包名
     *
     * @return
     */
    private fun getHigherPackageName(context: Context): String? {
        var topPackageName = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mUsageStatsManager: UsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            //time - 1000 * 1000, time 开始时间和结束时间的设置，在这个时间范围内 获取栈顶Activity 有效
            val stats: List<UsageStats> = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
            // Sort the stats by the last time used
            if (stats != null) {
                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap<Long, UsageStats>()
                for (usageStats in stats) {
                    mySortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap[mySortedMap.lastKey()]!!.packageName
                    Log.e("TopPackage Name", topPackageName)
                }
            }
        } else {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            var taskList : List<ActivityManager.RunningTaskInfo> = activityManager.getRunningTasks(5)
            val topActivity = activityManager.getRunningTasks(1)[0].topActivity //获取到栈顶最顶层的activity所对应的应用
            topPackageName = topActivity!!.packageName //从ComponentName对象中获取到最顶层的应用包名
        }
        return topPackageName
    }
}