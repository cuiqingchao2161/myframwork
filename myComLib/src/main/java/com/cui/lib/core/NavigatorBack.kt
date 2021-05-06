package com.cui.lib.core

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import com.cui.mycommonlibrary.R

/**
 * description : activity返回
 * author : cuiqingchao
 * date : 2019/9/10 15:22
 */
class NavigatorBack internal constructor(){
    companion object{
        /**
         * 转场动画和回退栈方向保持一致 历史页面在左 新页面在右 故回退时历史页面（左）进，新页面出（右）
         */
        fun navigateBackCommon(context: Activity? ) {
            if (context != null) {
                ActivityCompat.finishAfterTransition(context)//直接finish会造成makeSceneTransitionAnimation携带的控件销毁
                context.overridePendingTransition(R.anim.activity_out_enter, R.anim.activity_out_exit)
            }
        }

    }
}

