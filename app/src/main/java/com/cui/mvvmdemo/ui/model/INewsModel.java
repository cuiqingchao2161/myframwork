package com.cui.mvvmdemo.ui.model;

import com.cui.lib.base.BaseLoadListener;
import com.cui.mvvmdemo.bean.SimpleNewsBean;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public interface INewsModel {
    /**
     * 获取新闻数据
     *
     * @param page 页数
     * @param loadListener
     */
    void loadNewsData(int page, BaseLoadListener<SimpleNewsBean> loadListener);
}
