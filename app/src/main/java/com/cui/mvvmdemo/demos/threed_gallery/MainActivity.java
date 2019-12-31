package com.cui.mvvmdemo.demos.threed_gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.cui.mvvmdemo.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

public class MainActivity extends AppCompatActivity {
    private ViewGroup layoutmain;
    private ViewGroup layoutnext;

    private Button btn_MainLast;
    private Button btn_MainNext;
    private Button btn_NextLast;
    private Button btn_NextNext;

    private Rotate3D lQuest1Animation;
    private Rotate3D lQuest2Animation;
    private Rotate3D rQuest1Animation;
    private Rotate3D rQuest2Animation;
    private int mCenterX = 160;        // 320x480 的宽一半
    private int mCenterY = 240;        // 320x480 的高一半

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
//        initAnimation();
//        initMain();

        Integer[] images = {R.mipmap.img0001, R.mipmap.img0030,
                R.mipmap.img0100, R.mipmap.img0130};

        ImageAdapter adapter = new ImageAdapter(this, images);
        adapter.createReflectedImages();//创建倒影效果
        GalleryFlow galleryFlow = (GalleryFlow) this.findViewById(R.id.Gallery01);
//        galleryFlow.setFadingEdgeLength(0);
        galleryFlow.setSpacing(10); //图片之间的间距
        galleryFlow.setAdapter(adapter);

        galleryFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), String.valueOf(position), Toast
                        .LENGTH_SHORT).show();
                Intent intent  = new Intent();
//                androidx.core.util.Pair<View, String> hileiaTransPair = new  androidx.core.util.Pair(view, "transitonName1");
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, hileiaTransPair);
                intent.putExtra("position",position);
//                intent.putExtras(options.toBundle());
                setResult(RESULT_OK,intent);
                finish();
            }

        });

        int position = getIntent().getIntExtra("position",2);
        galleryFlow.setSelection(position);
    }

//    private void initMain() {
//        setContentView(R.layout.main);
//
//        layoutmain = (LinearLayout) findViewById(R.id.layout_main);
//        btn_MainLast = (Button) findViewById(R.id.main_last);
//        btn_MainNext = (Button) findViewById(R.id.main_next);
//
//        btn_MainLast.setOnClickListener(listener);
//        btn_MainNext.setOnClickListener(listener);
//    }
//
//    private void initNext() {
//        setContentView(R.layout.next);
//
//        layoutnext = (LinearLayout) findViewById(R.id.layout_next);
//        btn_NextLast = (Button) findViewById(R.id.next_last);
//        btn_NextNext = (Button) findViewById(R.id.next_next);
//
//        btn_NextLast.setOnClickListener(listener);
//        btn_NextNext.setOnClickListener(listener);
//    }
//
//    private View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.main_last:    // 上一页
//                    layoutmain.startAnimation(lQuest1Animation);    // 当前页向左旋转（0，-90）
//                    initNext();
//                    layoutnext.startAnimation(lQuest2Animation);    // 下一页向左旋转（90, 0）
//                    break;
//                case R.id.main_next:    // 下一页
//                    layoutmain.startAnimation(rQuest1Animation);    // 当前页向右旋转（0，90）
//                    initNext();
//                    layoutnext.startAnimation(rQuest2Animation);    // 下一页向右旋转（-90, 0）
//                    break;
//                case R.id.next_last:
//                    layoutnext.startAnimation(lQuest1Animation);
//                    initMain();
//                    layoutmain.startAnimation(lQuest2Animation);
//                    break;
//                case R.id.next_next:
//                    layoutnext.startAnimation(rQuest1Animation);
//                    initMain();
//                    layoutmain.startAnimation(rQuest2Animation);
//                    break;
//            }
//        }
//    };

    public void initAnimation() {
        // 获取旋转中心
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        mCenterX = dm.widthPixels / 2;
        mCenterY = dm.heightPixels / 2;

        // 定义旋转方向
        int duration = 1000;
        lQuest1Animation = new Rotate3D(0, -90, mCenterX, mCenterY);    //
        // 下一页的【question1】旋转方向（从0度转到-90，参考系为水平方向为0度）
        lQuest1Animation.setFillAfter(true);
        lQuest1Animation.setDuration(duration);

        lQuest2Animation = new Rotate3D(90, 0, mCenterX, mCenterY);        //
        // 下一页的【question2】旋转方向（从90度转到0，参考系为水平方向为0度）（起始第一题）
        lQuest2Animation.setFillAfter(true);
        lQuest2Animation.setDuration(duration);

        rQuest1Animation = new Rotate3D(0, 90, mCenterX, mCenterY);        //
        // 上一页的【question1】旋转方向（从0度转到90，参考系为水平方向为0度）
        rQuest1Animation.setFillAfter(true);
        rQuest1Animation.setDuration(duration);

        rQuest2Animation = new Rotate3D(-90, 0, mCenterX, mCenterY);    //
        // 上一页的【question2】旋转方向（从-90度转到0，参考系为水平方向为0度）
        rQuest2Animation.setFillAfter(true);
        rQuest2Animation.setDuration(duration);
    }
}
