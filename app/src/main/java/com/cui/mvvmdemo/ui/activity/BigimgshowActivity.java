package com.cui.mvvmdemo.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.cui.mvvmdemo.R;
import com.cui.mvvmdemo.bean.Girl;
import com.cui.mvvmdemo.utils.ImageUtil;
import com.cui.lib.utils.NewStatusBarUtil;
import com.cui.mvvmdemo.ui.widgets.zoomableImageView.ZoomableImageView;
import com.cui.mvvmdemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Administrator on 2018/12/15.
 */

public class BigimgshowActivity extends BaseActivity {
    protected ProgressDialog mProgressDialog;
    protected InputMethodManager inputMethodManager;
    private static final String TAG = "ShowBigImage";
    private ProgressDialog pd;
    private ZoomableImageView image;
    private int default_res = R.mipmap.zhaoxi;
    private String secret;
    private Bitmap bitmap;
    private boolean isDownloaded;
    private ProgressBar loadLocalPb;
    private Button mBackBtn;
//    private Button mRightBtn;
//    private TextView titleName;
    private List<Girl> imageInfoList;
    private int currentPosition = 1;
    private int totalCount = 0;
    private ViewPager viewPager;
    private ImgAdapter imgAdapter;

    public BigimgshowActivity() {
    }

    protected void showProgressDialog(Context context, String msg) {
        this.mProgressDialog = this.mProgressDialog == null?new ProgressDialog(context):this.mProgressDialog;
        this.mProgressDialog.setTitle((CharSequence)null);
        this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getNewIntentData();
    }

    private void getNewIntentData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            currentPosition = bundle.getInt("currentPosition");
            imageInfoList = (List<Girl>) bundle.getSerializable("imageInfoList");
        }

        totalCount = imageInfoList.size();
//        titleName.setText(currentPosition+"/"+totalCount);
        imgAdapter = new ImgAdapter(this,imageInfoList);
        viewPager.setAdapter(imgAdapter);
        viewPager.setCurrentItem(currentPosition);//设置起始位置
//        viewPager.setPageTransformer(true, new DepthPageTransformer());//修改动画效果

//        loadImage();

//        setGestureListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NewStatusBarUtil.setStatusBarColor(this,R.color.transparent);//设置状态栏颜色和顶部布局背景色一致
        NewStatusBarUtil.setStatusBarTextColor(this,true);
    }

    private void loadImage(){
        //show the image if it exist in local path
        if(currentPosition>totalCount){
            return;
        }
        Girl imageInfo = imageInfoList.get(currentPosition-1);
        String bigImageUrl = imageInfo.getPicPath();

        if (TextUtils.isEmpty(bigImageUrl)) {
            // to make it compatible with thumbnail received in previous version
            bigImageUrl = imageInfo.getPicPath();

        }

        if(TextUtils.isEmpty(bigImageUrl)){
            image.setImageResource(default_res);
        }else {
            Glide.with(this).load(bigImageUrl).apply(ImageUtil.getOption()).into(image);
        }
    }

    private float mPosX,mPosY,mCurPosX,mCurPosY;
    private void setGestureListener(){
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();

                        break;
                    case MotionEvent.ACTION_UP:
                        if (mCurPosY - mPosY > 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向下滑動
                        } else if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 25)) {
                            //向上滑动
                        }

                        if (mCurPosX - mPosX > 0
                                && (Math.abs(mCurPosX - mPosX) > 25)) {
                            //向右滑動
                            if(currentPosition > 1){
                                switchImage(1);
                            }
                        } else if (mCurPosX - mPosX < 0
                                && (Math.abs(mCurPosX - mPosX) > 25)) {
                            //向左滑动
                            if(currentPosition < totalCount){
                                switchImage(2);
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }


    /**
     * 淡出的动画，同时监听动画状态，动画完成后执行淡入动画
     */
    private void disappearAnim() {
        AlphaAnimation anim = new AlphaAnimation(1, 0);
        anim.setDuration(50);
        anim.setFillAfter(true);
        image.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(bitmap == null){
                    return;
                }
                image.setImageBitmap(bitmap);
                showAnim();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 淡入的动画
     */
    private void showAnim() {
        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(50);
        anim.setFillAfter(true);
        image.startAnimation(anim);
    }

    private void switchImage(int action){

        if(action == 1){
            currentPosition --;
        }else if(action == 2){
            currentPosition ++;
        }
//        titleName.setText(currentPosition+"/"+totalCount);

        loadImage();
    }

    @Override
    public void onBackPressed() {
        if (isDownloaded) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_bigimgshow_activity;
    }

    @Override
    protected void initView() {

        if(!this.isTaskRoot()) {
            Intent intent = this.getIntent();
            String action = intent.getAction();
            if(intent.hasCategory("android.intent.category.LAUNCHER") && action.equals("android.intent.action.MAIN")) {
                this.finish();
                return;
            }
        }

        imageInfoList = new ArrayList<>();

        image = (ZoomableImageView) findViewById(R.id.image);
        image.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setVisibility(View.VISIBLE);

        loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
        default_res = getIntent().getIntExtra("default_image", R.mipmap.zhaoxi);
        mBackBtn = (Button) findViewById(R.id.btn_activity_title_layout_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getNewIntentData();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void refreshView() {

    }

    public class ImgAdapter extends PagerAdapter {
        private List<Girl> imgs;
        private Context context;

        public ImgAdapter(Context context, List<Girl> imgs) {
            this.context = context;
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//及时销毁界面，防止内存溢出
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {//必须实现
            View view = LayoutInflater.from(context).inflate(R.layout.layout_bigimgshow_activity,container,false);
            ImageView imageView = (ZoomableImageView) view.findViewById(R.id.image);
            Glide.with(BigimgshowActivity.this).load(imgs.get(position).getPicPath()).apply(ImageUtil.getOption()).into(imageView);

            container.addView(view);
            return view;
        }

    }


    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
//    private class MyGuestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                               float velocityY) {
//            int position = ga.getSelectedItemPosition();
//            if (e1.getRawX() - e2.getRawX() > 20) {
//                if (++currentPosition == totalCount) {// 自右向左
//                    position = 0;
//                }
//                is.setInAnimation(AnimationUtils.loadAnimation(
//                        BigimgshowActivity.this, R.anim.slide_in_right));
//                is.setOutAnimation(AnimationUtils.loadAnimation(
//                        BigimgshowActivity.this, R.anim.slide_out_left));
//            } else if (e2.getRawX() - e1.getRawX() > 20) {// //前一张
//                if (--position < 0) {totalCount.getCount() - 1;
//                }
//                is.setInAnimation(AnimationUtils.loadAnimation(
//                        BigimgshowActivity.this, android.R.anim.slide_in_left));
//                is.setOutAnimation(AnimationUtils.loadAnimation(
//                        BigimgshowActivity.this, android.R.anim.slide_out_right));
//
//            }
//            ga.setSelection(position);
//
//            return super.onFling(e1, e2, velocityX, velocityY);
//        }
//
//    }
}
