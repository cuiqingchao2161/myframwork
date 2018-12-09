
package com.zx.mvvmdemo.viewmodel;


import com.zx.mvvmdemo.bean.NewsData;

public interface NewsItemClickCallback {
    void onClick(NewsData.ResultsBean newsItem);
}
