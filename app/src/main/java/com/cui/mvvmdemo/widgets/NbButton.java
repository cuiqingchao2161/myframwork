package com.cui.mvvmdemo.widgets;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Button;


import com.cui.mvvmdemo.R;

import androidx.core.content.ContextCompat;


/**
 * @author cuiqingchao
 * @date 2019/9/18
 * used 带加载动画按钮
 */
@SuppressLint("AppCompatCustomView")
public class NbButton extends Button {

    private int width;
    private int heigh;

    private GradientDrawable shape;
    boolean unable;
    //enable为false时的颜色
    int unableColor = R.color.button_normal_color;
    int colorId = R.color.color_primary_dark;
    int alpha = 255;
    Context mContext;

    private boolean isMorphing;
    private int startAngle;

    private Paint paint;
    private RectF rectF;
    private ValueAnimator arcValueAnimator;

    public NbButton(Context context) {
        super(context);
        init(context);
    }

    public NbButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NbButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        isMorphing = false;
        rectF = new RectF();
        //GradientDrawable为shape标签的代码实现
        shape = new GradientDrawable();

        if(unable){
            shape.setColor(ContextCompat.getColor(context, unableColor));
        }else {
            shape.setColor(ContextCompat.getColor(context, colorId));
        }
        shape.setAlpha(alpha);
        shape.setCornerRadius(20);
        //将GradientDrawable设置为背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(shape);
        } else {
            setBackgroundDrawable(shape);
        }

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(2);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if(pressed){
            shape.setAlpha((int) (alpha*0.6));
        }else{
            shape.setAlpha(alpha);
        }
        shape.setCornerRadius(20);
        //将GradientDrawable设置为背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(shape);
        } else {
            setBackgroundDrawable(shape);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        unable = !enabled;
        if(shape != null) {
            if(unable){
                shape.setColor(ContextCompat.getColor(mContext, unableColor));
            }else {
                shape.setColor(ContextCompat.getColor(mContext, colorId));
            }
            shape.setAlpha(alpha);
            shape.setCornerRadius(20);
            //将GradientDrawable设置为背景
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(shape);
            } else {
                setBackgroundDrawable(shape);
            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heighMode = MeasureSpec.getMode(heightMeasureSpec);
        int heighSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        if (heighMode == MeasureSpec.EXACTLY) {
            heigh = heighSize;
        }
    }

    public void startAnim() {
        setClickable(false);
        if (isMorphing) {
            return;
        } else {
            isMorphing = true;
        }

        setText("");
        ValueAnimator valueAnimator = ValueAnimator.ofInt(width, heigh);

        valueAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            int leftOffset = (width - value) / 2;
            int rightOffset = width - leftOffset;

            shape.setBounds(leftOffset, 0, rightOffset, heigh);
        });
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(shape, "cornerRadius", 120, heigh / 2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(valueAnimator, objectAnimator);
        animatorSet.start();

        //画中间的白色圆圈
        showArc();
    }

    public void gotoNew() {
        isMorphing = false;
        if(arcValueAnimator != null) {
            arcValueAnimator.cancel();
        }
        setVisibility(GONE);
    }

    public void regainBackground(String text) {
        isMorphing = false;
        setVisibility(VISIBLE);
        shape.setBounds(0,0,width,heigh);
        shape.setCornerRadius(10);
        setBackground(shape);
        setText(text);
        setClickable(true);
    }

    private void showArc() {
        arcValueAnimator = ValueAnimator.ofInt(0, 1080);
        arcValueAnimator.addUpdateListener(animation -> {
            startAngle = (int) animation.getAnimatedValue();
            invalidate();
        });
        arcValueAnimator.setInterpolator(new LinearInterpolator());
        arcValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        arcValueAnimator.setDuration(3000);
        arcValueAnimator.start();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (isMorphing) {
            rectF.top = getHeight() / 7;    //getHeight() / 7为白圈与边界的间距
            rectF.left = getWidth()/2 - getHeight() / 2 +  getHeight() / 7;

            rectF.right = getWidth()/2 + getHeight() / 2 - getHeight() / 7;
            rectF.bottom = getHeight() - getHeight() / 7;
            canvas.drawArc(rectF, startAngle, 270, false, paint);
        }
    }
}
