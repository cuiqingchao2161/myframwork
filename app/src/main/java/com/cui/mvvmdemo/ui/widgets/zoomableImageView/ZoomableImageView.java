package com.cui.mvvmdemo.ui.widgets.zoomableImageView;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/12/15.
 */

public class ZoomableImageView extends ImageView {
    private final PhotoViewAttacher mAttacher;
    private ScaleType mPendingScaleType;

    public ZoomableImageView(Context context) {
        this(context, (AttributeSet)null);
    }

    public ZoomableImageView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public ZoomableImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        super.setScaleType(ScaleType.MATRIX);
        this.mAttacher = new PhotoViewAttacher(this);
        if(null != this.mPendingScaleType) {
            this.setScaleType(this.mPendingScaleType);
            this.mPendingScaleType = null;
        }

    }

    public boolean canZoom() {
        return this.mAttacher.canZoom();
    }

    public RectF getDisplayRect() {
        return this.mAttacher.getDisplayRect();
    }

    public float getMinScale() {
        return this.mAttacher.getMinScale();
    }

    public float getMidScale() {
        return this.mAttacher.getMidScale();
    }

    public float getMaxScale() {
        return this.mAttacher.getMaxScale();
    }

    public float getScale() {
        return this.mAttacher.getScale();
    }

    public ScaleType getScaleType() {
        return this.mAttacher.getScaleType();
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        this.mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    public void setMinScale(float minScale) {
        this.mAttacher.setMinScale(minScale);
    }

    public void setMidScale(float midScale) {
        this.mAttacher.setMidScale(midScale);
    }

    public void setMaxScale(float maxScale) {
        this.mAttacher.setMaxScale(maxScale);
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if(null != this.mAttacher) {
            this.mAttacher.update();
        }

    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if(null != this.mAttacher) {
            this.mAttacher.update();
        }

    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if(null != this.mAttacher) {
            this.mAttacher.update();
        }

    }

    public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
        this.mAttacher.setOnMatrixChangeListener(listener);
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        this.mAttacher.setOnLongClickListener(l);
    }

    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
        this.mAttacher.setOnPhotoTapListener(listener);
    }

    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
        this.mAttacher.setOnViewTapListener(listener);
    }

    public void setScaleType(ScaleType scaleType) {
        if(null != this.mAttacher) {
            this.mAttacher.setScaleType(scaleType);
        } else {
            this.mPendingScaleType = scaleType;
        }

    }

    public void setZoomable(boolean zoomable) {
        this.mAttacher.setZoomable(zoomable);
    }

    public void zoomTo(float scale, float focalX, float focalY) {
        this.mAttacher.zoomTo(scale, focalX, focalY);
    }

    protected void onDetachedFromWindow() {
        this.mAttacher.cleanup();
        super.onDetachedFromWindow();
    }
}

