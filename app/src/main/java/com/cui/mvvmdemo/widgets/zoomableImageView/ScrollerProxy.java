package com.cui.mvvmdemo.widgets.zoomableImageView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/12/15.
 */

abstract class ScrollerProxy {
    ScrollerProxy() {
    }

    public static ScrollerProxy getScroller(Context context) {
        return (ScrollerProxy)(Build.VERSION.SDK_INT < 9?new ScrollerProxy.PreGingerScroller(context):new ScrollerProxy.GingerScroller(context));
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

    public abstract void forceFinished(boolean var1);

    public abstract int getCurrX();

    public abstract int getCurrY();

    private static class PreGingerScroller extends ScrollerProxy {
        private Scroller mScroller;

        public PreGingerScroller(Context context) {
            this.mScroller = new Scroller(context);
        }

        public boolean computeScrollOffset() {
            return this.mScroller.computeScrollOffset();
        }

        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
            this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
        }

        public void forceFinished(boolean finished) {
            this.mScroller.forceFinished(finished);
        }

        public int getCurrX() {
            return this.mScroller.getCurrX();
        }

        public int getCurrY() {
            return this.mScroller.getCurrY();
        }
    }

    @TargetApi(9)
    private static class GingerScroller extends ScrollerProxy {
        private OverScroller mScroller;

        public GingerScroller(Context context) {
            this.mScroller = new OverScroller(context);
        }

        public boolean computeScrollOffset() {
            return this.mScroller.computeScrollOffset();
        }

        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
            this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
        }

        public void forceFinished(boolean finished) {
            this.mScroller.forceFinished(finished);
        }

        public int getCurrX() {
            return this.mScroller.getCurrX();
        }

        public int getCurrY() {
            return this.mScroller.getCurrY();
        }
    }
}
