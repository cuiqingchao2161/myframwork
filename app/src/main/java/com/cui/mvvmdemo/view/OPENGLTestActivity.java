package com.cui.mvvmdemo.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cui.vrlibrary.glview.GLPhotoView;
import com.cui.vrlibrary.model.Photo;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class OPENGLTestActivity extends AppCompatActivity {
    GLPhotoView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        glSurfaceView = new GLPhotoView(this);
        glSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));

        setContentView(glSurfaceView);
        Bitmap bitmap = null;


        try {
            bitmap = BitmapFactory.decodeStream(getApplicationContext().getResources().getAssets().open("df.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photo photo = new Photo(bitmap);
        glSurfaceView.setTexture(photo);
    }


    ViewHandler viewHandler ;

    static class ViewHandler extends Handler {
        WeakReference<View> mView;

        ViewHandler(View activity) {
            super(Looper.getMainLooper());
            mView = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            View theView = mView.get();
            if (theView == null || !theView.isAttachedToWindow()) {
                return;
            }

//            GestureTouchUtils.simulateScroll(theView, 10000, 400, 0, 400, 100000, GestureTouchUtils.HIGH);
//            sendEmptyMessageDelayed(1, 11800);

        }
    }
}
