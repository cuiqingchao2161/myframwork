package com.cui.lib.widget.imageviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cui.lib.utils.DeviceUtil;
import com.cui.mycommonlibrary.R;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatImageView;

public class NiceImageView extends AppCompatImageView {
    private static final String TAG = "NiceImageView";
    private Context context;
    public static final int[] COLORS = {0xffffffff,
            0xff55ccdd,
            0xffbb7733,
            0xffff6655,
            0xffffbb44,
            0xff44aaff};

    private static final int COLORS_NUMBER = 1;
    private static final int DEFAULT_TEXT_COLOR = 0xffffff;
    private static final int DEFAULT_TYPE_BITMAP = 0;
    private static final int DEFAULT_TYPE_TEXT = 1;
    private static final String DEFAULT_TEXT = "";
    private static final float DEFAULT_TEXT_SIZE_RATIO = 0.4f;
    private static final float DEFAULT_TEXT_MASK_RATIO = 0.8f;
    private static final boolean DEFAULT_BORDER_SHOW = false;

    private boolean mIsCircle; // 是否显示为圆形，如果为圆形则设置的corner无效
    private boolean mIsCoverSrc; // border、inner_border是否覆盖图片
    private int mBorderWidth; // 边框宽度
    private int mBorderColor = DEFAULT_TEXT_COLOR; // 边框颜色
    private int mInnerBorderWidth; // 内层边框宽度
    private int mInnerBorderColor = DEFAULT_TEXT_COLOR; // 内层边框充色

    private int mCornerRadius; // 统一设置圆角半径，优先级高于单独设置每个角的半径
    private int mCornerTopLeftRadius; // 左上角圆角半径
    private int mCornerTopRightRadius; // 右上角圆角半径
    private int mCornerBottomLeftRadius; // 左下角圆角半径
    private int mCornerBottomRightRadius; // 右下角圆角半径

    private int mMaskColor; // 遮罩颜色
    private Drawable mDrawable;
    private int mType = DEFAULT_TYPE_BITMAP;
    private float mRadius;//the circle's radius
    private int mBgColor = COLORS[0];//background color when show text
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private float mTextSizeRatio = DEFAULT_TEXT_SIZE_RATIO;//the text size divides (2 * mRadius)
    private float mTextMaskRatio = DEFAULT_TEXT_MASK_RATIO;//the inner-radius text divides outer-radius text
    private boolean mShowBorder = DEFAULT_BORDER_SHOW;
    private String mText = DEFAULT_TEXT;


    private Xfermode xfermode;
    private Xfermode textFermode;

    private int width;
    private int height;
    private int mCenterX;
    private int mCenterY;
    private float[] borderRadii;
    private float[] srcRadii;

    private RectF srcRectF; // 图片占的矩形区域
    private RectF borderRectF; // 边框的矩形区域

    private Paint mPaintTextForeground;
    private Paint mPaintDraw;
    private Paint.FontMetrics mFontMetrics;
    private Path path; // 用来裁剪图片的ptah
    private Path srcPath; // 图片区域大小的path

    public NiceImageView(Context context) {
        super(context);
        init();
    }

    public NiceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public NiceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }


    private void initAttr(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NiceImageView, 0, 0);
        if (ta == null) {
            return;
        }
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.NiceImageView_is_cover_src) {
                mIsCoverSrc = ta.getBoolean(attr, mIsCoverSrc);
            } else if (attr == R.styleable.NiceImageView_is_circle) {
                mIsCircle = ta.getBoolean(attr, mIsCircle);
            } else if (attr == R.styleable.NiceImageView_border_width) {
                mBorderWidth = ta.getDimensionPixelSize(attr, mBorderWidth);
            } else if (attr == R.styleable.NiceImageView_border_color) {
                mBorderColor = ta.getColor(attr, mBorderColor);
            } else if (attr == R.styleable.NiceImageView_inner_border_width) {
                mInnerBorderWidth = ta.getDimensionPixelSize(attr, mInnerBorderWidth);
            } else if (attr == R.styleable.NiceImageView_inner_border_color) {
                mInnerBorderColor = ta.getColor(attr, mInnerBorderColor);
            } else if (attr == R.styleable.NiceImageView_corner_radius) {
                mCornerRadius = ta.getDimensionPixelSize(attr, mCornerRadius);
            } else if (attr == R.styleable.NiceImageView_corner_top_left_radius) {
                mCornerTopLeftRadius = ta.getDimensionPixelSize(attr, mCornerTopLeftRadius);
            } else if (attr == R.styleable.NiceImageView_corner_top_right_radius) {
                mCornerTopRightRadius = ta.getDimensionPixelSize(attr, mCornerTopRightRadius);
            } else if (attr == R.styleable.NiceImageView_corner_bottom_left_radius) {
                mCornerBottomLeftRadius = ta.getDimensionPixelSize(attr, mCornerBottomLeftRadius);
            } else if (attr == R.styleable.NiceImageView_corner_bottom_right_radius) {
                mCornerBottomRightRadius = ta.getDimensionPixelSize(attr, mCornerBottomRightRadius);
            } else if (attr == R.styleable.NiceImageView_mask_color) {
                mMaskColor = ta.getColor(attr, mMaskColor);
            } else if (attr == R.styleable.NiceImageView_text_size_ratio) {
                mTextSizeRatio = ta.getFloat(attr, DEFAULT_TEXT_SIZE_RATIO);
            } else if (attr == R.styleable.NiceImageView_text_mask_ratio) {
                mTextMaskRatio = ta.getFloat(attr, DEFAULT_TEXT_MASK_RATIO);
            } else if (attr == R.styleable.NiceImageView_text_color) {
                mTextColor = ta.getColor(attr, DEFAULT_TEXT_COLOR);
            } else if (attr == R.styleable.NiceImageView_is_show_border) {
                mShowBorder = ta.getBoolean(attr, DEFAULT_BORDER_SHOW);
            } else if (attr == R.styleable.NiceImageView_bg_color) {
                mBgColor = ta.getColor(attr, mBgColor);
            }
        }
        ta.recycle();
    }

    private void init() {
        borderRadii = new float[8];
        srcRadii = new float[8];

        borderRectF = new RectF();
        srcRectF = new RectF();

        mPaintDraw = new Paint();
        path = new Path();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        } else {
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
            srcPath = new Path();
        }

        textFermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

        calculateRadii();
        clearInnerBorderWidth();

        mPaintTextForeground = new Paint();
        mPaintTextForeground.setColor(mTextColor);
        mPaintTextForeground.setAntiAlias(true);
        mPaintTextForeground.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        initBorderRectF();
        initSrcRectF();

        refreshTextSizeConfig();
    }

    private void refreshTextSizeConfig() {
        mRadius = Math.min(width, height) / 2.0f;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        mCenterX = (int) (paddingLeft + mRadius);
        mCenterY = (int) (paddingTop + mRadius);

        mPaintTextForeground.setTextSize(mTextSizeRatio * 2 * mRadius);
        mFontMetrics = mPaintTextForeground.getFontMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawable != null && mType == DEFAULT_TYPE_BITMAP) {
            // 使用图形混合模式来显示指定区域的图片
            canvas.saveLayer(srcRectF, null, Canvas.ALL_SAVE_FLAG);
            if (!mIsCoverSrc) {
                float sx = 1.0f * (width - 2 * mBorderWidth - 2 * mInnerBorderWidth) / width;
                float sy = 1.0f * (height - 2 * mBorderWidth - 2 * mInnerBorderWidth) / height;
                // 缩小画布，使图片内容不被borders覆盖
                canvas.scale(sx, sy, width / 2.0f, height / 2.0f);
            }

            try {
                super.onDraw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPaintDraw.reset();
            path.reset();
            if (mIsCircle) {
                path.addCircle(width / 2.0f, height / 2.0f, mRadius, Path.Direction.CCW);
            } else {
                path.addRoundRect(srcRectF, srcRadii, Path.Direction.CCW);
            }

            mPaintDraw.setAntiAlias(true);
            mPaintDraw.setStyle(Paint.Style.FILL);
            mPaintDraw.setXfermode(xfermode);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                canvas.drawPath(path, mPaintDraw);
            } else {
                srcPath.reset();
                srcPath.addRect(srcRectF, Path.Direction.CCW);
                // 计算tempPath和path的差集
                srcPath.op(path, Path.Op.DIFFERENCE);
                canvas.drawPath(srcPath, mPaintDraw);
            }
            mPaintDraw.setXfermode(null);
            // 绘制遮罩
            if (mMaskColor != 0) {
                mPaintDraw.setColor(mMaskColor);
                canvas.drawPath(path, mPaintDraw);
            }
            // 恢复画布
            canvas.restore();
        } else if (mText != null && mType == DEFAULT_TYPE_TEXT) {
            toDrawText(canvas);
            // 绘制边框
            if (mShowBorder) {
                drawBorders(canvas);
            }
        }

    }

    private void toDrawText(Canvas canvas) {
        // 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(srcRectF, null, Canvas.ALL_SAVE_FLAG);
        if (!mIsCoverSrc) {
            float sx = 1.0f * (width - 2 * mBorderWidth - 2 * mInnerBorderWidth) / width;
            float sy = 1.0f * (height - 2 * mBorderWidth - 2 * mInnerBorderWidth) / height;
            // 缩小画布，使图片内容不被borders覆盖
            canvas.scale(sx, sy, width / 2.0f, height / 2.0f);
        }
        mPaintDraw.reset();
        path.reset();
        if (mIsCircle) {
            path.addCircle(width / 2.0f, height / 2.0f, mRadius, Path.Direction.CCW);
        } else {
            path.addRoundRect(srcRectF, srcRadii, Path.Direction.CCW);
        }

        mPaintDraw.setAntiAlias(true);
        mPaintDraw.setStyle(Paint.Style.FILL);
        mPaintDraw.setXfermode(xfermode);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            canvas.drawPath(path, mPaintDraw);
        } else {
            srcPath.reset();
            srcPath.addRect(srcRectF, Path.Direction.CCW);
            // 计算tempPath和path的差集
            srcPath.op(path, Path.Op.DIFFERENCE);
            canvas.drawPath(srcPath, mPaintDraw);
        }
        mPaintDraw.setXfermode(null);

        // 绘制背景
        if (mBgColor != 0) {
            mPaintDraw.setColor(mBgColor);
            canvas.drawPath(path, mPaintDraw);
        }
        mPaintTextForeground.setXfermode(textFermode);
        canvas.drawText(mText, 0, mText.length(), mCenterX, mCenterY + Math.abs(mFontMetrics.top + mFontMetrics.bottom) / 2, mPaintTextForeground);
        // 恢复画布
        canvas.restore();
    }

    public void setBorderShow(boolean flag) {
        mShowBorder = flag;
    }

    public void setTextAndColor(String text, int bgColor) {
        if (this.mType != DEFAULT_TYPE_TEXT || !stringEqual(text, this.mText) || bgColor != this.mBgColor) {
            this.mText = text;
//            this.mBgColor = bgColor;
            this.mType = DEFAULT_TYPE_TEXT;
            invalidate();
        }
    }

    public void setTextColor(int textColor) {
        if (this.mTextColor != textColor) {
            mTextColor = textColor;
            mPaintTextForeground.setColor(mTextColor);
            invalidate();
        }
    }

    public void setBgColor(int bgColor){
        if(this.mBgColor != bgColor){
            mBgColor = bgColor;
            invalidate();
        }
    }

    public void setTextAndColorSeed(String text, String colorSeed) {
        setTextAndColor(text, getColorBySeed(colorSeed));
    }

    private void drawBorders(Canvas canvas) {
        if (mIsCircle) {
            if (mBorderWidth > 0) {
                drawCircleBorder(canvas, mBorderWidth, mBorderColor, mRadius - mBorderWidth / 2.0f);
            }
            if (mInnerBorderWidth > 0) {
                drawCircleBorder(canvas, mInnerBorderWidth, mInnerBorderColor, mRadius - mBorderWidth - mInnerBorderWidth / 2.0f);
            }
        } else {
            if (mBorderWidth > 0) {
                drawRectFBorder(canvas, mBorderWidth, mBorderColor, borderRectF, borderRadii);
            }
        }
    }

    private void drawCircleBorder(Canvas canvas, int borderWidth, int borderColor, float radius) {
        initBorderPaint(borderWidth, borderColor);
        path.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CCW);
        canvas.drawPath(path, mPaintDraw);
    }

    private void drawRectFBorder(Canvas canvas, int borderWidth, int borderColor, RectF rectF, float[] radii) {
        initBorderPaint(borderWidth, borderColor);
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        canvas.drawPath(path, mPaintDraw);
    }

    private void initBorderPaint(int borderWidth, int borderColor) {
        path.reset();
        mPaintDraw.setStrokeWidth(borderWidth);
        mPaintDraw.setColor(borderColor);
        mPaintDraw.setStyle(Paint.Style.STROKE);
    }

    /**
     * 计算外边框的RectF
     */
    private void initBorderRectF() {
        if (!mIsCircle) {
            borderRectF.set(mBorderWidth / 2.0f, mBorderWidth / 2.0f, width - mBorderWidth / 2.0f, height - mBorderWidth / 2.0f);
        }
    }

    /**
     * 计算图片原始区域的RectF
     */
    private void initSrcRectF() {
        if (mIsCircle) {
            mRadius = Math.min(width, height) / 2.0f;
            srcRectF.set(width / 2.0f - mRadius, height / 2.0f - mRadius, width / 2.0f + mRadius, height / 2.0f + mRadius);
        } else {
            srcRectF.set(0, 0, width, height);
            if (mIsCoverSrc) {
                srcRectF = borderRectF;
            }
        }
    }

    /**
     * 计算RectF的圆角半径
     */
    private void calculateRadii() {
        if (mIsCircle) {
            return;
        }
        if (mCornerRadius > 0) {
            for (int i = 0; i < borderRadii.length; i++) {
                borderRadii[i] = mCornerRadius;
                srcRadii[i] = mCornerRadius - mBorderWidth / 2.0f;
            }
        } else {
            borderRadii[0] = borderRadii[1] = mCornerTopLeftRadius;
            borderRadii[2] = borderRadii[3] = mCornerTopRightRadius;
            borderRadii[4] = borderRadii[5] = mCornerBottomRightRadius;
            borderRadii[6] = borderRadii[7] = mCornerBottomLeftRadius;

            srcRadii[0] = srcRadii[1] = mCornerTopLeftRadius - mBorderWidth / 2.0f;
            srcRadii[2] = srcRadii[3] = mCornerTopRightRadius - mBorderWidth / 2.0f;
            srcRadii[4] = srcRadii[5] = mCornerBottomRightRadius - mBorderWidth / 2.0f;
            srcRadii[6] = srcRadii[7] = mCornerBottomLeftRadius - mBorderWidth / 2.0f;
        }
    }

    private void calculateRadiiAndRectF(boolean reset) {
        if (reset) {
            mCornerRadius = 0;
        }
        calculateRadii();
        initBorderRectF();
        invalidate();
    }

    public int getColorBySeed(String seed) {
        if (TextUtils.isEmpty(seed)) {
            return COLORS[0];
        }
        return COLORS[Math.abs(seed.hashCode() % COLORS_NUMBER)];
    }

    private boolean stringEqual(String a, String b) {
        if (a == null) {
            return (b == null);
        } else {
            if (b == null) {
                return false;
            }
            return a.equals(b);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (this.mType != DEFAULT_TYPE_BITMAP || drawable != this.mDrawable) {
            this.mDrawable = drawable;
            this.mType = DEFAULT_TYPE_BITMAP;
            invalidate();
        }
    }

    @Override
    public void setImageResource(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setImageDrawable(getContext().getDrawable(resId));
        } else {
            setImageDrawable(getContext().getResources().getDrawable(resId));
        }
    }

    /**
     * 目前圆角矩形情况下不支持inner_border，需要将其置0
     */
    private void clearInnerBorderWidth() {
        if (!mIsCircle) {
            this.mInnerBorderWidth = 0;
        }
    }

    public void isCoverSrc(boolean isCoverSrc) {
        this.mIsCoverSrc = isCoverSrc;
        initSrcRectF();
        invalidate();
    }

    public void isCircle(boolean isCircle) {
        this.mIsCircle = isCircle;
        clearInnerBorderWidth();
        initSrcRectF();
        invalidate();
    }

    public void setmBorderWidth(int mBorderWidth) {
        this.mBorderWidth = DeviceUtil.dip2px(mBorderWidth);
        calculateRadiiAndRectF(false);
    }

    public void setmBorderColor(@ColorInt int mBorderColor) {
        this.mBorderColor = mBorderColor;
        invalidate();
    }

    public void setmInnerBorderWidth(int mInnerBorderWidth) {
        this.mInnerBorderWidth = DeviceUtil.dip2px(mInnerBorderWidth);
        clearInnerBorderWidth();
        invalidate();
    }

    public void setmInnerBorderColor(@ColorInt int mInnerBorderColor) {
        this.mInnerBorderColor = mInnerBorderColor;
        invalidate();
    }

    public void setmCornerRadius(int mCornerRadius) {
        this.mCornerRadius = DeviceUtil.dip2px(mCornerRadius);
        calculateRadiiAndRectF(false);
    }

    public void setmCornerTopLeftRadius(int mCornerTopLeftRadius) {
        this.mCornerTopLeftRadius = DeviceUtil.dip2px(mCornerTopLeftRadius);
        calculateRadiiAndRectF(true);
    }

    public void setmCornerTopRightRadius(int mCornerTopRightRadius) {
        this.mCornerTopRightRadius = DeviceUtil.dip2px(mCornerTopRightRadius);
        calculateRadiiAndRectF(true);
    }

    public void setmCornerBottomLeftRadius(int mCornerBottomLeftRadius) {
        this.mCornerBottomLeftRadius = DeviceUtil.dip2px(mCornerBottomLeftRadius);
        calculateRadiiAndRectF(true);
    }

    public void setmCornerBottomRightRadius(int mCornerBottomRightRadius) {
        this.mCornerBottomRightRadius = DeviceUtil.dip2px(mCornerBottomRightRadius);
        calculateRadiiAndRectF(true);
    }

    public void setmMaskColor(@ColorInt int mMaskColor) {
        this.mMaskColor = mMaskColor;
        invalidate();
    }
}
