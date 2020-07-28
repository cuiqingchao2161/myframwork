package com.cui.lib.net;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Custom backback ,mapping retrofit callback to our original SCResultInterface
 * Created by zhangxingxing-PC on 2015/12/8.
 */
public class CustomCallback<T> implements Callback<T> {

    private Context mContext;

    private SCResultInterface mResultInterface;

    private AbstractPostRequestHandler<T> mPostRequestHandler;

    private SCSuccess mScSuccess = SCSuccess.defaultSuccess();

    private boolean mEnableCallback = true;

    public boolean isEnableCallback() {
        return mEnableCallback;
    }

    public void setEnableCallback(boolean mEnableCallback) {
        this.mEnableCallback = mEnableCallback;
    }

    public CustomCallback(SCResultInterface mResultInterface, Context context) {
        this.mContext = context;
        this.mResultInterface = mResultInterface;
    }

    public void setPostRequestHandler(AbstractPostRequestHandler<T> mPostRequestHandler) {
        this.mPostRequestHandler = mPostRequestHandler;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {

            if (response.body() instanceof SCResponseModel) {
                SCResponseModel model = (SCResponseModel) response.body();
                if (model.isResponseSuccess()) {

                    if (mEnableCallback) {
                        mResultInterface.onSuccess(((SCResponseModel) response.body()).getSyb_data(), mScSuccess, null);
                    }

                    if (mPostRequestHandler != null) {
                        mPostRequestHandler.onPostExecute(response.body());
                    }
                } else if (model.getSyb_status().equals(SCConstants.SC_STATUS_SESSION_TOKEN_FAILED)) {
                    if (mContext != null) {
                        mContext.sendBroadcast(new Intent(SCConstants.BROADCAST_RECEIVER_LOGOUT));
                    }
                } else {
                    if (!TextUtils.isEmpty(model.getSyb_status())) {
                        int errorCode = Integer.parseInt(model.getSyb_status());
                        SCError mScError = new SCError(errorCode, model.getSyb_info(), model.getSyb_info(), model.getSyb_info());
                        if (mEnableCallback) {
                            mResultInterface.onFailure(null, mScError, null);
                        }
                    }
                }
            }
        } else {

            SCError mScError = new SCError(1000, SCConstants.SCErrorCode.get(1000)
                    .toString(), "", "");
            mResultInterface.onFailure(null, mScError, null);

        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        SCError mScError = null;
        if (throwable instanceof IOException) {
            mScError = new SCError(1099, "Network problem"
                    .toString(), "", "");
        } else {
            mScError = new SCError(1000, SCConstants.SCErrorCode.get(1000)
                    .toString(), "", "");
        }

        mResultInterface.onFailure(null, mScError, throwable);
    }
}