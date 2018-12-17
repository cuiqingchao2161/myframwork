package com.zx.mvvmdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zx.mvvmdemo.R;
import com.zx.mvvmdemo.bean.Girl;
import com.zx.mvvmdemo.view.BigimgshowActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzx on 17/8/29.
 */
public class GirlsAdapter extends BaseQuickAdapter<Girl, BaseViewHolder> {

    private Context mContext;
    private List<Girl> mList;

    public GirlsAdapter(Context context, List<Girl> list) {
        super(R.layout.girl_layout, list);
        this.mContext = context;
        mList = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, Girl item) {
        if (item != null) {
            Glide.with(mContext).load(item.getPicPath()).placeholder(R.mipmap.zhaoxi).into((ImageView) helper.getView(R.id.girl_iv));
            helper.setText(R.id.girl_name, item.getName());
            helper.setText(R.id.girl_introduction, item.getIntroduction());
            helper.getView(R.id.girl_item_ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentPosition",helper.getAdapterPosition()+1);
                    bundle.putSerializable("imageInfoList", (Serializable) mList);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(mContext, BigimgshowActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
