package com.cui.mvvmdemo.demos.recyclerViewTest;

import android.view.View;
import android.widget.TextView;

import com.cui.mvvmdemo.R;
import com.cui.mvvmdemo.base.BaseActivity;
import com.cui.mvvmdemo.bean.Girl;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public class RecyclerViewTransLayoutActivity extends BaseActivity {
    static String TAG = "TestSlidingUpActivity";
    static int count = 0;
    private RecyclerView mRecycler;
    TextView title_tv;
    private GridLayoutManager gridLayoutManager;//网格布局
    private LinearLayoutManager linearLayoutManager;//列表布局
    private RecyclerTransAdapter rvCommodityAdapter;//RecyclerView的适配器
    private List<Girl> girls;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler_test;
    }

    @Override
    protected void initView() {
        mRecycler = findViewById(R.id.trans_layout_rv);
        title_tv = findViewById(R.id.title);

        gridLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void initData() {
        /*--------------------RecyclerView初始化---------------------*/
        //1、设置布局管理器
        //=1.1、创建布局管理器
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)，列表布局才需要设置
        //=1.3、设置recyclerView的布局管理器，首次设置（相当于第一次进入时显示的布局）
        mRecycler.setLayoutManager(gridLayoutManager);
        //2、设置适配置
        girls = new ArrayList<>();
        //=2.1、初始化适配器
        rvCommodityAdapter = new RecyclerTransAdapter(girls, this);

        //=2.3 设置recyclerView的适配器
        mRecycler.setAdapter(rvCommodityAdapter);
        //3、添加android自带的分割线
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //4、设置增加或删除条目的动画
        mRecycler.setItemAnimator(new DefaultItemAnimator());

        //加载RecyclerView数据的方法
        initGirlsData();
    }

    private void initGirlsData(){
        Girl girl1 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/1.jpg");
        Girl girl2 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/2.jpg");
        Girl girl3 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/3.jpg");
        Girl girl4 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/4.jpg");
        Girl girl5 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/5.jpg");
        Girl girl6 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/6.jpg");
        Girl girl7 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/7.jpg");
        Girl girl8 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/8.jpg");
        Girl girl9 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/9.jpg");
        Girl girl10 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/10.jpg");
        Girl girl11 = new Girl("崔莹莹","模特，180cm,爱好健身跑步","file:///android_asset/11.jpg");
        girls.add(girl1);
        girls.add(girl2);
        girls.add(girl3);
        girls.add(girl4);
        girls.add(girl5);
        girls.add(girl6);
        girls.add(girl7);
        girls.add(girl8);
        girls.add(girl9);
        girls.add(girl10);
        girls.add(girl11);

        rvCommodityAdapter.notifyDataSetChanged();
    }


    int status = 0;
    @Override
    protected void initListener() {
        //切换布局点击事件
        title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 1) {
                    status = 0;
                    //1：设置布局类型
                    rvCommodityAdapter.setType(0);
                    //2：设置对应的布局管理器
                    mRecycler.setLayoutManager(gridLayoutManager);
                    //3：刷新adapter
                } else {
                    status = 1;
                    rvCommodityAdapter.setType(1);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecycler.setLayoutManager(linearLayoutManager);
                }
            }
        });
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void refreshView() {

    }


}
