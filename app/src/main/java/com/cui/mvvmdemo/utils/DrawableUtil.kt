package com.cui.mvvmdemo.utils

import android.widget.TextView

/**
 * description : drawable相关工具
 * author : cuiqingchao
 * date : 2019/9/27 14:08
 */
class DrawableUtil {
    companion object {
        /**
         * TextView四周drawable的序号。
         * 0 left,  1 top, 2 right, 3 bottom
         */
        val LEFT = 0
        val RIGHT = 2
        val TOP = 1
        val BOTTOM = 3

        fun setRadioButtonDrawableSize(view: TextView, width:Float, height:Float,location:Int){
            var drawableWidth:Int = DensityHelper.dp2px(view.context,width)
            var drawableHeight = DensityHelper.dp2px(view.context,height)
            var draws360 = view.compoundDrawables

            when(location){
                LEFT -> {
                    draws360[0]?.setBounds(0,0, drawableWidth,drawableHeight)
                    view.setCompoundDrawables(draws360[0],null,null,null)
                }
                TOP ->  {
                    draws360[1]?.setBounds(0,0, drawableWidth,drawableHeight)
                    view.setCompoundDrawables(null,draws360[1],null,null)
                }
                RIGHT ->  {
                    draws360[2]?.setBounds(0,0, drawableWidth,drawableHeight)
                    view.setCompoundDrawables(null,null,draws360[2],null)
                }
                BOTTOM ->  {
                    draws360[3]?.setBounds(0,0, drawableWidth,drawableHeight)
                    view.setCompoundDrawables(null,null,null,draws360[3])
                }
            }

        }

    }

}