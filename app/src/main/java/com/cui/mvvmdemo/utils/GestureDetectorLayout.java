package com.cui.mvvmdemo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cui.mvvmdemo.R;

import java.util.concurrent.TimeUnit;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xujiangang
 * @date 15/03/2019
 * Email: jiangang.xu@hiscene.com
 */
public class GestureDetectorLayout extends ConstraintLayout {

    private static final String TAG = "GestureDetectorLayout";
    // ************event*********************

    public static final int EVENT_MOVE_UP = 1;
    public static final int EVENT_MOVE_RIGHT_UP = 2;
    public static final int EVENT_MOVE_RIGHT = 3;
    public static final int EVENT_MOVE_RIGHT_DOWN = 4;
    public static final int EVENT_MOVE_DOWN = 5;
    public static final int EVENT_MOVE_LEFT_DOWN = 6;
    public static final int EVENT_MOVE_LEFT = 7;
    public static final int EVENT_MOVE_LEFT_UP = 8;
    public static final int EVENT_CLICK = 9;
    public static final int EVENT_CLICK_DOUBLE = 10;
    public static final int EVENT_CLICK_LONG = 11;

    // 180°分成8个刻度，每个刻度为22.5°
    private final float DEGREE_UNIT = 22.5f;
    // 左右滑动提示小图标由高亮到正常的时间
    private final int HINT_DEFAULT_DELAY = 500;
    // 最小滑动距离
    private final int HORIZONTAL_FLIP_DISTANCE = 15;
    private final int QUICK_SCROLL_HORIZONTAL_FLIP_DISTANCE = 250;
    private final int VERTICAL_FLIP_DISTANCE = 15;

    private Context mContext;
    private ImageView imgLeft;
    private ImageView imgRight;
    private boolean isShowHint = false;
    private boolean isShowMenu = false;
    private boolean isQuickScrollEnabled;
    private TouchListener mTouchListener;
    private LongClickListener mLongClickListener;
    private String TAG_HILEIA_CUSTOM_VIEW = "custom_view";

    public GestureDetectorLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public GestureDetectorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        this.setClickable(true);
        this.setLongClickable(true);
        this.setFocusable(false);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.GestureDetectorLayout);
        if (attributes != null) {
            isShowHint = attributes.getBoolean(R.styleable.GestureDetectorLayout_is_show_hint, false);
            isShowMenu = attributes.getBoolean(R.styleable.GestureDetectorLayout_is_show_menu, false);
            attributes.recycle();
        }

//        if (isShowHint) {
//            View view = View.inflate(context, R.layout.hint_direction, null);
//            if (view != null) {
//                imgLeft = view.findViewById(R.id.img_left);
//                imgRight = view.findViewById(R.id.img_right);
//
//                addView(view);
//                view.setLayoutParams(new LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
//            }
//        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 判断并执行action
        if (mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

//    private void showLeftHint() {
//        imgLeft.setImageResource(R.drawable.move_start_shape_enable);
//        final Observable<Integer> observable = Observable.create(e -> {
//            e.onNext(0);
//            e.onComplete();
//        });
//        DisposableObserver<Integer> disposableObserver = new DisposableObserver<Integer>() {
//
//            @Override
//            public void onNext(Integer value) {
//                imgLeft.setImageResource(R.drawable.move_start_shape_unable);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                imgLeft.setImageResource(R.drawable.move_start_shape_unable);
//            }
//
//            @Override
//            public void onComplete() {
//                imgLeft.setImageResource(R.drawable.move_start_shape_unable);
//            }
//        };
//        observable.delay(HINT_DEFAULT_DELAY, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
//    }

//    private void showRightHint() {
//        imgRight.setImageResource(R.drawable.move_end_shape_enable);
//        final Observable<Integer> observable = Observable.create(e -> {
//            e.onNext(0);
//            e.onComplete();
//        });
//        DisposableObserver<Integer> disposableObserver = new DisposableObserver<Integer>() {
//
//            @Override
//            public void onNext(Integer value) {
//                imgRight.setImageResource(R.drawable.move_end_shape_unable);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                imgRight.setImageResource(R.drawable.move_end_shape_unable);
//            }
//
//            @Override
//            public void onComplete() {
//                imgRight.setImageResource(R.drawable.move_end_shape_unable);
//            }
//        };
//        observable.delay(HINT_DEFAULT_DELAY, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
//    }

    /**
     * 手势处理
     */
    private GestureDetector mDetector = new GestureDetector(mContext,
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    //Log4aUtil.d(TAG, "onDown 按下！");
                    isQuickScrollEnabled = false;
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    //Log4aUtil.d(TAG, "onLongPress 长按！");
                    if (mLongClickListener != null) {
                        mLongClickListener.onLongPress();
                    }
                    super.onLongPress(e);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (isQuickScrollEnabled) {
                        // 获取手指滑动的方向
                        int eventDirection = getQuickEventDirection(e2);
                        switch (eventDirection) {
                            case EVENT_MOVE_UP:
                            case EVENT_MOVE_RIGHT_UP:
                            case EVENT_MOVE_LEFT_UP:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveUp();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_UP);
                                }
                                break;

                            case EVENT_MOVE_DOWN:
                            case EVENT_MOVE_RIGHT_DOWN:
                            case EVENT_MOVE_LEFT_DOWN:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveDown();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_DOWN);
                                }
                                break;

                            case EVENT_MOVE_LEFT:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveLeft();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_LEFT);
                                    if (isShowHint) {
//                                        showLeftHint();
                                    }
                                }
                                break;

                            case EVENT_MOVE_RIGHT:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveRight();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_RIGHT);
                                    if (isShowHint) {
//                                        showRightHint();
                                    }
                                }
                                break;
                        }
                    }
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    isQuickScrollEnabled = true;
                    lastPoint.x = e.getX();
                    lastPoint.y = e.getY();
                    super.onShowPress(e);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (!isQuickScrollEnabled) {
                        // 获取手指滑动的方向
                        int eventDirection = getEventDirection(e1, e2);
                        switch (eventDirection) {
                            case EVENT_MOVE_UP:
                            case EVENT_MOVE_RIGHT_UP:
                            case EVENT_MOVE_LEFT_UP:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveUp();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_UP);
                                }
                                break;

                            case EVENT_MOVE_DOWN:
                            case EVENT_MOVE_RIGHT_DOWN:
                            case EVENT_MOVE_LEFT_DOWN:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveDown();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_DOWN);
                                }
                                break;

                            case EVENT_MOVE_LEFT:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveLeft();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_LEFT);
                                    if (isShowHint) {
//                                        showLeftHint();
                                    }
                                }
                                break;

                            case EVENT_MOVE_RIGHT:
                                if (mTouchListener != null) {
                                    mTouchListener.onMoveRight();
                                } else {
                                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_MOVE_RIGHT);
                                    if (isShowHint) {
//                                        showRightHint();
                                    }
                                }
                                break;
                        }
                    }
                    return false;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    //Log4aUtil.d(TAG, "onDoubleTap 双击！");
                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_ENTER);
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    //Log4aUtil.d(TAG, "onSingleTapConfirmed 单击！");
                    sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_ENTER);
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //Log4aUtil.d(TAG, "onSingleTapUp 单击！");
                    return true;
                }
            });

    /**
     * 判断当前的移动方向
     *
     * @param e1
     * @param e2
     */
    private int getEventDirection(MotionEvent e1, MotionEvent e2) {
        int event = -1;

        float x1 = e1.getX();
        float y1 = e1.getY();
        float x2 = e2.getX();
        float y2 = e2.getY();
        float a = x2 - x1;// a边为对边 x2-x1
        float b = y2 - y1;// b边为邻边 y2-y1
        // 反正切得到∠B弧度
        double B_radian = Math.atan(a / b);
        // 弧度转换成角度
        B_radian = Math.toDegrees(B_radian);
        // 以x轴为分界线，以180°计算，算出的角度如果为负数，+180
        B_radian = B_radian < 0 ? B_radian + 180 : B_radian;

        int flip_distance = 0;
        if (isQuickScrollEnabled) {
            flip_distance = QUICK_SCROLL_HORIZONTAL_FLIP_DISTANCE;
        } else {
            flip_distance = HORIZONTAL_FLIP_DISTANCE;
        }

        if (Math.abs(a) >= flip_distance || Math.abs(b) >= VERTICAL_FLIP_DISTANCE) {
            // 当手指往x轴的正方向移动的时候，y轴正方向为0°，反方向为180°
            if (x2 > x1) {
                if (B_radian < DEGREE_UNIT) {
                    //Log4aUtil.d(TAG, "下 ：" + B_radian);
                    event = EVENT_MOVE_DOWN;
                } else if (B_radian >= DEGREE_UNIT && B_radian < DEGREE_UNIT * 3) {
                    //Log4aUtil.d(TAG, "右下 ：" + B_radian);
                    event = EVENT_MOVE_RIGHT_DOWN;
                } else if (B_radian >= DEGREE_UNIT * 3 && B_radian < DEGREE_UNIT * 5) {
                    //Log4aUtil.d(TAG, "右 ：" + B_radian);
                    event = EVENT_MOVE_RIGHT;
                } else if (B_radian >= DEGREE_UNIT * 5 && B_radian < DEGREE_UNIT * 7) {
                    //Log4aUtil.d(TAG, "右上 ：" + B_radian);
                    event = EVENT_MOVE_RIGHT_UP;
                } else if (B_radian >= DEGREE_UNIT * 7) {
                    //Log4aUtil.d(TAG, "上 ：" + B_radian);
                    event = EVENT_MOVE_UP;
                }
            } else if (x2 < x1) {
                // 当手指往x轴的反方向移动的时候y轴反方向为0°，正方向为180°
                if (B_radian < DEGREE_UNIT) {
                    //Log4aUtil.d(TAG, "上 ：" + B_radian);
                    event = EVENT_MOVE_UP;
                } else if (B_radian >= DEGREE_UNIT && B_radian < DEGREE_UNIT * 3) {
                    //Log4aUtil.d(TAG, "左上 ：" + B_radian);
                    event = EVENT_MOVE_LEFT_UP;
                } else if (B_radian >= DEGREE_UNIT * 3 && B_radian < DEGREE_UNIT * 5) {
                    //Log4aUtil.d(TAG, "左 ：" + B_radian);
                    event = EVENT_MOVE_LEFT;
                } else if (B_radian >= DEGREE_UNIT * 5 && B_radian < DEGREE_UNIT * 7) {
                    //Log4aUtil.d(TAG, "左下 ：" + B_radian);
                    event = EVENT_MOVE_LEFT_DOWN;
                } else if (B_radian >= DEGREE_UNIT * 7) {
                    //Log4aUtil.d(TAG, "下 ：" + B_radian);
                    event = EVENT_MOVE_DOWN;
                }
            } else {
                // 如果正好x轴不动，y轴移动
                if (y2 > y1) {
                    //Log4aUtil.d(TAG, "下 ：" + B_radian);
                    event = EVENT_MOVE_DOWN;
                } else if (y2 < y1) {
                    //Log4aUtil.d(TAG, "上 ：" + B_radian);
                    event = EVENT_MOVE_UP;
                }
            }
        }
        return event;
    }

    /**
     * 判断快速滑动的移动方向
     *
     * @param e1
     * @param e2
     */
    private long lastQuickTime = 0;
    private PointF lastPoint = new PointF();

    private int getQuickEventDirection(MotionEvent e2) {
        int event = -1;
        float x1 = lastPoint.x;
        float y1 = lastPoint.y;
        float x2 = e2.getX();
        float y2 = e2.getY();
        float a = Math.abs(x2 - x1);// a边为对边 x2-x1
        float b = Math.abs(y2 - y1);// b边为邻边 y2-y1
        // 反正切得到∠B弧度
        double atan = Math.atan(a / b);
        // 弧度转换成角度
        atan = Math.toDegrees(atan);
        long duration = System.currentTimeMillis() - lastQuickTime;
//        XLog.i(TAG,"getQuickEventDirection: a %f b %f atan %f",a,b,atan);
        if (a > HORIZONTAL_FLIP_DISTANCE && Math.abs(atan - 90) < 15 && duration > 100) {
            if (x2 > x1) {
                event = EVENT_MOVE_RIGHT;
            } else if (x2 < x1) {
                event = EVENT_MOVE_LEFT;
            }
            lastQuickTime = System.currentTimeMillis();
            lastPoint.x = e2.getX();
            lastPoint.y = e2.getY();
        }
        return event;
    }

    public interface TouchListener {
        void onMoveLeft();

        void onMoveRight();

        void onMoveDown();

        void onMoveUp();
    }

    public interface LongClickListener {
        void onLongPress();
    }

    public void setTouchListener(TouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }

    private void sendFakeKeyEvent(int code) {
        if (isShowMenu) {
            lastFocusedView = getFocusedChild(this);
        }
        FakeKeyEventUtils.sendFakeKeyEvent(code);
        if (isShowMenu && (code == FakeKeyEventUtils.TOUCH_MOVE_LEFT || code == FakeKeyEventUtils.TOUCH_MOVE_RIGHT)) {
            postDelayed(() -> showMenu(), 66);
        }
    }

    /****************************** 页面中唤出导航相关 ***************************/
    private View lastFocusedView = null;

    private void showMenu() {
        View focusedView = getFocusedChild(this);
        if (focusedView == lastFocusedView) {
            sendFakeKeyEvent(FakeKeyEventUtils.TOUCH_NAVIGATOR);
        }
        lastFocusedView = focusedView;
    }

    private View getFocusedChild(ViewGroup viewGroup) {
        View view = viewGroup.getFocusedChild();
        if (view != null && view.getTag() == TAG_HILEIA_CUSTOM_VIEW) {
            return view;
        } else if (view instanceof ViewGroup) {
            return getFocusedChild((ViewGroup) view);
        } else {
            return null;
        }
    }
}
