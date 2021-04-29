package com.cui.mvvmdemo.demos.recyclerViewTest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cui.mvvmdemo.R;
import com.cui.mvvmdemo.bean.Girl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2020/9/29 14:01
 */
public class RecyclerTransAdapter extends RecyclerView.Adapter<RecyclerTransAdapter.ViewHolder> {
    private int type; //控制布局类型的变量
    private List<Girl> listData; //RecyclerView数据
    private Activity myActivityContext; //Activity上下文
    private OnItemClickListener onItemClickListener; //存放点击事件的接口
    /*构造器*/
    public RecyclerTransAdapter(List<Girl> listData, Activity myActivityContext) {
        this.listData = listData;
        this.myActivityContext = myActivityContext;
    }
    /*内部接口：存放点击事件*/
    public interface OnItemClickListener {
        /*整条数据的点击事件*/
        void onItemClick(View view, Girl data, int position);
    }
    /*设置点击事件的方法*/
    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /*创建ViewHolder   设置RecyclerViewItem布局*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
        } else if (viewType == 1) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        return type;
    }
    /*绑定数据*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(myActivityContext).load(listData.get(position).getPicPath()).into(holder.ivPicture);
    }
    /*告诉RecyclerView有多少条数据*/
    @Override
    public int getItemCount() {
        return listData.size();
    }
    /**
     * 对外方法 用于切换布局方式
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }
    /*//获取列表项的控件*/
    static class ViewHolder extends RecyclerView.ViewHolder {
        /*控件*/
        ImageView ivPicture; //图片

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            /*获取控件*/
            ivPicture = itemView.findViewById(R.id.iv_item);
        }
    }
}
