package com.cui.mvvmdemo.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cui.mvvmdemo.R;

public class FocusLinearLayout extends LinearLayout {

    private AnimatorSet zoomInSet, zoomOutSet;
    private float zoom = 1.2f;
    String TAG_HILEIA_CUSTOM_VIEW = "custom_view";
    public FocusLinearLayout(Context context) {
        super(context);
    }

    public FocusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FocusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        setTag(TAG_HILEIA_CUSTOM_VIEW);
        if(attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.focusConstraintLayout);
            zoom = a.getFloat(R.styleable.focusConstraintLayout_zoom,1.3f);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            zoomOut();
        } else {
            zoomIn();
        }
    }

    private void zoomIn() {
        if (zoomInSet == null) {
            zoomInSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", zoom, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", zoom, 1f);
            zoomInSet.setDuration(200);
            zoomInSet.play(scaleX).with(scaleY);
        }
        zoomInSet.start();
    }

    private void zoomOut() {
        if (zoomOutSet == null) {
            zoomOutSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, zoom);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, zoom);
            zoomOutSet.setDuration(200);
            zoomOutSet.play(scaleX).with(scaleY);
        }
        zoomOutSet.start();
    }
}
