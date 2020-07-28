package com.cui.mvvmdemo.ui.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.cui.mvvmdemo.R;


/**
 *
 * @author xujiangang
 * @date 15/03/2019
 * Email: jiangang.xu@hiscene.com
 */
public class LoadingDialog extends Dialog {

    private TextView mTxtContent;
    private String mStrContent;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
        mTxtContent = findViewById(R.id.dialog_loading_text);
    }
    public void setMessage(String message){
        this.mStrContent = message;
        if (mStrContent != null && mTxtContent != null) {
            mTxtContent.setText(mStrContent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        mTxtContent = findViewById(R.id.dialog_loading_text);
        mTxtContent.setText(mStrContent);
    }


}
