package com.cui.mvvmdemo.demos.pullUpAmination;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cui.mvvmdemo.R;
import com.cui.lib.base.BaseActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 *
 */
public class TestSlidingUpActivity extends BaseActivity {
    static String TAG = "TestSlidingUpActivity";
    static int count = 0;
    private SlidingUpPanelLayout mLayout;
    private ImageView pullIV;
    private Button button;
    private LinearLayout list;
    TextView simple_tv;
    private View hatView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sliding_up;
    }

    @Override
    protected void initView() {
        hatView = findViewById(R.id.hat_view);
        pullIV = findViewById(R.id.pull_imageview);
        simple_tv = findViewById(R.id.simple);
        list = findViewById(R.id.list);
        button = findViewById(R.id.btn);

        mLayout = findViewById(R.id.sliding_layout);
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mLayout.setCoveredFadeColor(Color.parseColor("#00000000"));

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                simple_tv.setAlpha((1-slideOffset) * 255);
                list.setAlpha(slideOffset * 255);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(mLayout.getPanelState().toString().equals(SlidingUpPanelLayout.PanelState.COLLAPSED.toString())){
                    pullIV.setImageResource(R.mipmap.pull_up);
                    hatView.setVisibility(View.GONE);
                    simple_tv.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                }else if(mLayout.getPanelState().toString().equals(SlidingUpPanelLayout.PanelState.EXPANDED.toString())||mLayout.getPanelState().toString().equals(SlidingUpPanelLayout.PanelState.DRAGGING.toString())){
                    pullIV.setImageResource(R.mipmap.pull_down);
                    hatView.setVisibility(View.VISIBLE);
                    simple_tv.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                }
            }
        });
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


}
