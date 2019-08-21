package com.cui.mvvmdemo.widgets.zoomableImageView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2018/12/15.
 */

public abstract class VersionedGestureDetector {
    static final String LOG_TAG = "VersionedGestureDetector";
    VersionedGestureDetector.OnGestureListener mListener;

    VersionedGestureDetector() {
    }

    public static VersionedGestureDetector newInstance(Context context, VersionedGestureDetector.OnGestureListener listener) {
        int sdkVersion = Build.VERSION.SDK_INT;
        VersionedGestureDetector detector = null;
        if(sdkVersion < 5) {
            detector = new VersionedGestureDetector.CupcakeDetector(context);
        } else if(sdkVersion < 8) {
            detector = new VersionedGestureDetector.EclairDetector(context);
        } else {
            detector = new VersionedGestureDetector.FroyoDetector(context);
        }

        ((VersionedGestureDetector)detector).mListener = listener;
        return (VersionedGestureDetector)detector;
    }

    public abstract boolean onTouchEvent(MotionEvent var1);

    public abstract boolean isScaling();

    @TargetApi(8)
    private static class FroyoDetector extends VersionedGestureDetector.EclairDetector {
        private final ScaleGestureDetector mDetector;
        private final ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                FroyoDetector.this.mListener.onScale(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
                return true;
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        };

        public FroyoDetector(Context context) {
            super(context);
            this.mDetector = new ScaleGestureDetector(context, this.mScaleListener);
        }

        public boolean isScaling() {
            return this.mDetector.isInProgress();
        }

        public boolean onTouchEvent(MotionEvent ev) {
            this.mDetector.onTouchEvent(ev);
            return super.onTouchEvent(ev);
        }
    }

    @TargetApi(5)
    private static class EclairDetector extends VersionedGestureDetector.CupcakeDetector {
        private static final int INVALID_POINTER_ID = -1;
        private int mActivePointerId = -1;
        private int mActivePointerIndex = 0;

        public EclairDetector(Context context) {
            super(context);
        }

        float getActiveX(MotionEvent ev) {
            try {
                return ev.getX(this.mActivePointerIndex);
            } catch (Exception var3) {
                return ev.getX();
            }
        }

        float getActiveY(MotionEvent ev) {
            try {
                return ev.getY(this.mActivePointerIndex);
            } catch (Exception var3) {
                return ev.getY();
            }
        }

        public boolean onTouchEvent(MotionEvent ev) {
            int action = ev.getAction();
            switch(action & 255) {
                case 0:
                    this.mActivePointerId = ev.getPointerId(0);
                    break;
                case 1:
                case 3:
                    this.mActivePointerId = -1;
                case 2:
                case 4:
                case 5:
                default:
                    break;
                case 6:
                    int pointerIndex = (ev.getAction() & '\uff00') >> 8;
                    int pointerId = ev.getPointerId(pointerIndex);
                    if(pointerId == this.mActivePointerId) {
                        int newPointerIndex = pointerIndex == 0?1:0;
                        this.mActivePointerId = ev.getPointerId(newPointerIndex);
                        this.mLastTouchX = ev.getX(newPointerIndex);
                        this.mLastTouchY = ev.getY(newPointerIndex);
                    }
            }

            this.mActivePointerIndex = ev.findPointerIndex(this.mActivePointerId != -1?this.mActivePointerId:0);
            return super.onTouchEvent(ev);
        }
    }

    private static class CupcakeDetector extends VersionedGestureDetector {
        float mLastTouchX;
        float mLastTouchY;
        final float mTouchSlop;
        final float mMinimumVelocity;
        private VelocityTracker mVelocityTracker;
        private boolean mIsDragging;

        public CupcakeDetector(Context context) {
            ViewConfiguration configuration = ViewConfiguration.get(context);
            this.mMinimumVelocity = (float)configuration.getScaledMinimumFlingVelocity();
            this.mTouchSlop = (float)configuration.getScaledTouchSlop();
        }

        float getActiveX(MotionEvent ev) {
            return ev.getX();
        }

        float getActiveY(MotionEvent ev) {
            return ev.getY();
        }

        public boolean isScaling() {
            return false;
        }

        public boolean onTouchEvent(MotionEvent ev) {
            float x;
            float y;
            switch(ev.getAction()) {
                case 0:
                    this.mVelocityTracker = VelocityTracker.obtain();
                    this.mVelocityTracker.addMovement(ev);
                    this.mLastTouchX = this.getActiveX(ev);
                    this.mLastTouchY = this.getActiveY(ev);
                    this.mIsDragging = false;
                    break;
                case 1:
                    if(this.mIsDragging && null != this.mVelocityTracker) {
                        this.mLastTouchX = this.getActiveX(ev);
                        this.mLastTouchY = this.getActiveY(ev);
                        this.mVelocityTracker.addMovement(ev);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        x = this.mVelocityTracker.getXVelocity();
                        y = this.mVelocityTracker.getYVelocity();
                        if(Math.max(Math.abs(x), Math.abs(y)) >= this.mMinimumVelocity) {
                            this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -x, -y);
                        }
                    }

                    if(null != this.mVelocityTracker) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    break;
                case 2:
                    x = this.getActiveX(ev);
                    y = this.getActiveY(ev);
                    float dx = x - this.mLastTouchX;
                    float dy = y - this.mLastTouchY;
                    if(!this.mIsDragging) {
                        this.mIsDragging = Math.sqrt((double)(dx * dx + dy * dy)) >= (double)this.mTouchSlop;
                    }

                    if(this.mIsDragging) {
                        this.mListener.onDrag(dx, dy);
                        this.mLastTouchX = x;
                        this.mLastTouchY = y;
                        if(null != this.mVelocityTracker) {
                            this.mVelocityTracker.addMovement(ev);
                        }
                    }
                    break;
                case 3:
                    if(null != this.mVelocityTracker) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
            }

            return true;
        }
    }

    public interface OnGestureListener {
        void onDrag(float var1, float var2);

        void onFling(float var1, float var2, float var3, float var4);

        void onScale(float var1, float var2, float var3);
    }
}
