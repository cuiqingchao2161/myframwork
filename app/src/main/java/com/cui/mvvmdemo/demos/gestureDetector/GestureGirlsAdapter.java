package com.cui.mvvmdemo.demos.gestureDetector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cui.mvvmdemo.R;
import com.cui.mvvmdemo.bean.Girl;
import com.cui.mvvmdemo.ui.activity.BigimgshowActivity;
import com.cui.mvvmdemo.utils.ImageUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lcui on 17/8/29.
 */
public class GestureGirlsAdapter extends BaseQuickAdapter<Girl, BaseViewHolder> {

    private Context mContext;
    private List<Girl> mList;
    private boolean firstHover = true;

    public GestureGirlsAdapter(Context context, List<Girl> list) {
        super(R.layout.gesture_girl_layout, list);
        this.mContext = context;
        mList = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, Girl item) {
        if (item != null) {
            Glide.with(mContext).load(item.getPicPath()).apply(ImageUtil.getOption()).into((ImageView) helper.getView(R.id.girl_iv));
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

            helper.getView(R.id.girl_item_ll).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ((TextView) helper.getView(R.id.girl_name)).setTextColor(mContext.getResources().getColor(R.color.blue_color));
                }
            });
            if (helper.getAdapterPosition() == 0 && firstHover) {
                helper.getView(R.id.girl_item_ll).requestFocus();
                firstHover = false;
            }
        }
    }
}
