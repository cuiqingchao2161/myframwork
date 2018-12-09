package com.zx.mvvmdemo.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;


/**
 * Created by Administrator on 2018/12/9.
 */

public class MyProgressDialog {
    private ProgressDialog mProgressDialog;
    private int mShowingNum = 0;
    private Context mContext;
    private final String mShowMessage = "加载中";

    public MyProgressDialog(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
        initListener();
    }

    private void initListener(){
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingNum = 0;
            }
        });
    }

    public void showProgress(){
        mShowingNum++;
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage(mShowMessage);
        mProgressDialog.show();
    }

    public void showProgress(String message){
        mShowingNum++;
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hideProgress(){
        if(mShowingNum > 0){
            mShowingNum--;
        }

        if(mShowingNum == 0){
            mProgressDialog.dismiss();
        }
    }
}
