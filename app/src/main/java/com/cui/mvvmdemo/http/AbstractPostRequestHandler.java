package com.cui.mvvmdemo.http;

/**
 * Created by zhangxingxing-PC on 2015/12/8.
 */
public abstract class AbstractPostRequestHandler<T> {

    public abstract void onPostExecute(T t);

}