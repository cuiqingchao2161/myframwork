
package com.cui.mvvmdemo.viewmodel;


import com.cui.mvvmdemo.bean.NewsData;

public interface NewsItemClickCallback {
    void onClick(NewsData.ResultsBean newsItem);
}
