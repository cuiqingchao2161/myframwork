package com.zx.mvvmdemo.module;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.zx.mvvmdemo.R;

import java.lang.ref.WeakReference;

public class OPENGLTestActivity extends AppCompatActivity {
    GLPhotoView glSurfaceView;
    Button roate_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_opengltest);
//        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView = (GLPhotoView) findViewById(R.id.gl_view);
        glSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//        setContentView(glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
//        glSurfaceView.init(this);
        glSurfaceView.setRenderer();
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glSurfaceView.requestFocus();
        roate_btn = (Button) findViewById(R.id.button_roate);
        roate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRoate();
            }
        });
    }


    private void startRoate(){
        glSurfaceView.play();
//        GestureTouchUtils.simulateScroll(glSurfaceView, 10000, 400, 0, 400, 100000, GestureTouchUtils.HIGH);
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

            GestureTouchUtils.simulateScroll(theView, 10000, 400, 0, 400, 100000, GestureTouchUtils.HIGH);
//            sendEmptyMessageDelayed(1, 11800);

        }
    }
}
