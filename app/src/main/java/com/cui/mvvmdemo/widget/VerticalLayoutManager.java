package com.cui.mvvmdemo.widget;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 *
 * @author xujiangang
 * @date 2018/5/25
 */

public class VerticalLayoutManager extends LinearLayoutManager {
    public VerticalLayoutManager(Context context) {
        super(context);
        setOrientation(RecyclerView.VERTICAL);
        setAutoMeasureEnabled(true);
    }


    /**
     * 修复原生recycleview存在的bug，notifyDataSetChanged前后数据不一致导致IndexOutOfBoundsException异常崩溃
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void collectAdjacentPrefetchPositions(int dx, int dy, RecyclerView.State state, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        try {
            super.collectAdjacentPrefetchPositions(dx, dy, state, layoutPrefetchRegistry);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}