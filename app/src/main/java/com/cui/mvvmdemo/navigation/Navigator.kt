package com.hiscene.presentation.navigation

import android.app.Activity
import android.content.Intent
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.filebrowser.ui.ImageDetailsActivity

/**
 * description : activity跳转至新页面
 * author : cuiqingchao
 * date : 2019/9/10 15:22
 */
class Navigator internal constructor() {
    companion object {
        /**
         * 转场动画和回退栈方向保持一致 历史页面在左 新页面在右 故回退时新页面（右）进，新页面出（左）
         */
        private fun navigateAnimCommon(context: Activity?) {
            context?.overridePendingTransition(R.anim.activity_in_enter, R.anim.activity_in_exit)
        }

        fun navigationImageDetail(context: Activity?, position: Int) {
            if (context != null) {
                val intent = Intent(context, ImageDetailsActivity::class.java)
                intent.putExtra("image_position", position)
                context.startActivity(intent)
            }
        }
//        fun navigateToMain(context: Activity?, button: NbButton, view: View) {
//            if (context != null) {
//                val intent = Intent(context, MainActivity::class.java)
//
//                val xc = (button.left + button.right) / 2
//                val yc = (button.top + button.bottom) / 2
//                // 获取扩散的半径
//                val finalRadius = hypot(xc.toDouble(), yc.toDouble()).toFloat()
//                val animator = ViewAnimationUtils.createCircularReveal(view, xc, yc, 0f, finalRadius)
//                animator.duration = 400
//                animator.addListener(object : Animator.AnimatorListener {
//                    override fun onAnimationStart(animation: Animator) {
//                        context.startActivity(intent)
//                        context.overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
//                        context.finish()
//                    }
//
//                    override fun onAnimationEnd(animation: Animator) {
//                    }
//
//                    override fun onAnimationCancel(animation: Animator) {
//
//                    }
//
//                    override fun onAnimationRepeat(animation: Animator) {
//
//                    }
//                })
//                animator.start()
//                view.background.alpha = 255
//
//            }
//        }
    }
}

