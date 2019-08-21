//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cui.mvvmdemo.widgets.zoomableImageView;

import android.graphics.RectF;
import android.view.View.OnLongClickListener;
import android.widget.ImageView.ScaleType;

public interface IPhotoView {
    boolean canZoom();

    RectF getDisplayRect();

    float getMinScale();

    float getMidScale();

    float getMaxScale();

    float getScale();

    ScaleType getScaleType();

    void setAllowParentInterceptOnEdge(boolean var1);

    void setMinScale(float var1);

    void setMidScale(float var1);

    void setMaxScale(float var1);

    void setOnLongClickListener(OnLongClickListener var1);

    void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener var1);

    void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener var1);

    void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener var1);

    void setScaleType(ScaleType var1);

    void setZoomable(boolean var1);

    void zoomTo(float var1, float var2, float var3);
}
