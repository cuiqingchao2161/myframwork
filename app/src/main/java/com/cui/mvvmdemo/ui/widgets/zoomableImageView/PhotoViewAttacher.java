//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cui.mvvmdemo.ui.widgets.zoomableImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.lang.ref.WeakReference;

class PhotoViewAttacher implements IPhotoView, OnTouchListener, VersionedGestureDetector.OnGestureListener, OnDoubleTapListener, OnGlobalLayoutListener {
    static final String LOG_TAG = "PhotoViewAttacher";
    static final boolean DEBUG = Log.isLoggable("PhotoViewAttacher", 3);
    static final int EDGE_NONE = -1;
    static final int EDGE_LEFT = 0;
    static final int EDGE_RIGHT = 1;
    static final int EDGE_BOTH = 2;
    public static final float DEFAULT_MAX_SCALE = 2.0F;
    public static final float DEFAULT_MIN_SCALE = 1.0F;
    private float mMinScale = 1.0F;
    private float mMaxScale = 2.0F;
    private boolean mAllowParentInterceptOnEdge = true;
    private WeakReference<ImageView> mImageView;
    private ViewTreeObserver mViewTreeObserver;
    private GestureDetector mGestureDetector;
    private VersionedGestureDetector mScaleDragDetector;
    private final Matrix mBaseMatrix = new Matrix();
    private final Matrix mDrawMatrix = new Matrix();
    private final Matrix mSuppMatrix = new Matrix();
    private final RectF mDisplayRect = new RectF();
    private final float[] mMatrixValues = new float[9];
    private PhotoViewAttacher.OnMatrixChangedListener mMatrixChangeListener;
    private PhotoViewAttacher.OnPhotoTapListener mPhotoTapListener;
    private PhotoViewAttacher.OnViewTapListener mViewTapListener;
    private OnLongClickListener mLongClickListener;
    private int mIvTop;
    private int mIvRight;
    private int mIvBottom;
    private int mIvLeft;
    private PhotoViewAttacher.FlingRunnable mCurrentFlingRunnable;
    private int mScrollEdge = 2;
    private boolean mZoomEnabled;
    private ScaleType mScaleType;

    private static void checkZoomLevels(float minZoom, float maxZoom) {
        if(minZoom >= maxZoom) {
            throw new IllegalArgumentException("MinZoom should be less than maxZoom");
        }
    }

    private static boolean hasDrawable(ImageView imageView) {
        return null != imageView && null != imageView.getDrawable();
    }

    private static boolean isSupportedScaleType(ScaleType scaleType) {
        if(null == scaleType) {
            return false;
        } else {
            switch(scaleType.ordinal()) {
                case 1:
                    throw new IllegalArgumentException(scaleType.name() + " is not supported in PhotoView");
                default:
                    return true;
            }
        }
    }

    private static void setImageViewScaleTypeMatrix(ImageView imageView) {
        if(null != imageView && !(imageView instanceof ZoomableImageView)) {
            imageView.setScaleType(ScaleType.MATRIX);
        }

    }

    public PhotoViewAttacher(ImageView imageView) {
        this.mScaleType = ScaleType.FIT_CENTER;
        this.mImageView = new WeakReference(imageView);
        imageView.setOnTouchListener(this);
        this.mViewTreeObserver = imageView.getViewTreeObserver();
        this.mViewTreeObserver.addOnGlobalLayoutListener(this);
        setImageViewScaleTypeMatrix(imageView);
        if(!imageView.isInEditMode()) {
            this.mScaleDragDetector = VersionedGestureDetector.newInstance(imageView.getContext(), this);
            this.mGestureDetector = new GestureDetector(imageView.getContext(), new SimpleOnGestureListener() {
                public void onLongPress(MotionEvent e) {
                    if(null != PhotoViewAttacher.this.mLongClickListener) {
                        PhotoViewAttacher.this.mLongClickListener.onLongClick((View)PhotoViewAttacher.this.mImageView.get());
                    }

                }
            });
            this.mGestureDetector.setOnDoubleTapListener(this);
            this.setZoomable(true);
        }

    }

    public final boolean canZoom() {
        return this.mZoomEnabled;
    }

    @SuppressLint({"NewApi"})
    public final void cleanup() {
        if(VERSION.SDK_INT >= 16) {
            if(null != this.mImageView) {
                ((ImageView)this.mImageView.get()).getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }

            if(null != this.mViewTreeObserver && this.mViewTreeObserver.isAlive()) {
                this.mViewTreeObserver.removeOnGlobalLayoutListener(this);
                this.mViewTreeObserver = null;
                this.mMatrixChangeListener = null;
                this.mPhotoTapListener = null;
                this.mViewTapListener = null;
                this.mImageView = null;
            }
        } else {
            if(null != this.mImageView) {
                ((ImageView)this.mImageView.get()).getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }

            if(null != this.mViewTreeObserver && this.mViewTreeObserver.isAlive()) {
                this.mViewTreeObserver.removeGlobalOnLayoutListener(this);
                this.mViewTreeObserver = null;
                this.mMatrixChangeListener = null;
                this.mPhotoTapListener = null;
                this.mViewTapListener = null;
                this.mImageView = null;
            }
        }

    }

    public final RectF getDisplayRect() {
        this.checkMatrixBounds();
        return this.getDisplayRect(this.getDisplayMatrix());
    }

    public final ImageView getImageView() {
        ImageView imageView = null;
        if(null != this.mImageView) {
            imageView = (ImageView)this.mImageView.get();
        }

        if(null == imageView) {
            this.cleanup();
            throw new IllegalStateException("ImageView no longer exists. You should not use this PhotoViewAttacher any more.");
        } else {
            return imageView;
        }
    }

    public float getMinScale() {
        return this.mMinScale;
    }

    public float getMaxScale() {
        return this.mMaxScale;
    }

    public final float getScale() {
        return this.getValue(this.mSuppMatrix, 0);
    }

    public final ScaleType getScaleType() {
        return this.mScaleType;
    }

    public final boolean onDoubleTap(MotionEvent ev) {
        try {
            float scale = this.getScale();
            float x = ev.getX();
            float y = ev.getY();
            if(scale < this.mMaxScale) {
                this.zoomTo(this.mMaxScale, x, y);
            } else {
                this.zoomTo(this.mMinScale, x, y);
            }
        } catch (ArrayIndexOutOfBoundsException var5) {
            ;
        }

        return true;
    }

    public final boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    public final void onDrag(float dx, float dy) {
        if(DEBUG) {
            Log.d("PhotoViewAttacher", String.format("onDrag: dx: %.2f. dy: %.2f", new Object[]{Float.valueOf(dx), Float.valueOf(dy)}));
        }

        ImageView imageView = this.getImageView();
        if(null != imageView && hasDrawable(imageView)) {
            this.mSuppMatrix.postTranslate(dx, dy);
            this.checkAndDisplayMatrix();
            if(this.mAllowParentInterceptOnEdge && !this.mScaleDragDetector.isScaling() && (this.mScrollEdge == 2 || this.mScrollEdge == 0 && dx >= 1.0F || this.mScrollEdge == 1 && dx <= -1.0F)) {
                imageView.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }

    }

    public final void onFling(float startX, float startY, float velocityX, float velocityY) {
        if(DEBUG) {
            Log.d("PhotoViewAttacher", "onFling. sX: " + startX + " sY: " + startY + " Vx: " + velocityX + " Vy: " + velocityY);
        }

        ImageView imageView = this.getImageView();
        if(hasDrawable(imageView)) {
            this.mCurrentFlingRunnable = new PhotoViewAttacher.FlingRunnable(imageView.getContext());
            this.mCurrentFlingRunnable.fling(imageView.getWidth(), imageView.getHeight(), (int)velocityX, (int)velocityY);
            imageView.post(this.mCurrentFlingRunnable);
        }

    }

    public final void onGlobalLayout() {
        ImageView imageView = this.getImageView();
        if(null != imageView && this.mZoomEnabled) {
            int top = imageView.getTop();
            int right = imageView.getRight();
            int bottom = imageView.getBottom();
            int left = imageView.getLeft();
            if(top != this.mIvTop || bottom != this.mIvBottom || left != this.mIvLeft || right != this.mIvRight) {
                this.updateBaseMatrix(imageView.getDrawable());
                this.mIvTop = top;
                this.mIvRight = right;
                this.mIvBottom = bottom;
                this.mIvLeft = left;
            }
        }

    }

    public final void onScale(float scaleFactor, float focusX, float focusY) {
        if(DEBUG) {
            Log.d("PhotoViewAttacher", String.format("onScale: scale: %.2f. fX: %.2f. fY: %.2f", new Object[]{Float.valueOf(scaleFactor), Float.valueOf(focusX), Float.valueOf(focusY)}));
        }

        if(hasDrawable(this.getImageView()) && (this.getScale() < this.mMaxScale || scaleFactor < 1.0F)) {
            this.mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
            this.checkAndDisplayMatrix();
        }

    }

    public final boolean onSingleTapConfirmed(MotionEvent e) {
        ImageView imageView = this.getImageView();
        if(null != imageView) {
            if(null != this.mPhotoTapListener) {
                RectF displayRect = this.getDisplayRect();
                if(null != displayRect) {
                    float x = e.getX();
                    float y = e.getY();
                    if(displayRect.contains(x, y)) {
                        float xResult = (x - displayRect.left) / displayRect.width();
                        float yResult = (y - displayRect.top) / displayRect.height();
                        this.mPhotoTapListener.onPhotoTap(imageView, xResult, yResult);
                        return true;
                    }
                }
            }

            if(null != this.mViewTapListener) {
                this.mViewTapListener.onViewTap(imageView, e.getX(), e.getY());
            }
        }

        return false;
    }

    public final boolean onTouch(View v, MotionEvent ev) {
        boolean handled = false;
        if(this.mZoomEnabled) {
            switch(ev.getAction()) {
                case 0:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    this.cancelFling();
                    break;
                case 1:
                case 3:
                    if(this.getScale() < this.mMinScale) {
                        RectF rect = this.getDisplayRect();
                        if(null != rect) {
                            v.post(new PhotoViewAttacher.AnimatedZoomRunnable(this.getScale(), this.mMinScale, rect.centerX(), rect.centerY()));
                            handled = true;
                        }
                    }
                case 2:
            }

            if(null != this.mGestureDetector && this.mGestureDetector.onTouchEvent(ev)) {
                handled = true;
            }

            if(null != this.mScaleDragDetector && this.mScaleDragDetector.onTouchEvent(ev)) {
                handled = true;
            }
        }

        return handled;
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        this.mAllowParentInterceptOnEdge = allow;
    }

    public void setMinScale(float minScale) {
        checkZoomLevels(minScale, this.mMaxScale);
        this.mMinScale = minScale;
    }

    public void setMaxScale(float maxScale) {
        checkZoomLevels(this.mMinScale, maxScale);
        this.mMaxScale = maxScale;
    }

    public final void setOnLongClickListener(OnLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public final void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
        this.mMatrixChangeListener = listener;
    }

    public final void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
        this.mPhotoTapListener = listener;
    }

    public final void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
        this.mViewTapListener = listener;
    }

    public final void setScaleType(ScaleType scaleType) {
        if(isSupportedScaleType(scaleType) && scaleType != this.mScaleType) {
            this.mScaleType = scaleType;
            this.update();
        }

    }

    public final void setZoomable(boolean zoomable) {
        this.mZoomEnabled = zoomable;
        this.update();
    }

    public final void update() {
        ImageView imageView = this.getImageView();
        if(null != imageView) {
            if(this.mZoomEnabled) {
                setImageViewScaleTypeMatrix(imageView);
                this.updateBaseMatrix(imageView.getDrawable());
            } else {
                this.resetMatrix();
            }
        }

    }

    public final void zoomTo(float scale, float focalX, float focalY) {
        ImageView imageView = this.getImageView();
        if(null != imageView) {
            imageView.post(new PhotoViewAttacher.AnimatedZoomRunnable(this.getScale(), scale, focalX, focalY));
        }

    }

    protected Matrix getDisplayMatrix() {
        this.mDrawMatrix.set(this.mBaseMatrix);
        this.mDrawMatrix.postConcat(this.mSuppMatrix);
        return this.mDrawMatrix;
    }

    private void cancelFling() {
        if(null != this.mCurrentFlingRunnable) {
            this.mCurrentFlingRunnable.cancelFling();
            this.mCurrentFlingRunnable = null;
        }

    }

    private void checkAndDisplayMatrix() {
        this.checkMatrixBounds();
        this.setImageViewMatrix(this.getDisplayMatrix());
    }

    private void checkImageViewScaleType() {
        ImageView imageView = this.getImageView();
        if(null != imageView && !(imageView instanceof ZoomableImageView) && imageView.getScaleType() != ScaleType.MATRIX) {
            throw new IllegalStateException("The ImageView's ScaleType has been changed since attaching a PhotoViewAttacher");
        }
    }

    private void checkMatrixBounds() {
        ImageView imageView = this.getImageView();
        if(null != imageView) {
            RectF rect = this.getDisplayRect(this.getDisplayMatrix());
            if(null != rect) {
                float height = rect.height();
                float width = rect.width();
                float deltaX = 0.0F;
                float deltaY = 0.0F;
                int viewHeight = imageView.getHeight();
                if(height <= (float)viewHeight) {
                    switch(this.mScaleType.ordinal()) {
                        case 2:
                            deltaY = -rect.top;
                            break;
                        case 3:
                            deltaY = (float)viewHeight - height - rect.top;
                            break;
                        default:
                            deltaY = ((float)viewHeight - height) / 2.0F - rect.top;
                    }
                } else if(rect.top > 0.0F) {
                    deltaY = -rect.top;
                } else if(rect.bottom < (float)viewHeight) {
                    deltaY = (float)viewHeight - rect.bottom;
                }

                int viewWidth = imageView.getWidth();
                if(width <= (float)viewWidth) {
                    switch(this.mScaleType.ordinal()) {
                        case 2:
                            deltaX = -rect.left;
                            break;
                        case 3:
                            deltaX = (float)viewWidth - width - rect.left;
                            break;
                        default:
                            deltaX = ((float)viewWidth - width) / 2.0F - rect.left;
                    }

                    this.mScrollEdge = 2;
                } else if(rect.left > 0.0F) {
                    this.mScrollEdge = 0;
                    deltaX = -rect.left;
                } else if(rect.right < (float)viewWidth) {
                    deltaX = (float)viewWidth - rect.right;
                    this.mScrollEdge = 1;
                } else {
                    this.mScrollEdge = -1;
                }

                this.mSuppMatrix.postTranslate(deltaX, deltaY);
            }
        }
    }

    private RectF getDisplayRect(Matrix matrix) {
        ImageView imageView = this.getImageView();
        if(null != imageView) {
            Drawable d = imageView.getDrawable();
            if(null != d) {
                this.mDisplayRect.set(0.0F, 0.0F, (float)d.getIntrinsicWidth(), (float)d.getIntrinsicHeight());
                matrix.mapRect(this.mDisplayRect);
                return this.mDisplayRect;
            }
        }

        return null;
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[whichValue];
    }

    private void resetMatrix() {
        this.mSuppMatrix.reset();
        this.setImageViewMatrix(this.getDisplayMatrix());
        this.checkMatrixBounds();
    }

    private void setImageViewMatrix(Matrix matrix) {
        ImageView imageView = this.getImageView();
        if(null != imageView) {
            this.checkImageViewScaleType();
            imageView.setImageMatrix(matrix);
            if(null != this.mMatrixChangeListener) {
                RectF displayRect = this.getDisplayRect(matrix);
                if(null != displayRect) {
                    this.mMatrixChangeListener.onMatrixChanged(displayRect);
                }
            }
        }

    }

    private void updateBaseMatrix(Drawable d) {
        ImageView imageView = this.getImageView();
        if(null != imageView && null != d) {
            float viewWidth = (float)imageView.getWidth();
            float viewHeight = (float)imageView.getHeight();
            int drawableWidth = d.getIntrinsicWidth();
            int drawableHeight = d.getIntrinsicHeight();
            this.mBaseMatrix.reset();
            float widthScale = viewWidth / (float)drawableWidth;
            float heightScale = viewHeight / (float)drawableHeight;
            if(this.mScaleType == ScaleType.CENTER) {
                this.mBaseMatrix.postTranslate((viewWidth - (float)drawableWidth) / 2.0F, (viewHeight - (float)drawableHeight) / 2.0F);
            } else {
                float scale;
                if(this.mScaleType == ScaleType.CENTER_CROP) {
                    scale = Math.max(widthScale, heightScale);
                    this.mBaseMatrix.postScale(scale, scale);
                    this.mBaseMatrix.postTranslate((viewWidth - (float)drawableWidth * scale) / 2.0F, (viewHeight - (float)drawableHeight * scale) / 2.0F);
                } else if(this.mScaleType == ScaleType.CENTER_INSIDE) {
                    scale = Math.min(1.0F, Math.min(widthScale, heightScale));
                    this.mBaseMatrix.postScale(scale, scale);
                    this.mBaseMatrix.postTranslate((viewWidth - (float)drawableWidth * scale) / 2.0F, (viewHeight - (float)drawableHeight * scale) / 2.0F);
                } else {
                    RectF mTempSrc = new RectF(0.0F, 0.0F, (float)drawableWidth, (float)drawableHeight);
                    RectF mTempDst = new RectF(0.0F, 0.0F, viewWidth, viewHeight);
                    switch(this.mScaleType.ordinal()) {
                        case 2:
                            this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.START);
                            break;
                        case 3:
                            this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.END);
                            break;
                        case 4:
                            this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER);
                            break;
                        case 5:
                            this.mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.FILL);
                    }
                }
            }

            this.resetMatrix();
        }
    }

    public float getMidScale() {
        return 0.0F;
    }

    public void setMidScale(float midScale) {
    }

    private class FlingRunnable implements Runnable {
        private final ScrollerProxy mScroller;
        private int mCurrentX;
        private int mCurrentY;

        public FlingRunnable(Context context) {
            this.mScroller = ScrollerProxy.getScroller(context);
        }

        public void cancelFling() {
            if(PhotoViewAttacher.DEBUG) {
                Log.d("PhotoViewAttacher", "Cancel Fling");
            }

            this.mScroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            RectF rect = PhotoViewAttacher.this.getDisplayRect();
            if(null != rect) {
                int startX = Math.round(-rect.left);
                int minX;
                int maxX;
                if((float)viewWidth < rect.width()) {
                    minX = 0;
                    maxX = Math.round(rect.width() - (float)viewWidth);
                } else {
                    maxX = startX;
                    minX = startX;
                }

                int startY = Math.round(-rect.top);
                int minY;
                int maxY;
                if((float)viewHeight < rect.height()) {
                    minY = 0;
                    maxY = Math.round(rect.height() - (float)viewHeight);
                } else {
                    maxY = startY;
                    minY = startY;
                }

                this.mCurrentX = startX;
                this.mCurrentY = startY;
                if(PhotoViewAttacher.DEBUG) {
                    Log.d("PhotoViewAttacher", "fling. StartX:" + startX + " StartY:" + startY + " MaxX:" + maxX + " MaxY:" + maxY);
                }

                if(startX != maxX || startY != maxY) {
                    this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
                }

            }
        }

        public void run() {
            ImageView imageView = PhotoViewAttacher.this.getImageView();
            if(null != imageView && this.mScroller.computeScrollOffset()) {
                int newX = this.mScroller.getCurrX();
                int newY = this.mScroller.getCurrY();
                if(PhotoViewAttacher.DEBUG) {
                    Log.d("PhotoViewAttacher", "fling run(). CurrentX:" + this.mCurrentX + " CurrentY:" + this.mCurrentY + " NewX:" + newX + " NewY:" + newY);
                }

                PhotoViewAttacher.this.mSuppMatrix.postTranslate((float)(this.mCurrentX - newX), (float)(this.mCurrentY - newY));
                PhotoViewAttacher.this.setImageViewMatrix(PhotoViewAttacher.this.getDisplayMatrix());
                this.mCurrentX = newX;
                this.mCurrentY = newY;
                Compat.postOnAnimation(imageView, this);
            }

        }
    }

    private class AnimatedZoomRunnable implements Runnable {
        static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07F;
        static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93F;
        private final float mFocalX;
        private final float mFocalY;
        private final float mTargetZoom;
        private final float mDeltaScale;

        public AnimatedZoomRunnable(float currentZoom, float targetZoom, float focalX, float focalY) {
            this.mTargetZoom = targetZoom;
            this.mFocalX = focalX;
            this.mFocalY = focalY;
            if(currentZoom < targetZoom) {
                this.mDeltaScale = 1.07F;
            } else {
                this.mDeltaScale = 0.93F;
            }

        }

        public void run() {
            ImageView imageView = PhotoViewAttacher.this.getImageView();
            if(null != imageView) {
                PhotoViewAttacher.this.mSuppMatrix.postScale(this.mDeltaScale, this.mDeltaScale, this.mFocalX, this.mFocalY);
                PhotoViewAttacher.this.checkAndDisplayMatrix();
                float currentScale = PhotoViewAttacher.this.getScale();
                if((this.mDeltaScale <= 1.0F || currentScale >= this.mTargetZoom) && (this.mDeltaScale >= 1.0F || this.mTargetZoom >= currentScale)) {
                    float delta = this.mTargetZoom / currentScale;
                    PhotoViewAttacher.this.mSuppMatrix.postScale(delta, delta, this.mFocalX, this.mFocalY);
                    PhotoViewAttacher.this.checkAndDisplayMatrix();
                } else {
                    Compat.postOnAnimation(imageView, this);
                }
            }

        }
    }

    public interface OnViewTapListener {
        void onViewTap(View var1, float var2, float var3);
    }

    public interface OnPhotoTapListener {
        void onPhotoTap(View var1, float var2, float var3);
    }

    public interface OnMatrixChangedListener {
        void onMatrixChanged(RectF var1);
    }
}
