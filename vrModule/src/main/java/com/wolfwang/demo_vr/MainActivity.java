package com.wolfwang.demo_vr;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private VrPanoramaView vrPanoramaView;
    private boolean loadImageSuccessful;//全景图是不是加载成功

    private Uri fileUri;
    private VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();
    private ImageLoaderTask backgroundImageLoaderTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vrPanoramaView = (VrPanoramaView) findViewById(R.id.pano_view);
        vrPanoramaView.setEventListener(new ActivityEventListener());
//
//        vrPanoramaView.setFullscreenButtonEnabled (false); //隐藏全屏模式按钮
//        vrPanoramaView.setInfoButtonEnabled(false); //设置隐藏最左边信息的按钮
//        vrPanoramaView.setStereoModeButtonEnabled(false); //设置隐藏立体模型的按钮
//        vrPanoramaView.setEventListener(new ActivityEventListener()); //设置监听
//        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
//        //加载本地的图片源
//        vrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(istr), panoOptions);



        handleIntent(getIntent());
    }
    private Target mTarget;// keep the reference for picasso.

    private void handleIntent(Intent intent) {
        //由于刚启动或者是加载activity 或者是app的时候由于旋转而产生的动态变化，
      loadImage();
//
//
//        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
//            Log.i(TAG, "ACTION_VIEW Intent recieved");
//            fileUri = intent.getData();
//            if (fileUri == null) {
//                Log.w(TAG, "No data uri specified. Use \"-d /path/filename\".");
//            } else {
//                Log.i(TAG, "Using file " + fileUri.toString());
//            }
//
//            panoOptions.inputType = intent.getIntExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
//            Log.i(TAG, "Options.inputType = " + panoOptions.inputType);
//        } else {
//            Log.i(TAG, "Intent is not ACTION_VIEW. Using default pano image.");
//            fileUri = null;
//            panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
//        }
//
//        // Load the bitmap in a background thread to avoid blocking the UI thread. This operation can
//        // take 100s of milliseconds.
//        if (backgroundImageLoaderTask != null) {
//            // Cancel any task from a previous intent sent to this activity.
//            backgroundImageLoaderTask.cancel(true);
//        }
//        backgroundImageLoaderTask = new ImageLoaderTask();
//        backgroundImageLoaderTask.execute(Pair.create(fileUri, panoOptions));
    }

    private void loadImage(){
        String url = "https://pannellum.org/images/alma.jpg";
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "loaded image, size:" + bitmap.getWidth() + "," + bitmap.getHeight());

                // notify if size changed
                VrPanoramaView.Options panoOptions = null;
                panoOptions = new VrPanoramaView.Options();
                panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
                vrPanoramaView.loadImageFromBitmap(bitmap, panoOptions);
//                startRoate();
//                startAnimation();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getApplicationContext())
                .load(Uri.parse(url))
//                    .resize(callback.getMaxTextureSize(),callback.getMaxTextureSize())
//                    .onlyScaleDown()
//                    .centerInside()
                .memoryPolicy(NO_CACHE, NO_STORE)
                .into(mTarget);
    }

    @Override
    protected void onPause() {
        vrPanoramaView.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vrPanoramaView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        // Destroy the widget and free memory.
        vrPanoramaView.shutdown();

        // The background task has a 5 second timeout so it can potentially stay alive for 5 seconds
        // after the activity is destroyed unless it is explicitly cancelled.
        if (backgroundImageLoaderTask != null) {
            backgroundImageLoaderTask.cancel(true);
        }
        super.onDestroy();
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public class ActivityEventListener extends VrPanoramaEventListener {
        @Override
        public void onLoadSuccess() {
            loadImageSuccessful = true;
        }
        @Override
        public void onLoadError(String errorMessage) {
            loadImageSuccessful=false;
            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public class ImageLoaderTask extends AsyncTask<Pair<Uri, VrPanoramaView.Options>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Pair<Uri, VrPanoramaView.Options>... fileInformation) {
            VrPanoramaView.Options panoOptions = null;  // It's safe to use null VrPanoramaView.Options.
            InputStream istr = null;
            if (fileInformation == null || fileInformation.length < 1
                    || fileInformation[0] == null || fileInformation[0].first == null) {
                AssetManager assetManager = getAssets();
                try {
                    istr = assetManager.open("abc.jpg");
                    panoOptions = new VrPanoramaView.Options();
                    panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
                } catch (IOException e) {
                    Log.e(TAG, "Could not decode default bitmap: " + e);
                    return false;
                }
            } else {
                try {
                    istr = new FileInputStream(new File(fileInformation[0].first.getPath()));
                    panoOptions = fileInformation[0].second;
                } catch (IOException e) {
                    Log.e(TAG, "Could not load file: " + e);
                    return false;
                }
            }

            vrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(istr), panoOptions);
            try {
                istr.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close input stream: " + e);
            }

            return true;
        }
    }
}
