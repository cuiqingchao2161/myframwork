package com.zx.mvvmdemo.module;


import static com.zx.mvvmdemo.module.GestureTouchUtils.NORMAL;

/**
 * class description here
 * Created by qingchao.cui on 2019/5/8.
 */

public class GestureBean {
    /**
     * 起始位置 X
     */
    float startX;
    /**
     * 起始位置 Y
     */
    float startY;
    /**
     * 终点位置 X
     */
    float endX;
    /**
     * 终点位置 Y
     */
    float endY;
    /**
     * 每个周期 x 移动的位置
     */
    float ratioX;
    /**
     * 每个周期 y 移动的位置
     */
    float ratioY;
    /**
     * 总共周期
     */
    long totalCount;
    /**
     * 当前周期
     */
    long count = 0;
    int period = NORMAL;

    GestureBean(float startX, float startY, float endX, float endY, long duration, int speed) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.period = speed;
        totalCount = duration / speed;
        ratioX = (endX - startX) / totalCount;
        ratioY = (endY - startY) / totalCount;
    }
}
